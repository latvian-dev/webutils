package dev.latvian.apps.webutils.data;

import org.jetbrains.annotations.Nullable;

public record Possible<T>(@Nullable Object value) {
	public static final Possible<?> ABSENT = new Possible<>(null);
	public static final Possible<?> NULL = new Possible<>(null);

	public Possible(@Nullable Object value) {
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public static <T> Possible<T> of(@Nullable T o) {
		return o == null ? (Possible<T>) NULL : new Possible<>(o);
	}

	@SuppressWarnings("unchecked")
	public static <T> Possible<T> absent() {
		return (Possible<T>) ABSENT;
	}

	public boolean isSet() {
		return this != ABSENT;
	}

	public boolean isAbsent() {
		return this == ABSENT;
	}

	public String toString() {
		return this == ABSENT ? "EMPTY" : String.valueOf(this.value);
	}

	@Override
	@Nullable
	public Object value() {
		return this.value;
	}
}