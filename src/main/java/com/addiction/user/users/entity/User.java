package com.addiction.user.users.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.addiction.global.BaseTimeEntity;
import com.addiction.jwt.dto.JwtToken;
import com.addiction.user.push.entity.Push;
import com.addiction.user.refreshToken.entity.RefreshToken;
import com.addiction.user.users.entity.enums.Role;
import com.addiction.user.users.entity.enums.SettingStatus;
import com.addiction.user.users.entity.enums.Sex;
import com.addiction.user.users.entity.enums.SnsType;

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

	private String nickName;

	private String phoneNumber;

	private String birthDay;

	private String purpose;

	private int totalScore;

	private int cigarettePrice;

	private LocalDateTime startDate;

	@Enumerated(EnumType.STRING)
	private SnsType snsType;

	@Enumerated(EnumType.STRING)
	private Sex sex;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Enumerated(EnumType.STRING)
	private SettingStatus settingStatus;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<RefreshToken> refreshTokens = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Push> pushes = new ArrayList<>();

	@Builder
	public User(String email, String password, String nickName, String phoneNumber, String birthDay, String purpose,
		int totalScore, int cigarettePrice, SnsType snsType, Sex sex, Role role,
		SettingStatus settingStatus) {
		this.email = email;
		this.password = password;
		this.nickName = nickName;
		this.phoneNumber = phoneNumber;
		this.birthDay = birthDay;
		this.purpose = purpose;
		this.totalScore = totalScore;
		this.cigarettePrice = cigarettePrice;
		this.snsType = snsType;
		this.sex = sex;
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

	public void update(Sex sex, String birthDay) {
		updateSex(sex);
		updateBirthDay(birthDay);
	}

	public void updateSurvey(String purpose, int totalScore, int cigarettePrice, LocalDateTime startDate) {
		updatePurpose(purpose);
		updateTotalScore(totalScore);
		updateCigarettePrice(cigarettePrice);
		completeSetting();
		updateStartDate(startDate);
	}

	public void updatePurpose(String purpose) {
		this.purpose = purpose;
	}

	private void updateSex(Sex sex) {
		this.sex = sex;
	}

	private void updateBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	private void updateTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	private void updateCigarettePrice(int cigarettePrice) {
		this.cigarettePrice = cigarettePrice;
	}

	private void completeSetting() {
		this.settingStatus = SettingStatus.COMPLETE;
	}

	private void updateStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

}
