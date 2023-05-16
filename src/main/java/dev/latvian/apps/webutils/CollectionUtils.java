package dev.latvian.apps.webutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface CollectionUtils {
	static <T> Stream<T> toStream(Iterable<T> iterable) {
		if (iterable instanceof Collection) {
			return ((Collection<T>) iterable).stream();
		}

		List<T> list = new ArrayList<>();
		iterable.forEach(list::add);
		return list.stream();
	}
}
