package dev.latvian.apps.webutils.data;

import dev.latvian.apps.webutils.ansi.Ansi;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class Lazy<T> {
	@FunctionalInterface
	public interface LazySupplier<T> {
		T get() throws Exception;
	}

	private record Present<T>(T value) implements LazySupplier<T> {
		@Override
		public T get() {
			return value;
		}
	}

	public static final Lazy<?> EMPTY = of(() -> null);

	public static <T> Lazy<T> of(LazySupplier<T> getter) {
		return new Lazy<>(getter);
	}

	public static <T> Lazy<T> present(T value) {
		return new Lazy<>(new Present<>(value));
	}

	@SuppressWarnings("unchecked")
	public static <T> Lazy<T> empty() {
		return (Lazy<T>) EMPTY;
	}

	private final LazySupplier<T> getter;
	private T object;
	private boolean inited;

	private Lazy(LazySupplier<T> g) {
		getter = g;
	}

	public void invalidate() {
		object = null;
		inited = false;
	}

	@Nullable
	public T get() {
		if (!inited) {
			inited = true;

			try {
				object = getter.get();
			} catch (Exception ex) {
				Ansi.log("! " + ex);
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