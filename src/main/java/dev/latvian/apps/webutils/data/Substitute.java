package dev.latvian.apps.webutils.data;

import org.jetbrains.annotations.NotNull;

public record Substitute(String with, String string, int order) implements Comparable<Substitute> {
	@Override
	public int compareTo(@NotNull Substitute o) {
		int i = Integer.compare(order, o.order);
		return i == 0 ? with.compareTo(o.with) : i;
	}

	@Override
	public String toString() {
		return string + "->" + with;
	}
}