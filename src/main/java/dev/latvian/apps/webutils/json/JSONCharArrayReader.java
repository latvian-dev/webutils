package dev.latvian.apps.webutils.json;

public class JSONCharArrayReader implements JSONReader {
	private final JSON json;
	private final char[] chars;
	private int pos;

	public JSONCharArrayReader(JSON json, char[] chars) {
		this.json = json;
		this.chars = chars;
		this.pos = 0;
		skipWhitespace();
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
	public char peek() {
		return pos == chars.length ? '\0' : chars[pos];
	}
}
