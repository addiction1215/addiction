package com.addiction.user.userCigaretteHistory.service.impl;

import com.addiction.global.security.SecurityService;
import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.enums.ComparisonType;
import com.addiction.user.userCigaretteHistory.enums.PeriodType;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCigaretteHistoryServiceImpl implements UserCigaretteHistoryService {

    private static final DateTimeFormatter BASIC_ISO_DATE = DateTimeFormatter.BASIC_ISO_DATE;
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    private final SecurityService securityService;
    private final UserCigaretteReadService userCigaretteReadService;
    private final UserCigaretteHistoryRepository userCigaretteHistoryRepository;

    @Override
    public void save(String monthStr, String dateStr, Long userId, Integer smokeCount, Long avgPatienceTime,
                     List<CigaretteHistoryDocument.History> historyList) {
        CigaretteHistoryDocument doc = CigaretteHistoryDocument.builder()
                .month(monthStr)
                .date(dateStr)
                .userId(userId)
                .smokeCount(smokeCount)
                .avgPatienceTime(avgPatienceTime)
                .history(historyList)
                .build();

        userCigaretteHistoryRepository.save(doc, dateStr);
    }

    @Override
    public List<UserCigaretteHistoryCalenderResponse> findCalendarByDate(String month) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();
        List<UserCigaretteHistoryCalenderResponse> results = userCigaretteHistoryRepository.findByMonthAndUserId(month, userId).stream()
                .map(doc -> UserCigaretteHistoryCalenderResponse.createResponse(doc.getDate(), doc.getSmokeCount()))
                .collect(Collectors.toList());

        // 당일 데이터 추가 (RDBMS에서 조회)
        String today = LocalDate.now().format(BASIC_ISO_DATE);
        String todayMonth = LocalDate.now().format(MONTH_FORMATTER);

        if (month.equals(todayMonth)) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

            List<UserCigarette> todayCigarettes = userCigaretteReadService.findAllByUserIdAndCreatedDateBetween(userId, startOfDay, endOfDay);

            if (!todayCigarettes.isEmpty()) {
                results.add(UserCigaretteHistoryCalenderResponse.createResponse(today, todayCigarettes.size()));
            }
        }

        return results;
    }

    @Override
    public List<UserCigaretteHistoryResponse> findHistoryByDate(String date) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();
        String today = LocalDate.now().format(BASIC_ISO_DATE);

        // 당일 데이터인 경우 RDBMS에서 조회
        if (date.equals(today)) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

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
        long userId = securityService.getCurrentLoginUserInfo().getUserId();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = periodType.calculateStartDate(endDate);

        String start = startDate.format(BASIC_ISO_DATE);
        String end = endDate.format(BASIC_ISO_DATE);

        // 가변 리스트로 변환 (불변 리스트일 수 있으므로)
        List<CigaretteHistoryDocument> docs = new ArrayList<>(
                userCigaretteHistoryRepository.findByUserIdAndDateBetween(userId, start, end)
        );

        // 당일 데이터 추가 (RDBMS에서 조회)
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        List<UserCigarette> todayCigarettes = userCigaretteReadService.findAllByUserIdAndCreatedDateBetween(userId, startOfDay, endOfDay);

        if (!todayCigarettes.isEmpty()) {
            CigaretteHistoryDocument todayDoc = convertToCigaretteHistoryDocument(todayCigarettes, userId);
            docs.add(todayDoc);
        }

        return UserCigaretteHistoryGraphResponse.createResponse(
                createGraphCountResponse(docs),
                createGraphPatientResponse(docs)
        );
    }

    @Override
    public UserCigaretteHistoryLastestResponse findLastestByUserId() {
        Long userId = securityService.getCurrentLoginUserInfo().getUserId();
        UserCigarette cigarette = userCigaretteReadService.findLatestByUserId(userId);
        if (cigarette == null) {
            CigaretteHistoryDocument doc = userCigaretteHistoryRepository.findLatestByUserId(userId);
            if (doc != null) {
                return UserCigaretteHistoryLastestResponse.createResponse(
                        doc.getHistory().get(doc.getHistory().size() - 1).getSmokeTime(),
                        doc.getHistory().get(doc.getHistory().size() - 1).getAddress()
                );
            }
        }

        assert cigarette != null;
        return UserCigaretteHistoryLastestResponse.createResponse(cigarette.getSmokeTime(), cigarette.getAddress());
    }

    @Override
    public WeeklyComparisonResponse compareWeekly(ComparisonType comparisonType) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();
        
        // 지난주 데이터 조회 (MongoDB 전체)
        List<CigaretteHistoryDocument> lastWeekDocs = getLastWeekDocuments(userId);
        
        // 이번주 데이터 조회 (MongoDB + 당일 RDBMS)
        List<CigaretteHistoryDocument> thisWeekDocs = getThisWeekDocuments(userId);
        
        // 비교 타입에 따라 계산
        if (comparisonType == ComparisonType.COUNT) {
            return calculateCountComparison(lastWeekDocs, thisWeekDocs);
        } else {
            return calculateTimeComparison(lastWeekDocs, thisWeekDocs);
        }
    }

    /**
     * 지난주 데이터 조회 (MongoDB)
     * 지난주 월요일 ~ 일요일의 데이터를 MongoDB에서 조회
     */
    private List<CigaretteHistoryDocument> getLastWeekDocuments(long userId) {
        LocalDate today = LocalDate.now();
        
        // 이번주 월요일 계산
        LocalDate thisWeekMonday = today.with(DayOfWeek.MONDAY);
        
        // 지난주 월요일, 일요일 계산
        LocalDate lastWeekMonday = thisWeekMonday.minusWeeks(1);
        LocalDate lastWeekSunday = lastWeekMonday.plusDays(6);
        
        String startDate = lastWeekMonday.format(BASIC_ISO_DATE);
        String endDate = lastWeekSunday.format(BASIC_ISO_DATE);
        
        return userCigaretteHistoryRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    /**
     * 이번주 데이터 조회 (MongoDB + RDBMS)
     * 이번주 월요일 ~ 어제: MongoDB
     * 오늘: RDBMS
     */
    private List<CigaretteHistoryDocument> getThisWeekDocuments(long userId) {
        LocalDate today = LocalDate.now();
        
        // 이번주 월요일 계산
        LocalDate thisWeekMonday = today.with(DayOfWeek.MONDAY);
        
        List<CigaretteHistoryDocument> thisWeekDocs = new ArrayList<>();
        
        // MongoDB에서 이번주 월요일 ~ 어제까지 조회
        if (today.isAfter(thisWeekMonday)) {
            String startDate = thisWeekMonday.format(BASIC_ISO_DATE);
            String endDate = today.minusDays(1).format(BASIC_ISO_DATE);
            
            thisWeekDocs.addAll(userCigaretteHistoryRepository.findByUserIdAndDateBetween(userId, startDate, endDate));
        }
        
        // 오늘 데이터 추가 (RDBMS에서 조회)
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
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
     * @param userId 사용자 ID
     * @return 변환된 CigaretteHistoryDocument
     */
    private CigaretteHistoryDocument convertToCigaretteHistoryDocument(List<UserCigarette> cigarettes, long userId) {
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
        long avgPatienceTime = (long) cigarettes.stream()
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

    /**
     * 흡연 횟수 비교 계산
     */
    private WeeklyComparisonResponse calculateCountComparison(
            List<CigaretteHistoryDocument> lastWeekDocs,
            List<CigaretteHistoryDocument> thisWeekDocs) {
        
        // 지난주 총 횟수
        int lastWeekCount = lastWeekDocs.stream()
                .mapToInt(CigaretteHistoryDocument::getSmokeCount)
                .sum();
        
        // 이번주 총 횟수
        int thisWeekCount = thisWeekDocs.stream()
                .mapToInt(CigaretteHistoryDocument::getSmokeCount)
                .sum();
        
        // 증감 값
        double difference = thisWeekCount - lastWeekCount;
        
        // 증감률 계산 (지난주가 0이면 100% 또는 -100%로 표시)
        double changeRate;
        if (lastWeekCount == 0) {
            changeRate = thisWeekCount > 0 ? 100.0 : 0.0;
        } else {
            changeRate = (difference / lastWeekCount) * 100.0;
        }
        
        return WeeklyComparisonResponse.builder()
                .comparisonType(ComparisonType.COUNT)
                .lastWeekCount(lastWeekCount)
                .thisWeekCount(thisWeekCount)
                .difference(difference)
                .changeRate(Math.round(changeRate * 10.0) / 10.0) // 소수점 1자리
                .build();
    }

    /**
     * 금연 시간 비교 계산
     */
    private WeeklyComparisonResponse calculateTimeComparison(
            List<CigaretteHistoryDocument> lastWeekDocs,
            List<CigaretteHistoryDocument> thisWeekDocs) {
        
        // 지난주 평균 금연 시간 (초 -> 시간)
        double lastWeekAvgTime = lastWeekDocs.stream()
                .mapToLong(CigaretteHistoryDocument::getAvgPatienceTime)
                .average()
                .orElse(0.0) / 3600.0; // 초를 시간으로 변환
        
        // 이번주 평균 금연 시간 (초 -> 시간)
        double thisWeekAvgTime = thisWeekDocs.stream()
                .mapToLong(CigaretteHistoryDocument::getAvgPatienceTime)
                .average()
                .orElse(0.0) / 3600.0; // 초를 시간으로 변환
        
        // 증감 값
        double difference = thisWeekAvgTime - lastWeekAvgTime;
        
        // 증감률 계산
        double changeRate = 0.0;
        if (lastWeekAvgTime == 0) {
            changeRate = thisWeekAvgTime > 0 ? 100.0 : 0.0;
        } else {
            changeRate = (difference / lastWeekAvgTime) * 100.0;
        }
        
        return WeeklyComparisonResponse.builder()
                .comparisonType(ComparisonType.TIME)
                .lastWeekAvgTime(Math.round(lastWeekAvgTime * 10.0) / 10.0) // 소수점 1자리
                .thisWeekAvgTime(Math.round(thisWeekAvgTime * 10.0) / 10.0) // 소수점 1자리
                .difference(Math.round(difference * 10.0) / 10.0) // 소수점 1자리
                .changeRate(Math.round(changeRate * 10.0) / 10.0) // 소수점 1자리
                .build();
    }

    private UserCigaretteHistoryGraphCountResponse createGraphCountResponse(List<CigaretteHistoryDocument> docs) {
        List<UserCigaretteHistoryGraphDateResponse> dateList = docs.stream()
                .map(doc -> UserCigaretteHistoryGraphDateResponse.createResponse(doc.getDate(), doc.getSmokeCount()))
                .toList();

        int avgCigaretteCount = dateList.isEmpty() ? 0 :
                (int) Math.round(
                        dateList.stream().mapToLong(UserCigaretteHistoryGraphDateResponse::getValue).average().orElse(0));

        return UserCigaretteHistoryGraphCountResponse.createResponse(
                avgCigaretteCount,
                dateList
        );
    }

    private UserCigaretteHistoryGraphPatientResponse createGraphPatientResponse(List<CigaretteHistoryDocument> docs) {
        List<UserCigaretteHistoryGraphDateResponse> dateList = docs.stream()
                .map(doc -> UserCigaretteHistoryGraphDateResponse.createResponse(doc.getDate(), doc.getAvgPatienceTime()))
                .collect(Collectors.toList());

        long avgSmokePatientTime = dateList.isEmpty() ? 0 :
                Math.round(dateList.stream().mapToLong(UserCigaretteHistoryGraphDateResponse::getValue).average().orElse(0));

        return UserCigaretteHistoryGraphPatientResponse.createResponse(
                avgSmokePatientTime,
                dateList
        );
    }
}