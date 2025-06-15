package com.addiction.user.userCigaretteHistory.repository;

import java.util.Optional;

import com.addiction.user.userCigaretteHistory.entity.UserCigaretteHistory;
import com.addiction.user.users.entity.User;

public interface UserCigaretteHistoryRepository {
	UserCigaretteHistory save(UserCigaretteHistory cigaretteHistory);

	Optional<UserCigaretteHistory> findById(Long id);

	void deleteAllInBatch();
}
