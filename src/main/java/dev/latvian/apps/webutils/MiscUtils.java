package dev.latvian.apps.webutils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface MiscUtils {
	@SuppressWarnings("all")
	static <T> T cast(@Nullable Object value) {
		return value == null ? null : (T) value;
	}

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

	static <T> Stream<T> toStream(Iterable<T> iterable) {
		if (iterable instanceof Collection) {
			return ((Collection<T>) iterable).stream();
		}

		List<T> list = new ArrayList<>();
		iterable.forEach(list::add);
		return list.stream();
	}
}
