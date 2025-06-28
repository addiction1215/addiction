package com.addiction.user.userCigarette.entity;

import java.time.LocalDateTime;

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

	private LocalDateTime smokeTime;

	private String address;

	@Builder
	public UserCigarette(int id, User user, LocalDateTime smokeTime, String address) {
		this.id = id;
		this.user = user;
		this.smokeTime = smokeTime;
		this.address = address;
	}

	public static UserCigarette createEntity(User user, String address) {
		return UserCigarette.builder()
				.user(user)
				.smokeTime(LocalDateTime.now())
				.address(address)
				.build();
	}
	
}
