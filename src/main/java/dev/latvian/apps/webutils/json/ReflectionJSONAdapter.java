package dev.latvian.apps.webutils.json;

import java.io.IOException;
import java.io.Writer;

public class ReflectionJSONAdapter implements JSONAdapter<Object> {
	private final Class<?> type;

	public ReflectionJSONAdapter(Class<?> type) {
		this.type = type;
	}

	@Override
	public Object read(JSONReader reader) {
		throw new UnsupportedOperationException("Reflection JSON for type '" + type.getName() + "' not supported yet");
	}

	@Override
	public void write(JSON json, Writer writer, Object value, int depth, boolean pretty) throws IOException {
		throw new UnsupportedOperationException("Reflection JSON for type '" + type.getName() + "' not supported yet");
	}
}
