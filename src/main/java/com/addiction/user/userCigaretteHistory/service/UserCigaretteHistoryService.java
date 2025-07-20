package com.addiction.user.userCigaretteHistory.service;

import java.util.List;

import com.addiction.user.userCigaretteHistory.document.CigaretteHistoryDocument;

public interface UserCigaretteHistoryService {

	void save(String dateStr, int userId, int smokeCount, long avgPatienceTime, List<CigaretteHistoryDocument.History> historyList);

}
