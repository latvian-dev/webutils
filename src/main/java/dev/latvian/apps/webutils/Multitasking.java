package dev.latvian.apps.webutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public interface Multitasking {
	static <T> List<T> supply(Executor executor, Supplier<T>[] tasks) {
		return supply(executor, Arrays.asList(tasks));
	}

	static <T> List<T> supply(Executor executor, List<? extends Supplier<T>> tasks) {
		CompletableFuture<T>[] futures = new CompletableFuture[tasks.size()];

		for (int i = 0; i < tasks.size(); i++) {
			futures[i] = CompletableFuture.supplyAsync(tasks.get(i), executor);
		}

		try {
			return CompletableFuture.allOf(futures).thenApply(v -> {
				var list = new ArrayList<T>(tasks.size());

				for (var f : futures) {
					list.add(f.join());
				}

				return list;
			}).get();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	static void run(Executor executor, Runnable[] tasks) {
		run(executor, Arrays.asList(tasks));
	}

	static void run(Executor executor, List<? extends Runnable> tasks) {
		CompletableFuture<Void>[] futures = new CompletableFuture[tasks.size()];

		for (int i = 0; i < tasks.size(); i++) {
			futures[i] = CompletableFuture.runAsync(tasks.get(i), executor);
		}

		try {
			CompletableFuture.allOf(futures).get();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
