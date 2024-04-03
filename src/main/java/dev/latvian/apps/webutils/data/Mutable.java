package dev.latvian.apps.webutils.data;

import dev.latvian.apps.webutils.Cast;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Mutable<T> implements Supplier<T>, Consumer<T> {
	private static final Function<Object, Mutable<?>> MAP_VALUE = o -> new Mutable<>();

	public static <T> Function<Object, Mutable<T>> mapValue() {
		return Cast.to(MAP_VALUE);
	}

	public T value = null;

	public Mutable() {
	}

	public Mutable(T v) {
		value = v;
	}

	@Override
	public T get() {
		return value;
	}

	@Override
	public void accept(T t) {
		value = t;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public static <T> T valueOf(@Nullable Mutable<T> v) {
		return v == null ? null : v.value;
	}
}
