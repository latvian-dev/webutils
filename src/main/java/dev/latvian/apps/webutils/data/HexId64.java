package dev.latvian.apps.webutils.data;

import dev.latvian.apps.tinyserver.http.response.error.client.BadRequestError;
import dev.latvian.apps.webutils.html.Tag;
import dev.latvian.apps.webutils.html.TagFunction;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.LongSupplier;

public class HexId64 implements TagFunction, LongSupplier {
	public static final HexId64 NONE = new HexId64(0L);
	public static final HexId64 SELF = new HexId64(-1L);
	public static BiConsumer<Tag, HexId64> TAG = (parent, id) -> parent.string(id.toString());

	public static final Function<String, HexId64> MAPPER = string -> {
		if (string == null || string.isBlank() || string.charAt(0) == '-') {
			return NONE;
		} else if (!isValid(string)) {
			throw new BadRequestError("Invalid ID: " + string);
		} else {
			return of(string);
		}
	};

	public static boolean isValid(String id) {
		if (id == null || id.isBlank() || id.length() > 16) {
			return false;
		}

		for (int i = 0; i < id.length(); i++) {
			char c = id.charAt(i);

			if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f'))) {
				return false;
			}
		}

		return true;
	}

	public static HexId64 of(long id) {
		return id == 0 ? NONE : new HexId64(id);
	}

	public static HexId64 of(String id) {
		return id == null || id.isBlank() || id.charAt(0) == '-' ? NONE : of(Long.parseUnsignedLong(id, 16));
	}

	private final long id;

	private HexId64(long id) {
		this.id = id;
	}

	public long toLong() {
		return id;
	}

	@Override
	public String toString() {
		return "%016X".formatted(id);
	}

	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || obj instanceof HexId64 o && o.id == id;
	}

	@Override
	public void acceptTag(Tag parent) {
		TAG.accept(parent, this);
	}

	@Override
	public long getAsLong() {
		return id;
	}
}
