package com.addiction.batch.userCigaretteHistory;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.addiction.user.userCigarette.service.UserCigaretteReadService;
import com.addiction.user.userCigaretteHistory.service.UserCigaretteHistoryService;
import com.addiction.user.userCigaretteHistory.service.request.UserCigaretteHistoryServiceRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class userCigaretteHistoryBatch {

	private final UserCigaretteReadService userCigaretteReadService;
	private final UserCigaretteHistoryService userCigaretteHistoryService;

	@Scheduled(cron = "0 0 0 * * *")
	public void userCigaretteHistory() {
		userCigaretteReadService.findAll().forEach(userCigarette -> {

		});
	}
}
