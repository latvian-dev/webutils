package dev.latvian.apps.webutils.json;

import java.io.IOException;
import java.io.Writer;

public interface JSONAdapter<T> {
	T read(JSONReader reader);

	void write(JSON json, Writer writer, T value, int depth, boolean pretty) throws IOException;
}
