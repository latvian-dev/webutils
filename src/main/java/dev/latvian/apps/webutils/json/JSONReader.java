package dev.latvian.apps.webutils.json;

import dev.latvian.apps.webutils.MiscUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface JSONReader {
	JSON json();

	char read();

	char peek();

	default void expect(char c) {
		if (read() != c) {
			throw new IllegalStateException("Expected " + c);
		}
	}

	@SuppressWarnings("unchecked")
	default <T> T read(Class<T> type) {
		if (type == Object.class) {
			return (T) readValue();
		} else if (type == String.class) {
			return (T) readString();
		} else if (type == Character.class || type == Character.TYPE) {
			return (T) Character.valueOf(readString().charAt(0));
		} else if (type == Number.class) {
			return (T) readNumber();
		} else if (type == Byte.class || type == Byte.TYPE) {
			return (T) Byte.valueOf(readNumber().byteValue());
		} else if (type == Short.class || type == Short.TYPE) {
			return (T) Short.valueOf(readNumber().shortValue());
		} else if (type == Integer.class || type == Integer.TYPE) {
			return (T) Integer.valueOf(readNumber().intValue());
		} else if (type == Long.class || type == Long.TYPE) {
			return (T) Long.valueOf(readNumber().longValue());
		} else if (type == Float.class || type == Float.TYPE) {
			return (T) Float.valueOf(readNumber().floatValue());
		} else if (type == Double.class || type == Double.TYPE) {
			return (T) Double.valueOf(readNumber().doubleValue());
		} else if (type == Map.class || type == JSONObject.class) {
			return (T) readObject();
		} else if (type == List.class || type == Collection.class || type == Iterable.class || type == JSONArray.class) {
			return (T) readArray();
		} else if (type == Set.class) {
			return (T) new HashSet<>(readArray());
		} else if (type.isEnum()) {
			var str = readString();

			for (var e : type.getEnumConstants()) {
				if (e.toString().equalsIgnoreCase(str)) {
					return e;
				}
			}

			throw new IllegalArgumentException("Unknown enum constant: " + str);
		}
		// Other
		else {
			return MiscUtils.cast(json().getAdapter(type).read(this));
		}
	}

	@Nullable
	default Object readValue() {
		return switch (peek()) {
			case 'n' -> readNull();
			case 't' -> readTrue();
			case 'f' -> readFalse();
			case '"' -> readString();
			case '{' -> readObject();
			case '[' -> readArray();
			default -> readNumber();
		};
	}

	@Nullable
	default Object readNull() {
		expect('n');
		expect('u');
		expect('l');
		expect('l');
		return null;
	}

	default Boolean readTrue() {
		expect('t');
		expect('r');
		expect('u');
		expect('e');
		return Boolean.TRUE;
	}

	default Boolean readFalse() {
		expect('f');
		expect('a');
		expect('l');
		expect('s');
		expect('e');
		return Boolean.FALSE;
	}

	default String readString() {
		expect('"');

		var builder = new StringBuilder();

		while (peek() != '"') {
			char c = read();

			if (c == '\\') {
				c = read();

				switch (c) {
					case 'b' -> builder.append('\b');
					case 'f' -> builder.append('\f');
					case 'n' -> builder.append('\n');
					case 'r' -> builder.append('\r');
					case 't' -> builder.append('\t');
					case 'u' -> {
						char[] hex = new char[4];

						for (int i = 0; i < 4; i++) {
							hex[i] = read();
						}

						builder.append((char) Integer.parseInt(new String(hex), 16));
					}
					default -> builder.append(c);
				}
			} else {
				builder.append(c);
			}
		}

		expect('"');
		return builder.toString();
	}

	default Number readNumber() {
		// Improve this implementation later
		var builder = new StringBuilder();

		var c = peek();

		while (c == '-' || c == '+' || c == '.' || (c >= '0' && c <= '9')) {
			builder.append(read());
			c = peek();
		}

		String str = builder.toString();

		if (str.contains(".")) {
			return Double.parseDouble(str);
		} else {
			return Long.parseLong(str);
		}
	}

	default JSONObject readObject() {
		expect('{');
		var obj = new JSONObject();

		while (peek() != '}') {
			String key = readString();
			expect(':');
			var v = readValue();
			obj.put(key, v == null ? JSON.NULL : v);

			while (peek() == ',') {
				read();
			}
		}

		expect('}');
		return obj;
	}

	default JSONArray readArray() {
		expect('[');
		var arr = new JSONArray();

		while (peek() != ']') {
			var v = readValue();
			arr.add(v == null ? JSON.NULL : v);

			while (peek() == ',') {
				read();
			}
		}

		expect(']');
		return arr;
	}
}
