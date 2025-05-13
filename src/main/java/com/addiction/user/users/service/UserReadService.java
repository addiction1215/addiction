package com.addiction.user.users.service;

import com.addiction.user.users.entity.User;
import com.addiction.user.users.service.response.UserStartDateResponse;

public interface UserReadService {

	User save(User user);

	User findByEmail(String email);

	User findById(int id);

	void deleteAllInBatch();

	UserStartDateResponse findStartDate();

}
