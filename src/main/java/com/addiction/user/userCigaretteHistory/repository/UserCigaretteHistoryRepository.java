package com.addiction.user.userCigaretteHistory.repository;

import java.util.List;

import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;

public interface UserCigaretteHistoryRepository {
	void save(CigaretteHistoryDocument document);

	List<CigaretteHistoryDocument> findByMonthAndUserId(String month, int userId);

	CigaretteHistoryDocument findByDateAndUserId(String date, int userId);

	List<CigaretteHistoryDocument> findByUserIdAndDateBetween(int userId, String startDate, String endDate);
}
