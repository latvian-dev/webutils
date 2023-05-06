package dev.latvian.apps.webutils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.latvian.apps.webutils.html.Tag;
import dev.latvian.apps.webutils.html.TagConvertible;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.IntSupplier;

public class HexId32 implements TagConvertible, IntSupplier {
	public static final HexId32 NONE = new HexId32(0);
	public static final HexId32 SELF = new HexId32(-1);
	public static BiConsumer<Tag, HexId32> TAG = (parent, id) -> parent.string(id.toString());

	public static boolean isValid(String id) {
		if (id == null || id.isBlank() || id.length() > 8) {
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

	public static HexId32 of(int id) {
		return id == 0 ? NONE : id == -1 ? SELF : new HexId32(id);
	}

	public static HexId32 of(String id) {
		return id == null || id.isBlank() || id.charAt(0) == '-' ? NONE : of(Integer.parseUnsignedInt(id, 16));
	}

	public static HexId32 of(@Nullable JsonElement json) {
		return json instanceof JsonPrimitive primitive ? primitive.isString() ? of(json.getAsString()) : of(json.getAsInt()) : NONE;
	}

	private final int id;

	private HexId32(int id) {
		this.id = id;
	}

	public int toInt() {
		return id;
	}

	@Override
	public String toString() {
		return "%08X".formatted(id);
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || obj instanceof HexId32 o && o.id == id;
	}

	@Override
	public void appendHTMLTag(Tag parent) {
		TAG.accept(parent, this);
	}

	@Override
	public int getAsInt() {
		return id;
	}
}
