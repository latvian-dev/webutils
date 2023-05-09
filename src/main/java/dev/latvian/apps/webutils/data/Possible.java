package dev.latvian.apps.webutils.data;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public record Possible<T>(@Nullable T value) implements Supplier<T> {
	public static final Possible<?> ABSENT = new Possible<>(null);
	public static final Possible<?> NULL = new Possible<>(null);

	public static <T> Possible<T> of(@Nullable T o) {
		return o == null ? (Possible<T>) NULL : new Possible<>(o);
	}

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

	public <C> Possible<C> cast(Class<C> type) {
		return (Possible<C>) this;
	}

	public <C> Possible<C> map(Function<T, C> function) {
		return isAbsent() ? absent() : Possible.of(function.apply(value));
	}

	@Override
	@Nullable
	public T get() {
		return isAbsent() ? null : get();
	}
}