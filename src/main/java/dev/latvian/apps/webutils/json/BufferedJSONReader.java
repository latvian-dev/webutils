package dev.latvian.apps.webutils.json;

import java.io.Reader;

public class BufferedJSONReader implements JSONReader {
	private final JSON json;
	private final Reader reader;

	public BufferedJSONReader(JSON json, Reader reader) {
		this.json = json;
		this.reader = reader;
	}

	@Override
	public JSON json() {
		return json;
	}

	@Override
	public char read() {
		try {
			return (char) reader.read();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public char peek() {
		try {
			reader.mark(1);
			int c = reader.read();
			reader.reset();
			return (char) c;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
