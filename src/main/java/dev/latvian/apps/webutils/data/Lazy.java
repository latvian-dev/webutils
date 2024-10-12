package dev.latvian.apps.webutils.data;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
	private record Present<T>(T value) implements Callable<T> {
		@Override
		public T call() {
			return value;
		}
	}

	public static final Lazy<?> EMPTY = present(null);

	public static <T> Lazy<T> of(Callable<T> getter) {
		return new Lazy<>(getter);
	}

	public static <T> Lazy<T> present(T value) {
		return new Lazy<>(new Present<>(value));
	}

	@SuppressWarnings("unchecked")
	public static <T> Lazy<T> empty() {
		return (Lazy<T>) EMPTY;
	}

	private final Callable<T> getter;
	private T object;
	private boolean inited;

	private Lazy(Callable<T> g) {
		getter = g;
	}

	public void invalidate() {
		object = null;
		inited = false;
	}

	@Override
	@Nullable
	public T get() {
		if (!inited) {
			inited = true;

			try {
				object = getter.call();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		return object;
	}

	public void set(T v) {
		inited = true;
		object = v;
	}

	public boolean isPresent() {
		return get() != null;
	}

	public T getNonnull() {
		T t = get();

		if (t == null) {
			throw new NullPointerException();
		}

		return t;
	}

	public Optional<T> getOptional() {
		return Optional.ofNullable(get());
	}
}