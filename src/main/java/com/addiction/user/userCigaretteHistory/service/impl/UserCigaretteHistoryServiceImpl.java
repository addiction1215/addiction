package com.addiction.user.userCigaretteHistory.service.impl;

import com.addiction.global.security.SecurityService;
import com.addiction.global.time.KoreaTime;
import com.addiction.smokefree.service.SmokeFreeConfirmationReadService;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.enums.CalendarSmokingStatus;
import com.addiction.user.userCigaretteHistory.enums.PeriodType;
import com.addiction.user.userCigaretteHistory.enums.SmokingFeedback;
import com.addiction.user.userCigaretteHistory.enums.StatsFeedback;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.userCigaretteHistory.service.response.*;
import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCigaretteHistoryServiceImpl implements UserCigaretteHistoryService {

    private static final DateTimeFormatter BASIC_ISO_DATE = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    // 시간 변환 관련 상수
    private static final double SECONDS_PER_HOUR = 3600.0;

    // 반올림 관련 상수
    private static final double DECIMAL_MULTIPLIER = 10.0;

    // 날짜 관련 상수
    private static final int ONE_DAY = 1;
    private static final int DAYS_IN_WEEK = 7;
    private static final int DAYS_FROM_SUNDAY_TO_SATURDAY = 6;
    private static final int ONE_WEEK = 1;
    private static final int ONE_MONTH = 1;

    // 퍼센트 계산 상수
    private static final double PERCENTAGE_MULTIPLIER = 100.0;

    private final SecurityService securityService;
    private final UserCigaretteReadService userCigaretteReadService;
    private final UserCigaretteHistoryRepository userCigaretteHistoryRepository;
    private final UserReadService userReadService;
    private final SmokeFreeConfirmationReadService smokeFreeConfirmationReadService;
    private final KoreaTime koreaTime;

    @Override
    public void save(String monthStr, String dateStr, Long userId, Integer smokeCount, Long avgPatienceTime,
                     List<CigaretteHistoryDocument.History> historyList) {
        LocalDateTime smokeDate = LocalDate.parse(dateStr, BASIC_ISO_DATE).atStartOfDay();
        CigaretteHistoryDocument doc = CigaretteHistoryDocument.builder()
                .smokeDate(smokeDate)
                .month(monthStr)
                .date(dateStr)
                .userId(userId)
                .smokeCount(smokeCount)
                .avgPatienceTime(avgPatienceTime)
                .history(historyList)
                .build();

        userCigaretteHistoryRepository.save(doc);
    }

    @Override
    public List<UserCigaretteHistoryCalenderResponse> findCalendarByDate(String month) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        // BASIC_ISO_DATE(yyyyMMdd) 형식으로 파싱하기 위해 요청 월(yyyyMM)에 01일을 붙인다.
        // 예: 20260901 -> 2026-09-01
        LocalDate firstDay = LocalDate.parse(month + "01", BASIC_ISO_DATE);
        
        // lengthOfMonth()는 해당 월의 총 일수(2월은 윤년 여부 포함)를 반환한다.
        // 그 일 수를 일자로 설정해 해당 월의 마지막 날을 구한다. 예: 2026-02-01 -> 2026-02-28
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        Set<LocalDate> successfulDates = smokeFreeConfirmationReadService.findSuccessfulDates(userId, firstDay, lastDay);
        List<UserCigaretteHistoryCalenderResponse> results = userCigaretteHistoryRepository.findByMonthAndUserId(month, userId).stream()
                .map(doc -> {
                    LocalDate date = LocalDate.parse(doc.getDate(), BASIC_ISO_DATE);
                    CalendarSmokingStatus status = doc.getSmokeCount() > 0
                            ? CalendarSmokingStatus.SMOKED
                            : successfulDates.contains(date)
                                    ? CalendarSmokingStatus.SMOKE_FREE
                                    : CalendarSmokingStatus.UNLOGGED;
                    return UserCigaretteHistoryCalenderResponse.createResponse(
                            doc.getDate(),
                            doc.getSmokeCount(),
                            status
                    );
                })
                .collect(Collectors.toList());

        // 금연 확정일에 MongoDB 일별 문서가 없어도 캘린더에 성공일로 표시한다.
        Set<String> recordedDates = results.stream()
                .map(UserCigaretteHistoryCalenderResponse::getDate)
                .collect(Collectors.toSet());
        for (LocalDate successfulDate : successfulDates) {
            String date = successfulDate.format(BASIC_ISO_DATE);
            if (recordedDates.add(date)) {
                results.add(UserCigaretteHistoryCalenderResponse.createResponse(
                        date, 0, CalendarSmokingStatus.SMOKE_FREE));
            }
        }

        // 당일 데이터 추가 (RDBMS에서 조회)
        LocalDate currentDate = koreaTime.today();
        String today = currentDate.format(BASIC_ISO_DATE);
        String todayMonth = currentDate.format(MONTH_FORMATTER);

        if (month.equals(todayMonth)) {
            LocalDateTime startOfDay = currentDate.atStartOfDay();
            LocalDateTime endOfDay = currentDate.plusDays(ONE_DAY).atStartOfDay();

            List<UserCigarette> todayCigarettes = userCigaretteReadService.findAllByUserIdAndCreatedDateBetween(userId, startOfDay, endOfDay);

            if (!todayCigarettes.isEmpty()) {
                results.removeIf(result -> result.getDate().equals(today));
                results.add(UserCigaretteHistoryCalenderResponse.createResponse(
                        today,
                        todayCigarettes.size(),
                        CalendarSmokingStatus.SMOKED
                ));
            }
        }

        return results;
    }

    @Override
    public List<UserCigaretteHistoryResponse> findHistoryByDate(String date) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        LocalDate currentDate = koreaTime.today();
        String today = currentDate.format(BASIC_ISO_DATE);

        // 당일 데이터인 경우 RDBMS에서 조회
        if (date.equals(today)) {
            LocalDateTime startOfDay = currentDate.atStartOfDay();
            LocalDateTime endOfDay = currentDate.plusDays(ONE_DAY).atStartOfDay();

            return userCigaretteReadService.findAllByUserIdAndCreatedDateBetween(userId, startOfDay, endOfDay)
                    .stream()
                    .map(c -> UserCigaretteHistoryResponse.createResponse(
                            CigaretteHistoryDocument.History.builder()
                                    .address(c.getAddress())
                                    .smokeTime(c.getSmokeTime())
                                    .smokePatienceTime(c.getSmokePatienceTime())
                                    .build()
                    ))
                    .collect(Collectors.toList());
        }

        // 과거 데이터는 MongoDB에서 조회
        CigaretteHistoryDocument doc = userCigaretteHistoryRepository.findByDateAndUserId(date, userId);
        if (doc == null || doc.getHistory() == null) {
            return List.of();
        }

        return doc.getHistory()
                .stream()
                .map(UserCigaretteHistoryResponse::createResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserCigaretteHistoryGraphResponse findGraphByPeriod(PeriodType periodType) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        return switch (periodType) {
            case WEEKLY -> buildWeeklyGraph(userId);
            case MONTHLY -> buildMonthlyGraph(userId);
            case SIXMONTHLY -> buildMonthAggGraph(userId, 6);
            case YEARLY -> buildMonthAggGraph(userId, 12);
        };
    }

    private UserCigaretteHistoryGraphResponse buildWeeklyGraph(Long userId) {
        LocalDate endDate = koreaTime.yesterday();
        LocalDate startDate = endDate.minusDays(DAYS_IN_WEEK - 1L);

        Map<String, CigaretteHistoryDocument> docMap = new HashMap<>();
        userCigaretteHistoryRepository.findByUserIdAndDateBetween(
                        userId,
                        startDate.format(BASIC_ISO_DATE),
                        endDate.format(BASIC_ISO_DATE))
                .forEach(d -> docMap.put(d.getDate(), d));

        List<UserCigaretteHistoryGraphDateResponse> countList = new ArrayList<>();
        List<UserCigaretteHistoryGraphDateResponse> patientList = new ArrayList<>();

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            LocalDate day = startDate.plusDays(i);
            String label = day.toString();
            CigaretteHistoryDocument doc = docMap.get(day.format(BASIC_ISO_DATE));
            countList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, doc != null ? doc.getSmokeCount() : 0));
            // 주간 그래프는 최근 7일의 날짜별 avgPatienceTime을 각 구간 값으로 사용한다.
            // 기록이 없는 날짜는 0초로 표시한다.
            patientList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, doc != null ? doc.getAvgPatienceTime() : 0));
        }

        return buildGraphResponse(countList, patientList);
    }

    /**
     * 최근 완료된 5주를 주 단위로 집계해 그래프 응답을 만든다.
     * 각 주의 흡연 횟수 합계와 기록된 일별 참은 시간의 평균을 오래된 주부터 반환한다.
     */
    private UserCigaretteHistoryGraphResponse buildMonthlyGraph(Long userId) {
        // 진행 중인 이번 주는 제외하고, 마지막으로 완료된 지난주부터 5주를 조회한다.
        // 예: 오늘이 2026-10-09(금)이면 이번 주 월요일(10-05)에서 1주를 빼 2026-09-28(월)을 구한다.
        LocalDate lastCompletedWeekStart = koreaTime.today()
                .with(DayOfWeek.MONDAY)
                .minusWeeks(ONE_WEEK);

        // x축에 표시할 주별 흡연 횟수와 평균 참은 시간을 오래된 주부터 저장한다.
        List<UserCigaretteHistoryGraphDateResponse> countList = new ArrayList<>();
        List<UserCigaretteHistoryGraphDateResponse> patientList = new ArrayList<>();

        // w를 4부터 0까지 감소시켜 5주 전 데이터부터 지난주 데이터 순으로 그래프를 구성한다.
        for (int w = 4; w >= 0; w--) {
            // 예: lastCompletedWeekStart가 2026-09-28이고 w가 2이면 weekStart는 2026-09-14이다.
            LocalDate weekStart = lastCompletedWeekStart.minusWeeks(w);
            // 예: weekStart가 월요일(09-14)이면 6일 뒤인 일요일(09-20)까지 해당 주 범위로 조회한다.
            LocalDate weekEnd = weekStart.plusDays(DAYS_FROM_SUNDAY_TO_SATURDAY);
            // 예: 2026-09-14는 그래프 라벨 "09/14"로 표시한다.
            String label = weekStart.format(DateTimeFormatter.ofPattern("MM/dd"));

            // 해당 주의 월요일부터 일요일까지 저장된 일별 흡연 기록을 모두 조회한다.
            List<CigaretteHistoryDocument> docs = userCigaretteHistoryRepository.findByUserIdAndDateBetween(
                    userId, weekStart.format(BASIC_ISO_DATE), weekEnd.format(BASIC_ISO_DATE));

            // 기록이 없는 날은 조회 결과에 없으므로, 있는 기록의 흡연 횟수만 합산한다.
            long totalCount = docs.stream().mapToLong(CigaretteHistoryDocument::getSmokeCount).sum();
            // [평균 참은 시간 계산 3/4] 기간 버킷(주/월)에 포함된 일별 avgPatienceTime을 평균 내어
            // 그래프의 각 구간 값(patient.date[].value)을 만든다. 소수점 이하는 버린다.
            long avgPatience = (long) docs.stream().mapToLong(CigaretteHistoryDocument::getAvgPatienceTime).average().orElse(0);

            countList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, totalCount));
            patientList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, avgPatience));
        }

        // 흡연 횟수와 평균 참은 시간을 각각 그래프의 두 데이터 계열로 묶어 반환한다.
        return buildGraphResponse(countList, patientList);
    }

    /**
     * 최근 완료된 {@code months}개월을 월 단위로 집계해 그래프 응답을 만든다.
     * 각 달의 흡연 횟수 합계와 기록된 일별 참은 시간의 평균을 오래된 달부터 반환한다.
     */
    private UserCigaretteHistoryGraphResponse buildMonthAggGraph(Long userId, int months) {
        // 진행 중인 이번 달은 제외하고, 마지막으로 완료된 지난달부터 월별 집계를 조회한다.
        // 예: 오늘이 2026-10-09이면 이번 달 1일(10-01)에서 1개월을 빼 2026-09-01을 구한다.
        LocalDate lastCompletedMonth = koreaTime.today()
                .withDayOfMonth(1)
                .minusMonths(ONE_MONTH);

        // x축에 표시할 월별 흡연 횟수와 평균 참은 시간을 오래된 달부터 저장한다.
        List<UserCigaretteHistoryGraphDateResponse> countList = new ArrayList<>();
        List<UserCigaretteHistoryGraphDateResponse> patientList = new ArrayList<>();

        // m을 months - 1부터 0까지 감소시켜 요청한 기간 중 가장 오래된 달부터 지난달 순으로 구성한다.
        for (int m = months - 1; m >= 0; m--) {
            // 예: lastCompletedMonth가 2026-09-01이고 m이 2이면 monthStart는 2026-07-01이다.
            LocalDate monthStart = lastCompletedMonth.minusMonths(m);
            // 예: 2026-07-01의 마지막 날은 해당 월의 일수(31일)를 사용해 2026-07-31로 구한다.
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
            // 예: 2026-07-01은 MONTH_FORMATTER 형식의 그래프 라벨로 표시한다.
            String label = monthStart.format(MONTH_FORMATTER);

            // 해당 달의 1일부터 마지막 날까지 저장된 일별 흡연 기록을 모두 조회한다.
            List<CigaretteHistoryDocument> docs = userCigaretteHistoryRepository.findByUserIdAndDateBetween(
                    userId, monthStart.format(BASIC_ISO_DATE), monthEnd.format(BASIC_ISO_DATE));

            // 기록이 없는 날은 조회 결과에 없으므로, 있는 기록의 흡연 횟수만 합산한다.
            long totalCount = docs.stream().mapToLong(CigaretteHistoryDocument::getSmokeCount).sum();
            // [평균 참은 시간 계산 3/4] 기간 버킷(주/월)에 포함된 일별 avgPatienceTime을 평균 내어
            // 그래프의 각 구간 값(patient.date[].value)을 만든다. 소수점 이하는 버린다.
            long avgPatience = (long) docs.stream().mapToLong(CigaretteHistoryDocument::getAvgPatienceTime).average().orElse(0);

            countList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, totalCount));
            patientList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, avgPatience));
        }

        // 흡연 횟수와 평균 참은 시간을 각각 그래프의 두 데이터 계열로 묶어 반환한다.
        return buildGraphResponse(countList, patientList);
    }

    private UserCigaretteHistoryGraphResponse buildGraphResponse(
            List<UserCigaretteHistoryGraphDateResponse> countList,
            List<UserCigaretteHistoryGraphDateResponse> patientList) {
        int avgCount = countList.isEmpty() ? 0 : (int) Math.round(
                countList.stream().mapToLong(UserCigaretteHistoryGraphDateResponse::getValue).average().orElse(0));
        // [평균 참은 시간 계산 4/4] 그래프에 표시할 모든 구간 값을 단순 평균한 뒤 반올림하여
        // 대표값 avgSmokePatientTime으로 반환한다. 즉 전체 흡연 간격의 가중 평균은 아니다.
        long avgPatience = patientList.isEmpty() ? 0 : Math.round(
                patientList.stream().mapToLong(UserCigaretteHistoryGraphDateResponse::getValue).average().orElse(0));
        return UserCigaretteHistoryGraphResponse.createResponse(
                UserCigaretteHistoryGraphCountResponse.createResponse(avgCount, countList),
                UserCigaretteHistoryGraphPatientResponse.createResponse(avgPatience, patientList)
        );
    }

    @Override
    public UserCigaretteHistoryLastestResponse findLastestByUserId() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        User user = userReadService.findById(userId);
        if (user.getLastSmokeAt() != null) {
            return UserCigaretteHistoryLastestResponse.createResponse(
                    user.getLastSmokeAt(), user.getLastSmokeAddress());
        }

        // lastSmokeAt 백필 전 기존 사용자에 대해서만 기존 이력 조회를 fallback으로 사용한다.
        UserCigarette cigarette = userCigaretteReadService.findLatestByUserId(userId);
        if (cigarette == null) {
            CigaretteHistoryDocument doc = userCigaretteHistoryRepository.findLatestByUserId(userId);
            if (doc != null && doc.getHistory() != null && !doc.getHistory().isEmpty()) {
                CigaretteHistoryDocument.History latestHistory = doc.getHistory().stream()
                        .filter(history -> history.getSmokeTime() != null)
                        .max(java.util.Comparator.comparing(CigaretteHistoryDocument.History::getSmokeTime))
                        .orElse(null);
                if (latestHistory == null) {
                    return UserCigaretteHistoryLastestResponse.createResponse(null, null);
                }
                return UserCigaretteHistoryLastestResponse.createResponse(
                        latestHistory.getSmokeTime(), latestHistory.getAddress()
                );
            }
            return UserCigaretteHistoryLastestResponse.createResponse(
                    null,
                    null
            );
        }

        return UserCigaretteHistoryLastestResponse.createResponse(cigarette.getSmokeTime(), cigarette.getAddress());
    }

    @Override
    public WeeklyComparisonResponse compareWeekly() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();

        List<CigaretteHistoryDocument> lastWeekDocs = getLastWeekDocuments(userId);
        List<CigaretteHistoryDocument> thisWeekDocs = getThisWeekDocuments(userId);

        // 횟수 비교
        int lastWeekCount = lastWeekDocs.stream().mapToInt(CigaretteHistoryDocument::getSmokeCount).sum();
        int thisWeekCount = thisWeekDocs.stream().mapToInt(CigaretteHistoryDocument::getSmokeCount).sum();
        double countDiff = thisWeekCount - lastWeekCount;
        double countChangeRate = lastWeekCount == 0
                ? (thisWeekCount > 0 ? PERCENTAGE_MULTIPLIER : 0.0)
                : (countDiff / lastWeekCount) * PERCENTAGE_MULTIPLIER;

        // 시간 비교 (초 → 시간)
        double lastWeekAvgTime = lastWeekDocs.stream()
                .mapToLong(CigaretteHistoryDocument::getAvgPatienceTime).average().orElse(0.0) / SECONDS_PER_HOUR;
        double thisWeekAvgTime = thisWeekDocs.stream()
                .mapToLong(CigaretteHistoryDocument::getAvgPatienceTime).average().orElse(0.0) / SECONDS_PER_HOUR;
        double timeDiff = thisWeekAvgTime - lastWeekAvgTime;
        double timeChangeRate = lastWeekAvgTime == 0
                ? (thisWeekAvgTime > 0 ? PERCENTAGE_MULTIPLIER : 0.0)
                : (timeDiff / lastWeekAvgTime) * PERCENTAGE_MULTIPLIER;

        return WeeklyComparisonResponse.createResponse(
                lastWeekCount, thisWeekCount,
                round(countDiff), round(countChangeRate),
                round(lastWeekAvgTime), round(thisWeekAvgTime),
                round(timeDiff), round(timeChangeRate)
        );
    }

    private double round(double value) {
        return Math.round(value * DECIMAL_MULTIPLIER) / DECIMAL_MULTIPLIER;
    }

    /**
     * 지난주 데이터 조회 (MongoDB)
     * 지난주 월요일 ~ 일요일의 데이터를 MongoDB에서 조회
     */
    private List<CigaretteHistoryDocument> getLastWeekDocuments(Long userId) {
        LocalDate today = koreaTime.today();

        // 이번주 월요일 계산
        LocalDate thisWeekMonday = today.with(DayOfWeek.MONDAY);

        // 지난주 월요일, 일요일 계산
        LocalDate lastWeekMonday = thisWeekMonday.minusWeeks(ONE_WEEK);
        LocalDate lastWeekSunday = lastWeekMonday.plusDays(DAYS_FROM_SUNDAY_TO_SATURDAY);

        String startDate = lastWeekMonday.format(BASIC_ISO_DATE);
        String endDate = lastWeekSunday.format(BASIC_ISO_DATE);

        return userCigaretteHistoryRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    /**
     * 이번주 데이터 조회 (MongoDB + RDBMS)
     * 이번주 월요일 ~ 어제: MongoDB
     * 오늘: RDBMS
     */
    private List<CigaretteHistoryDocument> getThisWeekDocuments(Long userId) {
        LocalDate today = koreaTime.today();

        // 이번주 월요일 계산
        LocalDate thisWeekMonday = today.with(DayOfWeek.MONDAY);

        List<CigaretteHistoryDocument> thisWeekDocs = new ArrayList<>();

        // MongoDB에서 이번주 월요일 ~ 어제까지 조회
        if (today.isAfter(thisWeekMonday)) {
            String startDate = thisWeekMonday.format(BASIC_ISO_DATE);
            String endDate = today.minusDays(ONE_DAY).format(BASIC_ISO_DATE);

            thisWeekDocs.addAll(userCigaretteHistoryRepository.findByUserIdAndDateBetween(userId, startDate, endDate));
        }

        // 오늘 데이터 추가 (RDBMS에서 조회)
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(ONE_DAY).atStartOfDay();
        List<UserCigarette> todayCigarettes = userCigaretteReadService.findAllByUserIdAndCreatedDateBetween(
                userId, startOfDay, endOfDay
        );

        // 오늘 데이터를 Document로 변환하여 추가
        if (!todayCigarettes.isEmpty()) {
            CigaretteHistoryDocument todayDoc = convertToCigaretteHistoryDocument(todayCigarettes, userId, today);
            thisWeekDocs.add(todayDoc);
        }

        return thisWeekDocs;
    }

    /**
     * UserCigarette 리스트를 CigaretteHistoryDocument로 변환
     *
     * @param cigarettes 담배 흡연 기록 리스트
     * @param userId     사용자 ID
     * @param today      호출한 쪽에서 확정한 기준 날짜(한국 시간).
     *                   자정 경계에서 호출부와 날짜가 어긋나지 않도록 여기서 다시 읽지 않고 전달받는다.
     * @return 변환된 CigaretteHistoryDocument
     */
    private CigaretteHistoryDocument convertToCigaretteHistoryDocument(List<UserCigarette> cigarettes, Long userId,
                                                                      LocalDate today) {
        // UserCigarette -> History 변환
        List<CigaretteHistoryDocument.History> historyList = cigarettes.stream()
                .map(c -> CigaretteHistoryDocument.History.builder()
                        .address(c.getAddress())
                        .smokeTime(c.getSmokeTime())
                        .smokePatienceTime(c.getSmokePatienceTime())
                        .build())
                .collect(Collectors.toList());

        // 평균 금연 시간 계산
        Long avgPatienceTime = (long) cigarettes.stream()
                .mapToLong(UserCigarette::getSmokePatienceTime)
                .average()
                .orElse(0);

        // CigaretteHistoryDocument 생성
        return CigaretteHistoryDocument.builder()
                .date(today.format(BASIC_ISO_DATE))
                .userId(userId)
                .smokeCount(cigarettes.size())
                .avgPatienceTime(avgPatienceTime)
                .history(historyList)
                .build();
    }

    @Override
    public WeeklyCigaretteResponse findThisWeekCigarettes() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        LocalDate today = koreaTime.today();

        // 이번 주 일요일 계산 (DayOfWeek.SUNDAY는 7)
        LocalDate thisSunday = today.with(DayOfWeek.SUNDAY);

        // 만약 오늘이 일요일보다 이전이면 지난주 일요일을 의미하므로 다시 계산
        if (today.isBefore(thisSunday)) {
            thisSunday = thisSunday.minusWeeks(ONE_WEEK);
        }

        // MongoDB에서 일요일 ~ 어제까지 데이터 조회
        List<CigaretteHistoryDocument> weekDocs = new ArrayList<>();
        if (today.isAfter(thisSunday)) {
            String startDate = thisSunday.format(BASIC_ISO_DATE);
            String endDate = today.minusDays(ONE_DAY).format(BASIC_ISO_DATE);
            weekDocs.addAll(userCigaretteHistoryRepository.findByUserIdAndDateBetween(userId, startDate, endDate));
        }

        // 오늘 데이터 추가 (RDBMS에서 조회)
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(ONE_DAY).atStartOfDay();
        List<UserCigarette> todayCigarettes = userCigaretteReadService.findAllByUserIdAndCreatedDateBetween(
                userId, startOfDay, endOfDay
        );

        if (!todayCigarettes.isEmpty()) {
            CigaretteHistoryDocument todayDoc = convertToCigaretteHistoryDocument(todayCigarettes, userId, today);
            weekDocs.add(todayDoc);
        }

        // 날짜별 데이터 맵 생성 (빠른 조회를 위해)
        Map<String, Integer> dateCountMap = weekDocs.stream()
                .collect(Collectors.toMap(
                        CigaretteHistoryDocument::getDate,
                        CigaretteHistoryDocument::getSmokeCount,
                        (existing, replacement) -> existing // 중복 시 기존 값 유지
                ));

        // 일요일부터 토요일까지 순회하며 Response 생성
        List<WeeklyCigaretteResponse.DailyCigaretteCount> weekData = new ArrayList<>();
        LocalDate currentDate = thisSunday;

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            String dateStr = currentDate.format(BASIC_ISO_DATE);
            String dayOfWeek = currentDate.getDayOfWeek().toString().substring(0, 3); // SUN, MON, TUE, ...
            int count = dateCountMap.getOrDefault(dateStr, 0);

            weekData.add(WeeklyCigaretteResponse.DailyCigaretteCount.createResponse(dateStr, dayOfWeek, count));
            currentDate = currentDate.plusDays(ONE_DAY);
        }

        return WeeklyCigaretteResponse.createResponse(weekData);
    }

    @Override
    public SmokingFeedbackResponse getSmokingFeedback() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        LocalDate today = koreaTime.today();
        LocalDate yesterday = today.minusDays(ONE_DAY);
        LocalDate dayBeforeYesterday = today.minusDays(ONE_DAY * 2);

        // 어제 데이터 조회 (MongoDB)
        CigaretteHistoryDocument yesterdayDoc = userCigaretteHistoryRepository.findByDateAndUserId(
                yesterday.format(BASIC_ISO_DATE), userId);
        int yesterdaySmokeCount = yesterdayDoc != null ? yesterdayDoc.getSmokeCount() : 0;

        // 그제 데이터 조회 (MongoDB)
        CigaretteHistoryDocument dayBeforeDoc = userCigaretteHistoryRepository.findByDateAndUserId(
                dayBeforeYesterday.format(BASIC_ISO_DATE), userId);
        int dayBeforeSmokeCount = dayBeforeDoc != null ? dayBeforeDoc.getSmokeCount() : 0;

        // 평소 흡연량 조회 (MongoDB 전체 평균)
        double usualSmokeCount = userCigaretteHistoryRepository.findAverageSmokeCountByUserId(userId);

        return SmokingFeedbackResponse.createResponse(
                SmokingFeedback.findFeedback(
                        calculateChangeRate(yesterdaySmokeCount, usualSmokeCount),    // 평소 대비 변화율
                        calculateChangeRate(yesterdaySmokeCount, dayBeforeSmokeCount) // 그제 대비 변화율
                )
        );
    }

    @Override
    public FirstSmokeDateResponse findFirstSmokeDate() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        CigaretteHistoryDocument doc = userCigaretteHistoryRepository.findEarliestByUserId(userId);
        if (doc == null) {
            return FirstSmokeDateResponse.builder().firstDate(null).build();
        }
        return FirstSmokeDateResponse.createResponse(doc);
    }

    @Override
    public StatsFeedbackResponse getStatsFeedback() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        double avgSmokeCount = userCigaretteHistoryRepository.findAverageSmokeCountByUserId(userId);
        double avgPatienceTime = userCigaretteHistoryRepository.findAverageAvgPatienceTimeByUserId(userId);
        return StatsFeedbackResponse.createResponse(StatsFeedback.findFeedback(avgSmokeCount, avgPatienceTime));
    }

    private double calculateChangeRate(int current, double previous) {
        if (previous == 0) {
            return current == 0 ? 0.0 : 100.0;
        }

        double rate = ((current - previous) / previous) * PERCENTAGE_MULTIPLIER;
        return Math.round(rate * DECIMAL_MULTIPLIER) / DECIMAL_MULTIPLIER;
    }

    private double calculateChangeRate(int current, int previous) {
        return calculateChangeRate(current, (double) previous);
    }
}
