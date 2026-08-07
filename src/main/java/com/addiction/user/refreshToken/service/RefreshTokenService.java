package com.addiction.user.refreshToken.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.addiction.jwt.JwtTokenGenerator;
import com.addiction.jwt.dto.JwtToken;
import com.addiction.jwt.dto.LoginUserInfo;
import com.addiction.jwt.exception.JwtTokenException;
import com.addiction.user.refreshToken.entity.RefreshToken;
import com.addiction.user.refreshToken.repository.RefreshTokenJpaRepository;
import com.addiction.user.users.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
	private final RefreshTokenJpaRepository refreshTokenJpaRepository;
	private final JwtTokenGenerator jwtTokenGenerator;

	/** 로그인 및 재발급 시에는 원문 대신 SHA-256 해시만 저장한다. */
	public void register(User user, JwtToken jwtToken, String deviceId) {
		String tokenHash = hash(jwtToken.getRefreshToken());
		refreshTokenJpaRepository.findByUserIdAndDeviceId(user.getId(), deviceId)
			.ifPresentOrElse(
				existing -> existing.updateRefreshToken(tokenHash),
				() -> refreshTokenJpaRepository.save(RefreshToken.of(user, tokenHash, deviceId))
			);
	}

	public JwtToken rotate(String refreshToken, String deviceId) {
		LoginUserInfo loginUserInfo = jwtTokenGenerator.validateRefreshToken(refreshToken);
		RefreshToken storedToken = refreshTokenJpaRepository
			.findByRefreshTokenAndDeviceId(hash(refreshToken), deviceId)
			.orElseThrow(() -> new JwtTokenException("REFRESH_TOKEN_REVOKED", "Refresh token이 폐기되었거나 존재하지 않습니다."));

		if (!storedToken.belongsTo(loginUserInfo.getUserId())) {
			throw new JwtTokenException("INVALID_REFRESH_TOKEN", "Refresh token 형식이 올바르지 않습니다.");
		}

		JwtToken renewed;
		try {
			renewed = jwtTokenGenerator.generate(loginUserInfo);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("토큰 subject 생성에 실패했습니다.", e);
		}
		storedToken.updateRefreshToken(hash(renewed.getRefreshToken()));
		return renewed;
	}

	public void revoke(String refreshToken, String deviceId) {
		LoginUserInfo loginUserInfo = jwtTokenGenerator.validateRefreshToken(refreshToken);
		RefreshToken storedToken = refreshTokenJpaRepository
			.findByRefreshTokenAndDeviceId(hash(refreshToken), deviceId)
			.orElseThrow(() -> new JwtTokenException("REFRESH_TOKEN_REVOKED", "Refresh token이 폐기되었거나 존재하지 않습니다."));

		if (!storedToken.belongsTo(loginUserInfo.getUserId())) {
			throw new JwtTokenException("INVALID_REFRESH_TOKEN", "Refresh token 형식이 올바르지 않습니다.");
		}
		refreshTokenJpaRepository.delete(storedToken);
	}

	private String hash(String value) {
		try {
			byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
			return java.util.HexFormat.of().formatHex(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 algorithm is unavailable", e);
		}
	}
}
