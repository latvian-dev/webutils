package dev.latvian.apps.webutils.misc;

public class StringParser {
	public final char[] input;
	public final int end;
	public int cursor;

	public StringParser(char[] input, int cursor, int end) {
		this.input = input;
		this.cursor = cursor;
		this.end = end;
	}

	public StringParser(String input) {
		this(input.toCharArray(), 0, input.length());
	}

	public StringParser copy() {
		return new StringParser(input, cursor, end);
	}

	public char peek(int offset) {
		int c = cursor + offset;
		return c >= end || c < 0 ? 0 : input[c];
	}

	public boolean isSpace(int off) {
		var c = peek(off);
		return c != 0 && c <= ' ';
	}

	public char peek() {
		return peek(0);
	}

	public char previous() {
		return peek(-1);
	}

	public boolean escaped() {
		return previous() == '\\';
	}

	public char read() {
		return cursor >= end ? 0 : input[cursor++];
	}

	public boolean ifRead(StringToken token) {
		int i = token.test(this);

		if (i > 0) {
			cursor += i;
			return true;
		}

		return false;
	}

	public int skipSpace() {
		int skipped = 0;

		while (true) {
			char c = peek();

			if (c == 0 || c > ' ') {
				break;
			} else {
				read();
				skipped++;
			}
		}

		return skipped;
	}

	public int countSpace() {
		int skipped = 0;

		while (true) {
			char c = peek(skipped);

			if (c == 0 || c > ' ') {
				return skipped;
			} else {
				skipped++;
			}
		}
	}

	public int find(StringToken token) {
		int index = -1;
		int pos = 0;

		return index;
	}

	public void readOrError(StringToken token) {
		if (!ifRead(token)) {
			throw new IllegalStateException("Expected '" + token + "'");
		}
	}

	public String readUntil(StringToken token, boolean consume) {
		var sb = new StringBuilder();
		int consumed = 0;

		while (true) {
			char c = peek();

			if (c == 0 || !escaped() && (consumed = token.test(this)) > 0) {
				break;
			}

			sb.append(c);
			read();
		}

		if (consume) {
			cursor += consumed;
		}

		return sb.toString();
	}

	public String remaining() {
		return new String(input, cursor, input.length - cursor);
	}

	public String fullString() {
		return new String(input);
	}

	@Override
	public String toString() {
		return fullString();
	}
}
