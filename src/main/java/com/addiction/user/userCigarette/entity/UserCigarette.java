package com.addiction.user.userCigarette.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.user.users.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCigarette extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	private int count;

	@Builder
	public UserCigarette(User user, int count) {
		this.user = user;
		this.count = count;
	}

	public void addCount() {
		this.count++;
	}

	public void minusCount() {
		if (this.count > 0) {
			this.count--;
		}
	}

	public void clearCount() {
		this.count = 0;
	}

	public static UserCigarette createEntity(User user) {
		return UserCigarette.builder()
				.user(user)
				.count(0)
				.build();
	}
	
}
