package dev.latvian.apps.webutils.data;

import dev.latvian.apps.webutils.MiscUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Mutable<T> {
	private static final Function<Object, Mutable<?>> MAP_VALUE = o -> new Mutable<>();

	public static <T> Function<Object, Mutable<T>> mapValue() {
		return MiscUtils.cast(MAP_VALUE);
	}

	public T value = null;

	public Mutable() {
	}

	public Mutable(T v) {
		value = v;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public static <T> T valueOf(@Nullable Mutable<T> v) {
		return v == null ? null : v.value;
	}
}
