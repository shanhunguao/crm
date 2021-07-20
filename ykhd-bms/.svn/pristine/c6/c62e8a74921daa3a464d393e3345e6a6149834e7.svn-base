package com.ykhd.office.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

/**
 * encode & decode
 */
public final class SecurityUtil {
	
	private static Logger log = LoggerFactory.getLogger(SecurityUtil.class);
	
	private static String private_key = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALBruEo+d/"
			+ "HijO0XwWznD73IWXx/R6HnmiA9OT2msqnFERS4A1QTd/Ol81VK4dCoZBJmMJ3nd9uICKaHFLf4HV9u4JCQha/"
			+ "++TsuR1lxC2uUB+C1YyduHmVhGLQwknnUoxY8WXAypChuMYHvl4YJ9BX1hbdYb1T93fxipxLeGrcpAgMBAAEC"
			+ "gYEAo+wTGnkcvdXBF/OAuA2h5Lcp3rHs/xTRcq8wZoZjb7c5/cRU42QLiophpJilZVlxUl89QRr3lTW6w/CzY"
			+ "ZTaxGyfRXMO6+jhd8ZpjkarVgYEtkxEpyCRiZWBld+Nl7OU8/5EDO/Iq7dsZieLPXoS5tXgCrZJGoml4k/Q7N"
			+ "7TedkCQQDZCtTCBAxSAQ1SOHo++UjSBacNRG+RIJXIoE6gL+UTqUCkKj/+GWjzdmKMxOC3LAxnAo0A2UjT7xV"
			+ "LDOnmAfsPAkEA0BZQyZ6o7niGeyIDnCPbZsLrbrbW2aaRfUPst/sZDtk6zjS1FBJdEjfmXwqwiMBIAilUz1FM"
			+ "wFWu06bJOkiKRwJBAJq5nfKx8BaFqXlzybIbBE60uOI8Z0yObjkYDvUjL6tUfUCK3hPHCYqGWrr/nzL1s+G7b"
			+ "8mrHL/5MLTP/+54ewECQQCVNgtEKCMWrRIxpVhVVFj1PPw5bZ5uf8R/wH80BjgXr6k9SAo4lFpsv+zPZgjkZo"
			+ "y8EAsihje1CzvLxSMySeXtAkAKP2CvvxdlJ6aITVOGMQeeM7UpwO2vXqLVyOnBmu1TSPDKSUxgp9BYOHzBPP/"
			+ "bZC6d6ebeSRz1on5g01esFiit";

	/**
	 * md5 encode
	 */
	public static String md5Encode(String data) {
		return DigestUtils.md5DigestAsHex(data.getBytes());
	}
	
	/**
	 * private_key decode
	 */
	public static String privateKeyDecryption(String data) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(private_key));
		try {
			PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(pkcs8EncodedKeySpec);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
		} catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			log.error("#private_key decode error# -->[{}]， data = [{}]", e.getMessage(), data);
			throw new IllegalArgumentException("password illegal !");
		}
	}
	
}
