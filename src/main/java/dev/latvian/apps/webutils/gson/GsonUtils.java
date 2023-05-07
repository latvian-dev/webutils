package dev.latvian.apps.webutils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.apps.webutils.ansi.Ansi;
import dev.latvian.apps.webutils.ansi.AnsiComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface GsonUtils {
	GsonBuilder GSON_BUILDER = new GsonBuilder()
			.disableHtmlEscaping()
			.setLenient()
			.registerTypeAdapterFactory(GsonFactory.HEX32)
			.registerTypeAdapterFactory(GsonFactory.HEX64)
			/*
			.registerTypeAdapter(Snowflake.class, new TypeAdapter<Snowflake>() {
				@Override
				public void write(JsonWriter out, Snowflake value) throws IOException {
					out.value(value.asString());
				}

				@Override
				public Snowflake read(JsonReader in) throws IOException {
					String s = in.nextString();
					return s.isEmpty() || s.equals("0") ? NO_SNOWFLAKE : Snowflake.of(s);
				}
			})
			 */
			.serializeNulls();

	Gson GSON = GSON_BUILDER.create();
	Gson GSON_PRETTY = GSON_BUILDER.setPrettyPrinting().create();

	static JsonElement toJson(@Nullable Object o) {
		if (o == null) {
			return JsonNull.INSTANCE;
		} else if (o instanceof JsonElement json) {
			return json;
		} else if (o instanceof GsonSerializable serializable) {
			return serializable.toJson();
		} else if (o instanceof Map<?, ?> map) {
			var json = new JsonObject();

			for (var entry : map.entrySet()) {
				json.add(String.valueOf(entry.getKey()), toJson(entry.getValue()));
			}

			return json;
		} else if (o instanceof Boolean bool) {
			return new JsonPrimitive(bool);
		} else if (o instanceof Number num) {
			return new JsonPrimitive(num);
		} else if (o instanceof Iterable<?> itr) {
			var array = new JsonArray();

			for (var o1 : itr) {
				array.add(toJson(o1));
			}

			return array;
		}

		return new JsonPrimitive(o.toString());
	}

	static AnsiComponent ansi(JsonElement json) {
		return ansi0(json, 0);
	}

	private static AnsiComponent ansi0(@Nullable JsonElement element, int depth) {
		if (element == null || element.isJsonNull()) {
			return Ansi.darkRed("null");
		} else if (element instanceof JsonObject json) {
			var builder = Ansi.of().debugColor(depth);
			builder.append('{');
			boolean first = true;

			for (var entry : json.entrySet()) {
				if (first) {
					first = false;
				} else {
					builder.append(',');
				}

				builder.append(Ansi.cyan("\"" + entry.getKey() + "\""));
				builder.append(':');
				builder.append(ansi0(entry.getValue(), depth + 1));
			}

			builder.append('}');
			return builder;
		} else if (element instanceof JsonArray json) {
			var builder = Ansi.of();
			builder.append('[');
			boolean first = true;

			for (var e : json) {
				if (first) {
					first = false;
				} else {
					builder.append(',');
				}

				builder.append(ansi0(e, depth + 1));
			}

			builder.append(']');
			return builder;
		} else {
			var primitive = (JsonPrimitive) element;

			if (primitive.isString()) {
				return Ansi.of(primitive).orange();
			} else if (primitive.isBoolean()) {
				return primitive.getAsBoolean() ? Ansi.green("true") : Ansi.red("false");
			} else {
				return Ansi.purple(primitive);
			}
		}
	}

	static JsonArray orArray(JsonElement element) {
		if (element.isJsonArray()) {
			return element.getAsJsonArray();
		}

		JsonArray array = new JsonArray();
		array.add(element);
		return array;
	}

	static JsonObject object(String key, JsonElement value) {
		var obj = new JsonObject();
		obj.add(key, value);
		return obj;
	}
}
