package com.psicoproject.service.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {
	
	public static String securedHash(String text) {
		String hash = DigestUtils.sha256Hex(text);
		return hash;
	}
}
