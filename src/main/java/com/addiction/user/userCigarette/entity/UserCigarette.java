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
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	private LocalDateTime smokeTime;

	private Long smokePatienceTime;

	private String address;

	@Builder
	public UserCigarette(Long id, User user, LocalDateTime smokeTime, String address, Long smokePatienceTime) {
		this.id = id;
		this.user = user;
		this.smokeTime = smokeTime;
		this.address = address;
		this.smokePatienceTime = smokePatienceTime;
	}

	public static UserCigarette createEntity(User user, String address, Long smokePatienceTime) {
		return UserCigarette.builder()
				.user(user)
				.smokeTime(LocalDateTime.now())
				.address(address)
				.smokePatienceTime(smokePatienceTime)
				.build();
	}
	
}
