package app.edlc.taskapi.security.jwt;

import java.util.Base64;

public class KeyUtil {
	
	public static boolean isBase64Encoded(String key) {
		try {
			Base64.getDecoder().decode(key);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String encodeKeyToBase64(String rawKey) {
		return Base64.getEncoder().encodeToString(rawKey.getBytes());
	}
	
	public static String ensureBase64Key(String key) {
		if (isBase64Encoded(key)) {
			return key;
		} else {
			return encodeKeyToBase64(key);
		}
	}
}
