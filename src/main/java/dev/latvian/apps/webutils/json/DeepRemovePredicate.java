package dev.latvian.apps.webutils.json;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface DeepRemovePredicate {
	DeepRemovePredicate NONE = (key, index, value) -> false;

	boolean remove(String key, int index, @Nullable Object value);
}
