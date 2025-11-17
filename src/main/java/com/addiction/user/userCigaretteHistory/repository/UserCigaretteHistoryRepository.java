package com.addiction.user.userCigaretteHistory.repository;

import java.util.List;

import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;

public interface UserCigaretteHistoryRepository {
	void save(CigaretteHistoryDocument document, String date);

	List<CigaretteHistoryDocument> findByMonthAndUserId(String month, Long userId);

	CigaretteHistoryDocument findByDateAndUserId(String date, Long userId);

	List<CigaretteHistoryDocument> findByUserIdAndDateBetween(Long userId, String startDate, String endDate);

    CigaretteHistoryDocument findLatestByUserId(Long userId);
}
