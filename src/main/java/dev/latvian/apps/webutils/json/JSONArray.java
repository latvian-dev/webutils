package dev.latvian.apps.webutils.json;

import java.util.ArrayList;

public class JSONArray extends ArrayList<Object> {
	public static JSONArray of() {
		return new JSONArray();
	}

	public static JSONArray of(Object value) {
		return new JSONArray(1).append(value);
	}

	public static JSONArray ofAll(Object... values) {
		var a = new JSONArray(values.length);

		for (var v : values) {
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
		return (JSONArray) get(index);
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
}
