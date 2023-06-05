package dev.latvian.apps.webutils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;

public interface CodingUtils {
	byte[] HEX_ARRAY = "0123456789abcdef".getBytes(StandardCharsets.US_ASCII);

	private static MessageDigest getMD5() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (Exception ex) {
			throw new RuntimeException("MD5 not found!");
		}
	}

	private static MessageDigest getSHA1() {
		try {
			return MessageDigest.getInstance("SHA-1");
		} catch (Exception ex) {
			throw new RuntimeException("SHA1 not found!");
		}
	}

	static String hex(byte[] bytes) {
		byte[] hexChars = new byte[bytes.length * 2];

		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}

		return new String(hexChars, StandardCharsets.UTF_8);
		//return javax.xml.bind.DatatypeConverter.printHexBinary(bytes).toLowerCase();
	}

	static String md5(byte[] bytes) {
		return hex(getMD5().digest(bytes));
	}

	static String md5(String string) {
		return md5(string.getBytes(StandardCharsets.UTF_8));
	}

	static String sha1(byte[] bytes) {
		return hex(getSHA1().digest(bytes));
	}

	static String sha1(String string) {
		return sha1(string.getBytes(StandardCharsets.UTF_8));
	}

	static String sha1(String key, String string) throws Exception {
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(keySpec);
		return hex(mac.doFinal(string.getBytes(StandardCharsets.UTF_8)));
	}

	static String encodeURL(String string) {
		try {
			return URLEncoder.encode(string, StandardCharsets.UTF_8);
		} catch (Exception ex) {
			return string;
		}
	}

	static String decodeURL(String string) {
		try {
			return URLDecoder.decode(string, StandardCharsets.UTF_8);
		} catch (Exception ex) {
			return string;
		}
	}

	static String encodeURL(Map<String, String> map, String sep) {
		StringBuilder sb = new StringBuilder();

		boolean first = true;

		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (first) {
				first = false;
			} else {
				sb.append(sep);
			}

			sb.append(encodeURL(entry.getKey()));

			if (!entry.getValue().isEmpty()) {
				sb.append('=');
				sb.append(encodeURL(entry.getValue()));
			}
		}

		return sb.toString();
	}

	static Map<String, String> decodeHeaders(String data) {
		Map<String, String> m = new LinkedHashMap<>();
		String[] s0 = data.split(";");

		for (int i = 0; i < s0.length; i++) {
			String s = s0[i].trim();
			int j = s.indexOf('=');

			if (j == -1) {
				if (i == 0) {
					m.put("", s);
				}

				m.put(s, "");
			} else {
				String s1 = s.substring(j + 1);

				if (s1.startsWith("\"") && s1.endsWith("\"")) {
					s1 = s1.substring(1, s1.length() - 1);
				}

				m.put(s.substring(0, j), decodeURL(s1));
			}
		}

		return m;
	}

	static String formData(Map<String, Object> map) {
		var sb = new StringBuilder();

		for (var entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append('&');
			}

			sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
			sb.append('=');
			sb.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
		}

		return sb.toString();
	}
}
