package dev.latvian.apps.webutils.json;

import java.util.ArrayList;
import java.util.Collection;

public class JSONArray extends ArrayList<Object> {
	public static JSONArray of() {
		return new JSONArray();
	}

	public static JSONArray of(Object value) {
		return new JSONArray(1).append(value);
	}

	public static JSONArray ofArray(Object... values) {
		var a = new JSONArray(values.length);

		for (var v : values) {
			a.append(v);
		}

		return a;
	}

	public static JSONArray ofAll(Iterable<Object> iterable) {
		var a = new JSONArray(iterable instanceof Collection<Object> c ? c.size() : 4);

		for (var v : iterable) {
			a.append(v);
		}

		return a;
	}

	public JSONArray() {
	}

	public JSONArray(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public Object get(int index) {
		var o = super.get(index);
		return o == JSON.NULL ? null : o;
	}

	public JSONArray append(Object value) {
		add(value);
		return this;
	}

	public JSONObject object(int index) {
		return (JSONObject) get(index);
	}

	public JSONArray array(int index) {
		var o = get(index);
		return o instanceof JSONArray a ? a : JSONArray.of(o);
	}

	public String string(int index) {
		return String.valueOf(get(index));
	}

	public Number number(int index) {
		return (Number) get(index);
	}

	public boolean bool(int index) {
		return (boolean) get(index);
	}

	@Override
	public String toString() {
		return JSON.DEFAULT.write(this);
	}
}
