package com.bongjangboo.legacy.oauth.token.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import jakarta.annotation.PostConstruct;

@Component
public class TokenEncryptor {
	private static final String ALGORITHM = "AES";

	@Value("${oauth.open-banking.key.token-secret}")
	private String key;

	private static String KEY;

	@PostConstruct
	public void init() {
		TokenEncryptor.KEY = this.key;
	}

	public static String encrypt(String token) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedToken = cipher.doFinal(token.getBytes());
		return Base64.getEncoder().encodeToString(encryptedToken);
	}

	public static String decrypt(String encryptedToken) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decodedBytes = Base64.getDecoder().decode(encryptedToken);
		byte[] originalToken = cipher.doFinal(decodedBytes);
		return new String(originalToken);
	}
}
