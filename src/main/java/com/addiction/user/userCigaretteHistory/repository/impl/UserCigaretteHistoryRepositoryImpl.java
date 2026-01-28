package com.addiction.user.userCigaretteHistory.repository.impl;

import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class UserCigaretteHistoryRepositoryImpl implements UserCigaretteHistoryRepository {

    private final MongoTemplate mongoTemplate;

    /**
     * 날짜 범위에 해당하는 모든 컬렉션 이름 목록 생성
     */
    private List<String> getCollectionNames(String startDate, String endDate) {
        List<String> collections = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.BASIC_ISO_DATE);

        while (!start.isAfter(end)) {
            collections.add(start.format(DateTimeFormatter.BASIC_ISO_DATE));
            start = start.plusDays(1);
        }

        return collections;
    }

    @Override
    public void save(CigaretteHistoryDocument document, String date) {
        mongoTemplate.save(document, date);
    }

    @Override
    public List<CigaretteHistoryDocument> findByMonthAndUserId(String month, Long userId) {
        // 월 단위 조회: 해당 월의 모든 날짜 컬렉션을 병렬로 조회
        // month 형식: yyyyMM (예: 202406)
        LocalDate firstDayOfMonth = LocalDate.parse(month + "01", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

        String startDate = firstDayOfMonth.format(DateTimeFormatter.BASIC_ISO_DATE);
        String endDate = lastDayOfMonth.format(DateTimeFormatter.BASIC_ISO_DATE);

        List<String> collections = getCollectionNames(startDate, endDate);

        // 병렬 스트림으로 각 컬렉션 조회 및 결과 수집
        ConcurrentLinkedQueue<CigaretteHistoryDocument> concurrentResults = new ConcurrentLinkedQueue<>();

        collections.parallelStream()
                .filter(mongoTemplate::collectionExists)
                .forEach(collectionName -> {
                    List<CigaretteHistoryDocument> docs = mongoTemplate.find(
                            query(where("userId").is(userId)),
                            CigaretteHistoryDocument.class,
                            collectionName
                    );
                    concurrentResults.addAll(docs);
                });

        // 날짜순으로 정렬하여 반환
        return concurrentResults.stream()
                .sorted(Comparator.comparing(CigaretteHistoryDocument::getDate))
                .collect(Collectors.toList());
    }

    @Override
    public CigaretteHistoryDocument findByDateAndUserId(String date, Long userId) {
        if (!mongoTemplate.collectionExists(date)) {
            return null;
        }

        CigaretteHistoryDocument cigaretteHistoryDocument =  mongoTemplate.findOne(
                query(where("userId").is(userId)),
                CigaretteHistoryDocument.class,
                date
        );

        if (cigaretteHistoryDocument == null) {
            cigaretteHistoryDocument = createEmptyDocument(LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE), userId);
        }

        return cigaretteHistoryDocument;
    }

    @Override
    public List<CigaretteHistoryDocument> findByUserIdAndDateBetween(Long userId, String startDate, String endDate) {
        List<String> collections = getCollectionNames(startDate, endDate);

        // 병렬 스트림으로 각 컬렉션 조회 및 결과 수집
        ConcurrentLinkedQueue<CigaretteHistoryDocument> concurrentResults = new ConcurrentLinkedQueue<>();

        collections.parallelStream()
                .filter(mongoTemplate::collectionExists)
                .forEach(collectionName -> {
                    List<CigaretteHistoryDocument> docs = mongoTemplate.find(
                            query(where("userId").is(userId)),
                            CigaretteHistoryDocument.class,
                            collectionName
                    );
                    concurrentResults.addAll(docs);
                });

        // 날짜순으로 정렬하여 반환
        return concurrentResults.stream()
                .sorted(Comparator.comparing(CigaretteHistoryDocument::getDate))
                .collect(Collectors.toList());
    }

    @Override
    public CigaretteHistoryDocument findLatestByUserId(Long userId) {
        // 최근 30일간의 컬렉션을 역순으로 조회
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 30; i++) {
            LocalDate date = today.minusDays(i);
            String collectionName = date.format(DateTimeFormatter.BASIC_ISO_DATE);

            if (mongoTemplate.collectionExists(collectionName)) {
                CigaretteHistoryDocument doc = mongoTemplate.findOne(
                        query(where("userId").is(userId))
                                .with(Sort.by(Sort.Direction.DESC, "date")),
                        CigaretteHistoryDocument.class,
                        collectionName
                );

                if (doc != null) {
                    return doc;
                }
            }
        }

        return null;
    }

    /**
     * 빈 문서 생성 (데이터가 없는 경우)
     */
    private CigaretteHistoryDocument createEmptyDocument(LocalDate date, Long userId) {
        return CigaretteHistoryDocument.builder()
                .date(date.format(DateTimeFormatter.BASIC_ISO_DATE))
                .userId(userId)
                .smokeCount(0)
                .avgPatienceTime(0L)
                .history(List.of())
                .build();
    }
}
