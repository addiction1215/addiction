package com.addiction.user.userCigaretteHistory.repository;

import java.util.List;
import java.time.LocalDate;
import java.util.Set;

import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;

public interface UserCigaretteHistoryRepository {
	void save(CigaretteHistoryDocument document);

	List<CigaretteHistoryDocument> findByMonthAndUserId(String month, Long userId);

	CigaretteHistoryDocument findByDateAndUserId(String date, Long userId);

	List<CigaretteHistoryDocument> findByUserIdAndDateBetween(Long userId, String startDate, String endDate);

	Set<LocalDate> findSmokedDatesByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    CigaretteHistoryDocument findLatestByUserId(Long userId);

    CigaretteHistoryDocument findEarliestByUserId(Long userId);

    double findAverageSmokeCountByUserId(Long userId);

    double findAverageAvgPatienceTimeByUserId(Long userId);

    Long findMaxSmokePatienceTimeByUserId(Long userId);
}
