package dev.latvian.apps.webutils;

import java.lang.reflect.Field;

public interface ReflectionUtils {
	static Object getPrivate(Object obj, Class<?> c, String f) {
		try {
			Field field = c.getDeclaredField(f);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
