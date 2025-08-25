package com.addiction.user.userCigaretteHistory.repository.impl;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.repository.UserCigaretteHistoryRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCigaretteHistoryRepositoryImpl implements UserCigaretteHistoryRepository {

	private final MongoTemplate mongoTemplate;

	@Override
	public void save(CigaretteHistoryDocument document) {
		mongoTemplate.save(document);
	}

	@Override
	public List<CigaretteHistoryDocument> findByMonthAndUserId(String month, Long userId) {
		return mongoTemplate.find(
			query(
				where("month").is(month)
					.and("userId").is(userId)
			),
			CigaretteHistoryDocument.class
		);
	}

	@Override
	public CigaretteHistoryDocument findByDateAndUserId(String date, Long userId) {
		return mongoTemplate.findOne(
			query(
				where("date").is(date)
					.and("userId").is(userId)
			),
			CigaretteHistoryDocument.class
		);
	}

	@Override
	public List<CigaretteHistoryDocument> findByUserIdAndDateBetween(Long userId, String startDate, String endDate) {
		return mongoTemplate.find(
			query(
				where("userId").is(userId)
					.and("date").gte(startDate).lte(endDate)
			),
			CigaretteHistoryDocument.class
		);
	}
}
