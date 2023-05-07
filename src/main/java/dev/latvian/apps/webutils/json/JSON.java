package dev.latvian.apps.webutils.json;

import dev.latvian.apps.webutils.MiscUtils;
import dev.latvian.apps.webutils.data.HexId32;
import dev.latvian.apps.webutils.data.HexId64;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class JSON {
	public static final Object NULL = new Object();
	public static final JSON DEFAULT = new JSON(null);

	static {
		DEFAULT.registerStringAdapter(UUID.class, UUID::fromString);
		DEFAULT.registerAdapter(Date.class, value -> Date.from(Instant.parse(String.valueOf(value))), date -> date.toInstant().toString());
		DEFAULT.registerStringAdapter(Instant.class, Instant::parse);
		DEFAULT.registerAdapter(URL.class, value -> {
			try {
				return new URL(String.valueOf(value));
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}, URL::toString);
		DEFAULT.registerStringAdapter(URI.class, URI::create);
		DEFAULT.registerStringAdapter(Path.class, Path::of);
		DEFAULT.registerAdapter(Class.class, value -> {
			try {
				return Class.forName(String.valueOf(value));
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}, Class::getName);
		DEFAULT.registerStringAdapter(HexId32.class, HexId32::of);
		DEFAULT.registerStringAdapter(HexId64.class, HexId64::of);
		DEFAULT.registerStringAdapter(StringBuilder.class, StringBuilder::new);
	}

	private static void escape(Writer writer, String string) throws IOException {
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);

			switch (c) {
				case '"' -> writer.write("\\\"");
				case '\\' -> writer.write("\\\\");
				case '\b' -> writer.write("\\b");
				case '\f' -> writer.write("\\f");
				case '\n' -> writer.write("\\n");
				case '\r' -> writer.write("\\r");
				case '\t' -> writer.write("\\t");
				default -> {
					if (c < ' ') {
						writer.write("\\u");
						writer.write(String.format("%04x", (int) c));
					} else {
						writer.write(c);
					}
				}
			}
		}
	}

	private final JSON parent;
	private final Map<Class<?>, JSONAdapter<?>> adapters;

	private JSON(JSON parent) {
		this.parent = parent;
		this.adapters = new IdentityHashMap<>();
	}

	public <T> void registerAdapter(Class<T> type, JSONAdapter<T> adapter) {
		adapters.put(type, adapter);
	}

	public <T> void registerAdapter(Class<T> type, final Function<Object, T> adapt, final Function<T, Object> write) {
		registerAdapter(type, new JSONAdapter<>() {
			@Override
			public T adapt(Object value) {
				return adapt.apply(value);
			}

			@Override
			public void write(JSON json, Writer writer, T value, int depth, boolean pretty) throws IOException {
				json.write(writer, write.apply(value), depth, pretty);
			}
		});
	}

	public <T> void registerStringAdapter(Class<T> type, final Function<String, T> adapt) {
		registerAdapter(type, new JSONAdapter<>() {
			@Override
			public T adapt(Object value) {
				return adapt.apply(String.valueOf(value));
			}

			@Override
			public void write(JSON json, Writer writer, T value, int depth, boolean pretty) throws IOException {
				json.write(writer, value.toString(), depth, pretty);
			}
		});
	}

	public <T> JSONAdapter<T> getAdapter(Class<T> type) {
		var adapter = adapters.get(type);

		if (adapter == null) {
			var p = parent;

			while (p != null) {
				adapter = p.adapters.get(type);

				if (adapter != null) {
					break;
				}

				p = p.parent;
			}
		}

		if (adapter == null) {
			adapter = new ReflectionJSONAdapter(this, type);
			adapters.put(type, adapter);
		}

		return (JSONAdapter<T>) adapter;
	}

	public JSONReader read(String string) {
		return new JSONCharArrayReader(this, string.toCharArray());
	}

	public JSONReader read(Path path) throws IOException {
		// use BufferedJSONReader in future
		return new JSONCharArrayReader(this, String.join("", Files.readAllLines(path)).toCharArray());
	}

	public String write(@Nullable Object value) {
		var builder = new StringWriter();

		try {
			write(builder, value, 0, false);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return builder.toString();
	}

	public String writePretty(@Nullable Object value) {
		var builder = new StringWriter();

		try {
			write(builder, value, 0, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return builder.toString();
	}

	public void write(Writer writer, @Nullable Object value, int depth, boolean pretty) throws IOException {
		if (depth > 1000) {
			throw new IllegalStateException("JSON depth limit reached");
		} else if (value == null || value == NULL) {
			writer.write("null");
		} else if (value instanceof JSONSerializable) {
			write(writer, ((JSONSerializable) value).toJSON(), depth, pretty);
		} else if (value instanceof String str) {
			writer.write('"');
			escape(writer, str);
			writer.write('"');
		} else if (value instanceof Number || value instanceof Boolean) {
			writer.write(String.valueOf(value));
		} else if (value instanceof Map<?, ?> map) {
			boolean first = true;
			writer.write('{');

			for (var entry : map.entrySet()) {
				if (first) {
					first = false;
				} else {
					writer.write(',');
				}

				writer.write('"');
				escape(writer, String.valueOf(entry.getKey()));
				writer.write('"');
				writer.write(':');
				write(writer, entry.getValue(), depth + 1, pretty);
			}

			writer.write('}');
		} else if (value instanceof Iterable<?> itr) {
			boolean first = true;
			writer.write('[');

			for (var o : itr) {
				if (first) {
					first = false;
				} else {
					writer.write(',');
				}

				write(writer, o, depth + 1, pretty);
			}

			writer.write(']');
		} else if (value instanceof Object[] arr) {
			write(writer, Arrays.asList(arr), depth, pretty);
		} else if (value.getClass().isArray()) {
			writer.write('[');
			int len = Array.getLength(value);

			for (int i = 0; i < len; i++) {
				if (i > 0) {
					writer.write(',');
				}

				write(writer, Array.get(value, i), depth + 1, pretty);
			}

			writer.write(']');
		} else if (value instanceof Enum<?> e) {
			write(writer, e.name().toLowerCase(), depth, pretty);
		} else if (value instanceof Record) {
			var obj = new JSONObject();

			for (var field : value.getClass().getRecordComponents()) {
				try {
					obj.put(field.getName(), field.getAccessor().invoke(value));
				} catch (Exception ex) {
					throw new RuntimeException("Failed to access record method", ex);
				}
			}

			write(writer, obj, depth, pretty);
		}
		// Other
		else {
			getAdapter(value.getClass()).write(this, writer, MiscUtils.cast(value), depth, pretty);
		}
	}

	public JSON child() {
		return new JSON(this);
	}

	@SuppressWarnings("unchecked")
	public <T> T adapt(Object jsonValue, Class<T> type) {
		if (type == Object.class) {
			return (T) jsonValue;
		} else if (type == String.class) {
			return (T) String.valueOf(jsonValue);
		} else if (type == Character.class || type == Character.TYPE) {
			return (T) Character.valueOf(String.valueOf(jsonValue).charAt(0));
		} else if (type == Number.class) {
			return (T) jsonValue;
		} else if (type == Byte.class || type == Byte.TYPE) {
			return (T) Byte.valueOf(((Number) jsonValue).byteValue());
		} else if (type == Short.class || type == Short.TYPE) {
			return (T) Short.valueOf(((Number) jsonValue).shortValue());
		} else if (type == Integer.class || type == Integer.TYPE) {
			return (T) Integer.valueOf(((Number) jsonValue).intValue());
		} else if (type == Long.class || type == Long.TYPE) {
			return (T) Long.valueOf(((Number) jsonValue).longValue());
		} else if (type == Float.class || type == Float.TYPE) {
			return (T) Float.valueOf(((Number) jsonValue).floatValue());
		} else if (type == Double.class || type == Double.TYPE) {
			return (T) Double.valueOf(((Number) jsonValue).doubleValue());
		} else if (type == Map.class || type == JSONObject.class) {
			return (T) jsonValue;
		} else if (type == List.class || type == Collection.class || type == Iterable.class || type == JSONArray.class) {
			return (T) jsonValue;
		} else if (type == Set.class) {
			return (T) new HashSet<>((Collection<?>) jsonValue);
		} else if (type.isEnum()) {
			var str = String.valueOf(jsonValue);

			for (var e : type.getEnumConstants()) {
				if (e.toString().equalsIgnoreCase(str)) {
					return e;
				}
			}

			throw new IllegalArgumentException("Unknown enum constant: " + str);
		}
		// Other
		else {
			return MiscUtils.cast(getAdapter(type).adapt(jsonValue));
		}
	}
}
