package dev.latvian.apps.webutils.json;

import java.util.LinkedHashMap;

public class JSONObject extends LinkedHashMap<String, Object> {
	public static JSONObject of() {
		return new JSONObject();
	}

	public static JSONObject of(String key, Object value) {
		return new JSONObject(1).append(key, value);
	}

	public static JSONObject of(String key1, Object value1, String key2, Object value2) {
		return new JSONObject(2).append(key1, value1).append(key2, value2);
	}

	public static JSONObject of(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
		return new JSONObject(3).append(key1, value1).append(key2, value2).append(key3, value3);
	}

	public JSONObject() {
	}

	public JSONObject(int initialCapacity) {
		super(initialCapacity);
	}

	@Override
	public Object get(Object key) {
		var o = super.get(key);
		return o == JSON.NULL ? null : o;
	}

	public JSONObject append(String key, Object value) {
		put(key, value);
		return this;
	}

	public JSONObject object(String key) {
		return (JSONObject) get(key);
	}

	public JSONArray array(String key) {
		var o = get(key);
		return o instanceof JSONArray a ? a : JSONArray.of(o);
	}

	public String string(String key) {
		return String.valueOf(get(key));
	}

	public Number number(String key) {
		return (Number) get(key);
	}

	public boolean bool(String key) {
		return (boolean) get(key);
	}

	@Override
	public String toString() {
		return JSON.DEFAULT.write(this);
	}
}
