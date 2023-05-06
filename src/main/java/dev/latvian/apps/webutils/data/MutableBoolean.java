package dev.latvian.apps.webutils.data;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MutableBoolean {
	public static final Function<Object, MutableBoolean> MAP_VALUE = o -> new MutableBoolean();

	public boolean value = false;

	public MutableBoolean() {
	}

	public MutableBoolean(boolean v) {
		value = v;
	}

	public void toggle() {
		value = !value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public static boolean valueOf(@Nullable MutableBoolean v, boolean def) {
		return v == null ? def : v.value;
	}
}
