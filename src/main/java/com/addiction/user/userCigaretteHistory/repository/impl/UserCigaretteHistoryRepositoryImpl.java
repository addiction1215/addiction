package com.addiction.user.userCigaretteHistory.repository.impl;

import com.addiction.global.config.MongoConfig;
import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class UserCigaretteHistoryRepositoryImpl implements UserCigaretteHistoryRepository {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;

    private final MongoTemplate mongoTemplate;

    @Override
    public void save(CigaretteHistoryDocument document) {
        mongoTemplate.insert(document, MongoConfig.COLLECTION_NAME);
    }

    @Override
    public List<CigaretteHistoryDocument> findByMonthAndUserId(String month, Long userId) {
        // month: yyyyMM → smokeDate 범위로 변환 (timeField 인덱스 활용)
        LocalDate firstDay = LocalDate.parse(month + "01", DATE_FORMAT);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        return mongoTemplate.find(
                query(where("userId").is(userId)
                        .and("smokeDate").gte(firstDay.atStartOfDay()).lt(lastDay.plusDays(1).atStartOfDay()))
                        .with(Sort.by(Sort.Direction.ASC, "smokeDate")),
                CigaretteHistoryDocument.class,
                MongoConfig.COLLECTION_NAME
        );
    }

    @Override
    public CigaretteHistoryDocument findByDateAndUserId(String date, Long userId) {
        // date: yyyyMMdd → smokeDate 범위로 변환 (timeField 인덱스 활용)
        LocalDateTime startOfDay = LocalDate.parse(date, DATE_FORMAT).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return mongoTemplate.findOne(
                query(where("userId").is(userId)
                        .and("smokeDate").gte(startOfDay).lt(endOfDay)),
                CigaretteHistoryDocument.class,
                MongoConfig.COLLECTION_NAME
        );
    }

    @Override
    public List<CigaretteHistoryDocument> findByUserIdAndDateBetween(Long userId, String startDate, String endDate) {
        // startDate/endDate: yyyyMMdd → smokeDate 범위로 변환 (timeField 인덱스 활용)
        LocalDateTime start = LocalDate.parse(startDate, DATE_FORMAT).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate, DATE_FORMAT).plusDays(1).atStartOfDay();

        return mongoTemplate.find(
                query(where("userId").is(userId)
                        .and("smokeDate").gte(start).lt(end))
                        .with(Sort.by(Sort.Direction.ASC, "smokeDate")),
                CigaretteHistoryDocument.class,
                MongoConfig.COLLECTION_NAME
        );
    }

    @Override
    public CigaretteHistoryDocument findLatestByUserId(Long userId) {
        return mongoTemplate.findOne(
                query(where("userId").is(userId))
                        .with(Sort.by(Sort.Direction.DESC, "smokeDate"))
                        .limit(1),
                CigaretteHistoryDocument.class,
                MongoConfig.COLLECTION_NAME
        );
    }

    @Override
    public double findAverageSmokeCountByUserId(Long userId) {
        return aggregateSingleAvg(userId, "smokeCount");
    }

    @Override
    public double findAverageAvgPatienceTimeByUserId(Long userId) {
        return aggregateSingleAvg(userId, "avgPatienceTime");
    }

    private double aggregateSingleAvg(Long userId, String field) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("userId").is(userId)),
                Aggregation.group().avg(field).as("avgValue")
        );
        AggregationResults<Document> results = mongoTemplate.aggregate(
                aggregation, MongoConfig.COLLECTION_NAME, Document.class);
        Document result = results.getUniqueMappedResult();
        if (result == null) return 0.0;
        Object avgValue = result.get("avgValue");
        return avgValue instanceof Number ? ((Number) avgValue).doubleValue() : 0.0;
    }
}
