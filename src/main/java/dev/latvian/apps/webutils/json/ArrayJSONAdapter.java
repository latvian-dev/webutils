package dev.latvian.apps.webutils.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Collection;

public class ArrayJSONAdapter implements JSONAdapter<Object> {
	private final JSON json;
	private final Class<?> type;
	private Object emptyArray;

	public ArrayJSONAdapter(JSON json, Class<?> type) {
		this.json = json;
		this.type = type;
		this.emptyArray = null;
	}

	@Override
	public Object adapt(Object jsonValue) {
		if (jsonValue instanceof Iterable<?> itr) {
			int size;

			if (itr instanceof Collection<?> c) {
				size = c.size();
			} else {
				size = 0;

				for (var ignored : itr) {
					size++;
				}
			}

			if (size == 0) {
				if (emptyArray == null) {
					emptyArray = Array.newInstance(type, 0);
				}

				return emptyArray;
			}

			var arr = Array.newInstance(type, size);

			int i = 0;

			for (var value : itr) {
				Array.set(arr, i, json.adapt(value, type));
				i++;
			}

			return arr;
		} else {
			throw new IllegalArgumentException("Expected collection, got " + jsonValue.getClass().getName());
		}
	}

	@Override
	public void write(JSON json, Writer writer, Object value, int depth, boolean pretty) throws IOException {
		var arr = new JSONArray();
		var len = Array.getLength(value);

		for (var i = 0; i < len; i++) {
			arr.add(Array.get(value, i));
		}

		json.write(writer, arr, depth, pretty);
	}
}
