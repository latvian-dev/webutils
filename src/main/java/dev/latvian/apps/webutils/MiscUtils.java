package dev.latvian.apps.webutils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
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

	static <T> List<T> multitaskSupply(Executor executor, Supplier<T>[] tasks) {
		CompletableFuture<T>[] futures = new CompletableFuture[tasks.length];

		for (int i = 0; i < tasks.length; i++) {
			futures[i] = CompletableFuture.supplyAsync(tasks[i], executor);
		}

		try {
			return CompletableFuture.allOf(futures).thenApply(v -> {
				var list = new ArrayList<T>(tasks.length);

				for (var f : futures) {
					list.add(f.join());
				}

				return list;
			}).get();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	static void multitaskRun(Executor executor, Runnable[] tasks) {
		CompletableFuture<Void>[] futures = new CompletableFuture[tasks.length];

		for (int i = 0; i < tasks.length; i++) {
			futures[i] = CompletableFuture.runAsync(tasks[i], executor);
		}

		try {
			CompletableFuture.allOf(futures).get();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
