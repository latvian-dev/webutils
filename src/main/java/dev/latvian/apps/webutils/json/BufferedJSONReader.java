package dev.latvian.apps.webutils.json;

import java.io.Reader;

public class BufferedJSONReader implements JSONReader {
	private final JSON json;
	private final Reader reader;
	private boolean move;
	private char peek;

	public BufferedJSONReader(JSON json, Reader reader) {
		this.json = json;
		this.reader = reader;
		this.move = true;
		this.peek = 0;
	}

	@Override
	public JSON json() {
		return json;
	}

	@Override
	public char read() {
		move = true;
		return peek();
	}

	@Override
	public char peek() {
		if (move) {
			move = false;

			try {
				peek = (char) reader.read();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}

		return peek;
	}
}
