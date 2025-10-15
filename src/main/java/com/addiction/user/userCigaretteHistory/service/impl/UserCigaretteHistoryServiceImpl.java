package com.addiction.user.userCigaretteHistory.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.addiction.user.userCigarette.entity.UserCigarette;
import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigaretteHistory.service.response.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.global.security.SecurityService;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.enums.PeriodType;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCigaretteHistoryServiceImpl implements UserCigaretteHistoryService {

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
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String todayMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

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
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

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

    public UserCigaretteHistoryGraphResponse findGraphByPeriod(PeriodType periodType) {
        long userId = securityService.getCurrentLoginUserInfo().getUserId();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = periodType.calculateStartDate(endDate);

        String start = startDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        String end = endDate.format(DateTimeFormatter.BASIC_ISO_DATE);

        List<CigaretteHistoryDocument> docs = userCigaretteHistoryRepository.findByUserIdAndDateBetween(userId, start, end);

        // 당일 데이터 추가 (RDBMS에서 조회)
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();

        List<UserCigarette> todayCigarettes = userCigaretteReadService.findAllByUserIdAndCreatedDateBetween(userId, startOfDay, endOfDay);

        if (!todayCigarettes.isEmpty()) {
            // 당일 데이터를 Document 형태로 변환하여 추가
            List<CigaretteHistoryDocument.History> todayHistory = todayCigarettes.stream()
                    .map(c -> CigaretteHistoryDocument.History.builder()
                            .address(c.getAddress())
                            .smokeTime(c.getSmokeTime())
                            .smokePatienceTime(c.getSmokePatienceTime())
                            .build())
                    .collect(Collectors.toList());

            long avgPatienceTime = (long) todayCigarettes.stream()
                    .mapToLong(UserCigarette::getSmokePatienceTime)
                    .average()
                    .orElse(0);

            CigaretteHistoryDocument todayDoc = CigaretteHistoryDocument.builder()
                    .date(today)
                    .userId(userId)
                    .smokeCount(todayCigarettes.size())
                    .avgPatienceTime(avgPatienceTime)
                    .history(todayHistory)
                    .build();

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
