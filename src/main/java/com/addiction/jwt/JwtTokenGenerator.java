package com.addiction.jwt;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.addiction.jwt.dto.JwtToken;
import com.addiction.jwt.dto.LoginUserInfo;
import com.addiction.jwt.exception.JwtTokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {
	private static final String BEARER_TYPE = "Bearer";
	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;                  // 30분
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30;      // 30일

	private final JwtTokenProvider jwtTokenProvider;

	private final ObjectMapper objectMapper;

	/*
	 * accessToken 발급
	 * */
	public JwtToken generate(LoginUserInfo loginUserInfo) throws JsonProcessingException {
		String subject = objectMapper.writeValueAsString(loginUserInfo);
		long now = (new Date()).getTime();
		Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);                //AccessToken 유효기간 지정
		Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);                //RefreshToken 유효기간 지정

		String accessToken = jwtTokenProvider.generate(subject, accessTokenExpiredAt,
			JwtTokenProvider.ACCESS_TOKEN_TYPE, UUID.randomUUID().toString());
		String refreshToken = jwtTokenProvider.generate(subject, refreshTokenExpiredAt,
			JwtTokenProvider.REFRESH_TOKEN_TYPE, UUID.randomUUID().toString());

		return JwtToken.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
	}

	/*
	 * refreshtoken으로 access-token재발급
	 * */
	public LoginUserInfo validateRefreshToken(String refreshToken) {
		try {
			// JWT 서명과 만료 시간을 검증한 뒤, refresh 전용 토큰인지 확인한다.
			// getClaims() 호출 중 서명이 잘못되었거나 토큰이 만료된 경우 예외가 발생한다.
			if (!JwtTokenProvider.REFRESH_TOKEN_TYPE.equals(
				jwtTokenProvider.getClaims(refreshToken).get(JwtTokenProvider.TOKEN_TYPE_CLAIM, String.class))) {
				throw new JwtTokenException("INVALID_REFRESH_TOKEN", "Refresh token 형식이 올바르지 않습니다.");
			}

			// 검증된 토큰의 subject(JSON)에서 재발급에 사용할 사용자 정보를 복원한다.
			return objectMapper.readValue(jwtTokenProvider.getUserPk(refreshToken), LoginUserInfo.class);
		} catch (ExpiredJwtException e) {
			// 만료된 refresh token은 별도 오류 코드로 반환해 클라이언트가 재로그인 처리할 수 있게 한다.
			throw new JwtTokenException("REFRESH_TOKEN_EXPIRED", "Refresh token이 만료되었습니다.", e);
		} catch (JwtTokenException e) {
			// 위에서 직접 발생시킨 refresh token 관련 오류는 코드 변경 없이 전달한다.
			throw e;
		} catch (JwtException | IllegalArgumentException | JsonProcessingException e) {
			// 서명 오류, 비정상 문자열, subject 역직렬화 실패는 모두 유효하지 않은 refresh token으로 처리한다.
			throw new JwtTokenException("INVALID_REFRESH_TOKEN", "Refresh token 형식이 올바르지 않습니다.", e);
		}
	}
}
