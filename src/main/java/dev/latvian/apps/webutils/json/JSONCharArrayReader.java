package dev.latvian.apps.webutils.json;

public class JSONCharArrayReader implements JSONReader {
	private final JSON json;
	private final char[] chars;
	private int pos;

	public JSONCharArrayReader(JSON json, char[] chars) {
		this.json = json;
		this.chars = chars;
		this.pos = 0;
	}

	@Override
	public JSON json() {
		return json;
	}

	@Override
	public char read() {
		if (pos == chars.length) {
			throw new IllegalStateException("EOL");
		}

		return chars[pos++];
	}

	@Override
	public void expect(char c) {
		if (read() != c) {
			throw new IllegalStateException("Expected " + c);
		}
	}

	@Override
	public char peek() {
		return pos == chars.length ? '\0' : chars[pos];
	}
}
