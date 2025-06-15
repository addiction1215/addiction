package com.addiction.user.userCigarette.service.request;

import com.addiction.user.userCigarette.entity.UserCigarette;

public enum ChangeType {
	ADD,
	MINUS;

	public void changeCount(UserCigarette userCigarette) {
		if (this == ADD) {
			userCigarette.addCount();
			return;
		}
		userCigarette.minusCount();
	}
}
