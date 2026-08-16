package com.addiction.user.refreshToken.entity;

import com.addiction.global.BaseTimeEntity;
import com.addiction.user.users.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "refresh_token", uniqueConstraints = {
	@UniqueConstraint(name = "uk_refresh_token_user_device", columnNames = {"user_id", "device_id"})
})
public class RefreshToken extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String deviceId;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	private String refreshToken;

	@Builder
	private RefreshToken(User user, String refreshToken, String deviceId) {
		this.user = user;
		this.refreshToken = refreshToken;
		this.deviceId = deviceId;
	}

	public static RefreshToken of(User user, String refreshToken, String deviceId) {
		return RefreshToken.builder()
			.user(user)
			.refreshToken(refreshToken)
			.deviceId(deviceId)
			.build();
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public boolean belongsTo(Long userId) {
		return user.getId().equals(userId);
	}

	public void setUser(User user) {
		this.user = user;
		user.getRefreshTokens().add(this);
	}
}
