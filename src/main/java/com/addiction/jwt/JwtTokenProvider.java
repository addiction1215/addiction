package com.addiction.jwt;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.addiction.jwt.dto.LoginUserInfo;
import com.addiction.user.users.entity.enums.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.servlet.http.HttpServletRequest;

/*
 * JWTToken의 생성, 유효여부, 복호화 관련 클래스
 * */
@Component
public class JwtTokenProvider {
	public static final String TOKEN_TYPE_CLAIM = "tokenType";
	public static final String ACCESS_TOKEN_TYPE = "ACCESS";
	public static final String REFRESH_TOKEN_TYPE = "REFRESH";

	private final ObjectMapper objectMapper;
	private final Key key;

	//키 생성하여 의존성 주입
	public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey, ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.key = createSigningKey(secretKey);
	}

	private Key createSigningKey(String secretKey) {
		try {
			return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		} catch (DecodingException | IllegalArgumentException e) {
			return createRawSigningKey(secretKey);
		} catch (WeakKeyException e) {
			return createRawSigningKey(secretKey);
		}
	}

	private Key createRawSigningKey(String secretKey) {
		try {
			return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		} catch (WeakKeyException e) {
			throw new IllegalStateException(
				"Invalid jwt.secret-key: provide either a Base64-encoded key or a raw UTF-8 secret with at least 32 bytes.",
				e
			);
		}
	}

	// 토큰 생성
	public String generate(String subject, Date expiredAt, String tokenType, String tokenId) {
		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(new Date())
			.setId(tokenId)
			.claim(TOKEN_TYPE_CLAIM, tokenType)
			.setExpiration(expiredAt)
			.signWith(key)
			.compact();
	}

	// 토큰 만료여부 체크
	public boolean isAccessToken(String token) {
		Claims claims = parseClaims(token);
		return ACCESS_TOKEN_TYPE.equals(claims.get(TOKEN_TYPE_CLAIM, String.class))
			&& !claims.getExpiration().before(new Date());
	}

	public Claims getClaims(String token) {
		return parseClaims(token);
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	// Request의 Header에서 token 값을 가져옵니다. "Authorization" : "TOKEN값'
	public String resolveToken(HttpServletRequest request) {
		String headerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(headerToken) && headerToken.startsWith("Bearer ")) {
			return headerToken.substring(7).trim();
		}
		return null;
	}

	// 토큰에서 회원 정보 추출
	public String getUserPk(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	// JWT 토큰에서 인증 정보 조회
	public Authentication getAuthentication(String token) throws JsonProcessingException {
		LoginUserInfo loginUserInfo = objectMapper.readValue(this.getUserPk(token), LoginUserInfo.class);
		return new UsernamePasswordAuthenticationToken(loginUserInfo, "",
			Collections.singletonList(new SimpleGrantedAuthority(
				Role.USER.name())));
	}
}
