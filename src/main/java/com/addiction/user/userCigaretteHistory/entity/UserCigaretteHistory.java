package com.addiction.user.userCigaretteHistory.entity;

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
public class UserCigaretteHistory extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	public int count;

	public LocalDateTime date;

	@Builder
	public UserCigaretteHistory(Long id, User user, int count, LocalDateTime date) {
		this.id = id;
		this.user = user;
		this.count = count;
		this.date = date;
	}

	public static UserCigaretteHistory createEntity(User user, int count, LocalDateTime date) {
		return UserCigaretteHistory.builder()
			.user(user)
			.count(count)
			.date(date)
			.build();
	}
}
