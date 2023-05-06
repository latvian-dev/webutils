package dev.latvian.apps.webutils.data;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MutableLong {
	public static final Function<Object, MutableLong> MAP_VALUE = o -> new MutableLong();

	public long value = 0L;

	public MutableLong() {
	}

	public MutableLong(long v) {
		value = v;
	}

	public void add(long v) {
		value += v;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public static long valueOf(@Nullable MutableLong v, long def) {
		return v == null ? def : v.value;
	}
}
