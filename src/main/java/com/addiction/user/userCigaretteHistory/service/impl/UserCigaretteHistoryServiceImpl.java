package com.addiction.user.userCigaretteHistory.service.impl;

import com.addiction.global.security.SecurityService;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.enums.PeriodType;
import com.addiction.user.userCigaretteHistory.enums.SmokingFeedback;
import com.addiction.user.userCigaretteHistory.enums.StatsFeedback;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.userCigaretteHistory.service.response.*;
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

    // 퍼센트 계산 상수
    private static final double PERCENTAGE_MULTIPLIER = 100.0;

    private final SecurityService securityService;
    private final UserCigaretteReadService userCigaretteReadService;
    private final UserCigaretteHistoryRepository userCigaretteHistoryRepository;

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
        List<UserCigaretteHistoryCalenderResponse> results = userCigaretteHistoryRepository.findByMonthAndUserId(month, userId).stream()
                .map(doc -> UserCigaretteHistoryCalenderResponse.createResponse(doc.getDate(), doc.getSmokeCount()))
                .collect(Collectors.toList());

        // 당일 데이터 추가 (RDBMS에서 조회)
        String today = LocalDate.now().format(BASIC_ISO_DATE);
        String todayMonth = LocalDate.now().format(MONTH_FORMATTER);

        if (month.equals(todayMonth)) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().plusDays(ONE_DAY).atStartOfDay();

            List<UserCigarette> todayCigarettes = userCigaretteReadService.findAllByUserIdAndCreatedDateBetween(userId, startOfDay, endOfDay);

            if (!todayCigarettes.isEmpty()) {
                results.add(UserCigaretteHistoryCalenderResponse.createResponse(today, todayCigarettes.size()));
            }
        }

        return results;
    }

    @Override
    public List<UserCigaretteHistoryResponse> findHistoryByDate(String date) {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        String today = LocalDate.now().format(BASIC_ISO_DATE);

        // 당일 데이터인 경우 RDBMS에서 조회
        if (date.equals(today)) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().plusDays(ONE_DAY).atStartOfDay();

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
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        Map<String, CigaretteHistoryDocument> docMap = new HashMap<>();
        if (today.isAfter(monday)) {
            String start = monday.format(BASIC_ISO_DATE);
            String end = today.minusDays(ONE_DAY).format(BASIC_ISO_DATE);
            userCigaretteHistoryRepository.findByUserIdAndDateBetween(userId, start, end)
                    .forEach(d -> docMap.put(d.getDate(), d));
        }

        CigaretteHistoryDocument todayDoc = buildTodayDocument(userId, today);
        if (todayDoc != null) {
            docMap.put(today.format(BASIC_ISO_DATE), todayDoc);
        }

        List<UserCigaretteHistoryGraphDateResponse> countList = new ArrayList<>();
        List<UserCigaretteHistoryGraphDateResponse> patientList = new ArrayList<>();

        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            LocalDate day = monday.plusDays(i);
            String label = day.getDayOfWeek().toString().substring(0, 3);
            CigaretteHistoryDocument doc = docMap.get(day.format(BASIC_ISO_DATE));
            countList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, doc != null ? doc.getSmokeCount() : 0));
            patientList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, doc != null ? doc.getAvgPatienceTime() : 0));
        }

        return buildGraphResponse(countList, patientList);
    }

    private UserCigaretteHistoryGraphResponse buildMonthlyGraph(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate currentMonday = today.with(DayOfWeek.MONDAY);

        List<UserCigaretteHistoryGraphDateResponse> countList = new ArrayList<>();
        List<UserCigaretteHistoryGraphDateResponse> patientList = new ArrayList<>();

        for (int w = 4; w >= 0; w--) {
            LocalDate weekStart = currentMonday.minusWeeks(w);
            LocalDate weekEnd = weekStart.plusDays(DAYS_FROM_SUNDAY_TO_SATURDAY);
            String label = weekStart.format(DateTimeFormatter.ofPattern("MM/dd"));

            List<CigaretteHistoryDocument> docs = new ArrayList<>();

            if (weekStart.isBefore(today)) {
                LocalDate mongoEnd = weekEnd.isBefore(today) ? weekEnd : today.minusDays(ONE_DAY);
                docs.addAll(userCigaretteHistoryRepository.findByUserIdAndDateBetween(
                        userId, weekStart.format(BASIC_ISO_DATE), mongoEnd.format(BASIC_ISO_DATE)));
            }

            if (!today.isBefore(weekStart) && !today.isAfter(weekEnd)) {
                CigaretteHistoryDocument todayDoc = buildTodayDocument(userId, today);
                if (todayDoc != null) docs.add(todayDoc);
            }

            long totalCount = docs.stream().mapToLong(CigaretteHistoryDocument::getSmokeCount).sum();
            long avgPatience = (long) docs.stream().mapToLong(CigaretteHistoryDocument::getAvgPatienceTime).average().orElse(0);

            countList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, totalCount));
            patientList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, avgPatience));
        }

        return buildGraphResponse(countList, patientList);
    }

    private UserCigaretteHistoryGraphResponse buildMonthAggGraph(Long userId, int months) {
        LocalDate today = LocalDate.now();

        List<UserCigaretteHistoryGraphDateResponse> countList = new ArrayList<>();
        List<UserCigaretteHistoryGraphDateResponse> patientList = new ArrayList<>();

        for (int m = months - 1; m >= 0; m--) {
            LocalDate monthStart = today.withDayOfMonth(1).minusMonths(m);
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
            String label = monthStart.format(MONTH_FORMATTER);

            List<CigaretteHistoryDocument> docs = new ArrayList<>();

            LocalDate mongoEnd = monthEnd.isBefore(today) ? monthEnd : today.minusDays(ONE_DAY);
            if (!mongoEnd.isBefore(monthStart)) {
                docs.addAll(userCigaretteHistoryRepository.findByUserIdAndDateBetween(
                        userId, monthStart.format(BASIC_ISO_DATE), mongoEnd.format(BASIC_ISO_DATE)));
            }

            if (m == 0) {
                CigaretteHistoryDocument todayDoc = buildTodayDocument(userId, today);
                if (todayDoc != null) docs.add(todayDoc);
            }

            long totalCount = docs.stream().mapToLong(CigaretteHistoryDocument::getSmokeCount).sum();
            long avgPatience = (long) docs.stream().mapToLong(CigaretteHistoryDocument::getAvgPatienceTime).average().orElse(0);

            countList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, totalCount));
            patientList.add(UserCigaretteHistoryGraphDateResponse.createResponse(label, avgPatience));
        }

        return buildGraphResponse(countList, patientList);
    }

    private CigaretteHistoryDocument buildTodayDocument(Long userId, LocalDate today) {
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(ONE_DAY).atStartOfDay();
        List<UserCigarette> todayCigarettes = userCigaretteReadService.findAllByUserIdAndCreatedDateBetween(userId, startOfDay, endOfDay);
        return todayCigarettes.isEmpty() ? null : convertToCigaretteHistoryDocument(todayCigarettes, userId);
    }

    private UserCigaretteHistoryGraphResponse buildGraphResponse(
            List<UserCigaretteHistoryGraphDateResponse> countList,
            List<UserCigaretteHistoryGraphDateResponse> patientList) {
        int avgCount = countList.isEmpty() ? 0 : (int) Math.round(
                countList.stream().mapToLong(UserCigaretteHistoryGraphDateResponse::getValue).average().orElse(0));
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
        UserCigarette cigarette = userCigaretteReadService.findLatestByUserId(userId);
        if (cigarette == null) {
            CigaretteHistoryDocument doc = userCigaretteHistoryRepository.findLatestByUserId(userId);
            if (doc != null && !doc.getHistory().isEmpty()) {
                return UserCigaretteHistoryLastestResponse.createResponse(
                        doc.getHistory().get(doc.getHistory().size() - 1).getSmokeTime(),
                        doc.getHistory().get(doc.getHistory().size() - 1).getAddress()
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
        LocalDate today = LocalDate.now();

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
        LocalDate today = LocalDate.now();

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
            CigaretteHistoryDocument todayDoc = convertToCigaretteHistoryDocument(todayCigarettes, userId);
            thisWeekDocs.add(todayDoc);
        }

        return thisWeekDocs;
    }

    /**
     * UserCigarette 리스트를 CigaretteHistoryDocument로 변환
     *
     * @param cigarettes 담배 흡연 기록 리스트
     * @param userId     사용자 ID
     * @return 변환된 CigaretteHistoryDocument
     */
    private CigaretteHistoryDocument convertToCigaretteHistoryDocument(List<UserCigarette> cigarettes, Long userId) {
        LocalDate today = LocalDate.now();

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
        LocalDate today = LocalDate.now();

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
            CigaretteHistoryDocument todayDoc = convertToCigaretteHistoryDocument(todayCigarettes, userId);
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
        LocalDate today = LocalDate.now();
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