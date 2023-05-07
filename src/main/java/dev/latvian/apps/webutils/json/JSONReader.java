package dev.latvian.apps.webutils.json;

import org.jetbrains.annotations.Nullable;

public interface JSONReader {
	default JSON json() {
		return JSON.DEFAULT;
	}

	char read();

	char peek();

	default void expect(char c) {
		if (read() != c) {
			throw new IllegalStateException("Expected " + c);
		}
	}

	default boolean peekWhitespace() {
		var c = peek();
		return c != '\0' && Character.isWhitespace(c);
	}

	default void skipWhitespace() {
		while (peekWhitespace()) {
			read();
		}
	}

	@SuppressWarnings("unchecked")
	default <T> T adapt(Class<T> type) {
		return json().adapt(readValue(), type);
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
		skipWhitespace();

		var obj = new JSONObject();

		while (peek() != '}') {
			String key = readString();
			skipWhitespace();
			expect(':');
			skipWhitespace();
			var v = readValue();

			obj.put(key, v == null ? JSON.NULL : v);

			while (peek() == ',' || peekWhitespace()) {
				read();
			}
		}

		expect('}');
		return obj;
	}

	default JSONArray readArray() {
		expect('[');
		skipWhitespace();

		var arr = new JSONArray();

		while (peek() != ']') {
			var v = readValue();
			skipWhitespace();

			arr.add(v == null ? JSON.NULL : v);

			while (peek() == ',' || peekWhitespace()) {
				read();
			}
		}

		expect(']');
		return arr;
	}
}
