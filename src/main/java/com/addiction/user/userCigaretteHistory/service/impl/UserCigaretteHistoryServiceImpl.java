package com.addiction.user.userCigaretteHistory.service.impl;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCigaretteHistoryServiceImpl implements UserCigaretteHistoryService {

	private final MongoTemplate mongoTemplate;

	@Override
	public void save(String dateStr, int userId, int smokeCount, long avgPatienceTime,
		List<CigaretteHistoryDocument.History> historyList) {
		CigaretteHistoryDocument doc = CigaretteHistoryDocument.builder()
			.date(dateStr)
			.userId(userId)
			.smokeCount(smokeCount)
			.avgPatienceTime(avgPatienceTime)
			.history(historyList)
			.build();

		mongoTemplate.save(doc);
	}
}
