package dev.latvian.apps.webutils.data;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MutableInt {
	public static final Function<Object, MutableInt> MAP_VALUE = o -> new MutableInt();

	public int value = 0;

	public MutableInt() {
	}

	public MutableInt(int v) {
		value = v;
	}

	public void add(int v) {
		value += v;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public static int valueOf(@Nullable MutableInt v, int def) {
		return v == null ? def : v.value;
	}
}
