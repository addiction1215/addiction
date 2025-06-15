package com.addiction.user.userCigarette.repository;

import java.util.List;
import java.util.Optional;

import com.addiction.user.userCigarette.entity.UserCigarette;

public interface UserCigaretteRepository {

	UserCigarette save(UserCigarette userCigarette);

	Optional<UserCigarette> findById(int id);

	Optional<UserCigarette> findByUserId(int userId);

	void deleteAllInBatch();

	List<UserCigarette> findAll();

}
