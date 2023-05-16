package dev.latvian.apps.webutils;

public interface Cast {
	@SuppressWarnings("unchecked")
	static <T> T to(Object value) {
		return value == null ? null : (T) value;
	}
}
