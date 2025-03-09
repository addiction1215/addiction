package com.addiction.users.entity;

import java.util.ArrayList;
import java.util.List;

import com.addiction.global.BaseTimeEntity;
import com.addiction.jwt.dto.JwtToken;
import com.addiction.push.entity.Push;
import com.addiction.refreshToken.entity.RefreshToken;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String email;

	private String password;

	private String nickname;

	@Enumerated(EnumType.STRING)
	private SnsType snsType;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private SettingStatus settingStatus;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<RefreshToken> refreshTokens = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Push> pushes = new ArrayList<>();

	@Builder
	private User(String email, String password, SnsType snsType, String nickname, Role role,
		SettingStatus settingStatus) {
		this.email = email;
		this.password = password;
		this.snsType = snsType;
		this.nickname = nickname;
		this.role = role;
		this.settingStatus = settingStatus;
	}

	public void checkSnsType(SnsType snsType) {
		if (!this.snsType.equals(snsType)) {
			this.snsType.checkSnsType();
		}
	}

	public void checkRefreshToken(JwtToken jwtToken, String deviceId) {
		refreshTokens.stream()
			.filter(refreshToken -> deviceId.equals(refreshToken.getDeviceId()))
			.findFirst()
			.ifPresentOrElse(
				existToken -> existToken.updateRefreshToken(jwtToken.getRefreshToken()),
				() -> {
					RefreshToken refreshToken = RefreshToken.of(this, jwtToken.getRefreshToken(), deviceId);
					refreshToken.setUser(this);
				}
			);
	}

	public void checkPushKey(String pushToken, String deviceId) {
		pushes.stream()
			.filter(push -> deviceId.equals(push.getDeviceId()))
			.findFirst()
			.ifPresentOrElse(
				existPush -> existPush.updatePushToken(pushToken),
				() -> {
					Push push = Push.of(deviceId, this, pushToken);
					push.updateUser(this);
				}
			);
	}

}
