package dev.latvian.apps.webutils.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.latvian.apps.webutils.html.Tag;
import dev.latvian.apps.webutils.html.TagConvertible;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.LongSupplier;

public class HexId64 implements TagConvertible, LongSupplier {
	public static final HexId64 NONE = new HexId64(0L);
	public static final HexId64 SELF = new HexId64(-1L);
	public static BiConsumer<Tag, HexId64> TAG = (parent, id) -> parent.string(id.toString());

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

	public static HexId64 of(@Nullable JsonElement json) {
		return json instanceof JsonPrimitive primitive ? primitive.isString() ? of(json.getAsString()) : of(json.getAsLong()) : NONE;
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
	public void appendHTMLTag(Tag parent) {
		TAG.accept(parent, this);
	}

	@Override
	public long getAsLong() {
		return id;
	}
}
