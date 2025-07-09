package com.bongjangboo.legacy.auth;

import com.bongjangboo.legacy.auth.controller.dto.Info.CurrentUserInfo;
import com.bongjangboo.legacy.auth.controller.dto.response.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.bongjangboo.legacy.auth.domain.user.CustomUserDetails;

@Component
public class JwtTokenProvider {
	public static final String BEARER_TYPE = "Bearer";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String REFRESH_HEADER = "Refresh";
	public static final String BEARER_PREFIX = "Bearer ";

	@Value("${jwt.secret_key}")
	private String encodedSecretKey;

	@Value("${jwt.access_token.valid_time}")
	private Long accessTokenValidTime;

	@Value("${jwt.refresh_token.valid_time}")
	private Long refreshTokenValidTime;

	public JwtToken generateToken(CustomUserDetails customUserDetails) {
		String accessToken = generateAccessToken(customUserDetails);
		String refreshToken = generateRefreshToken(customUserDetails);
		return new JwtToken(BEARER_TYPE,accessToken,refreshToken);
	}

	public CurrentUserInfo extractUserInfo(String token) {
		Long userId = extractClaim(token, claims -> claims.get("userId", Long.class));
		Long deptId = extractClaim(token, claims -> claims.get("deptId", Long.class));
		System.out.println(userId.toString()+" "+deptId.toString());
		return new CurrentUserInfo(userId, deptId);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSecretKey())  // parserBuilder()로 일관성 유지
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public String generateAccessToken(CustomUserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", Long.valueOf(userDetails.getUserId()));
		claims.put("deptId", Long.valueOf(userDetails.getDeptId()));
		return createToken(claims, userDetails.getUsername(),accessTokenValidTime);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername(),refreshTokenValidTime);
	}

	public String createToken(Map<String, Object> claims, String loginId, Long validTime) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + validTime);

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(loginId)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(getSecretKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	private Key getSecretKey() {
		try {
			byte[] keyBytes = Base64.getUrlDecoder().decode(encodedSecretKey); // Base64 URL로 디코딩
			return Keys.hmacShaKeyFor(keyBytes);  // HMAC SHA 키 생성
		} catch (IllegalArgumentException e) {
			System.err.println("Invalid base64 encoded key: " + encodedSecretKey);
			throw e; // 적절한 예외 처리
		}
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	// 토큰 만료 여부 확인
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
}
