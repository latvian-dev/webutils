package dev.latvian.apps.webutils.json;

import dev.latvian.apps.webutils.MiscUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JSONArray extends ArrayList<Object> {
	public static JSONArray of() {
		return new JSONArray(4);
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

	private JSONArray(int initialCapacity) {
		super(initialCapacity);
	}

	JSONArray(String key) {
	}

	public List<JSONObject> ofObjects() {
		return MiscUtils.cast(this);
	}

	public List<JSONArray> ofArrays() {
		return MiscUtils.cast(this);
	}

	public List<String> ofStrings() {
		return MiscUtils.cast(this);
	}

	public List<Number> ofNumbers() {
		return MiscUtils.cast(this);
	}

	public List<Boolean> ofBooleans() {
		return MiscUtils.cast(this);
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

	public JSONObject asObject(int key) {
		return (JSONObject) get(key);
	}

	public JSONObject addObject() {
		var o = JSONObject.of();
		add(o);
		return o;
	}

	public JSONArray asArray(int key) {
		var o = get(key);
		return o instanceof JSONArray a ? a : JSONArray.of(o);
	}

	public JSONArray addArray() {
		var a = JSONArray.of();
		add(a);
		return a;
	}

	public String asString(int key) {
		return String.valueOf(get(key));
	}

	public Number asNumber(int key) {
		return (Number) get(key);
	}

	public Number asNumber(int key, Number def) {
		var o = get(key);

		if (o == null) {
			return def;
		} else if (o instanceof Number n) {
			return n;
		} else if (o instanceof CharSequence) {
			try {
				return Double.parseDouble(o.toString());
			} catch (NumberFormatException e) {
				return def;
			}
		} else {
			return def;
		}
	}

	public int asInt(int key) {
		return asInt(key, 0);
	}

	public int asInt(int key, int def) {
		return asNumber(key, def).intValue();
	}

	public long asLong(int key) {
		return asLong(key, 0L);
	}

	public long asLong(int key, long def) {
		return asNumber(key, def).longValue();
	}

	public double asDouble(int key) {
		return asDouble(key, 0D);
	}

	public double asDouble(int key, double def) {
		return asNumber(key, def).doubleValue();
	}

	public boolean asBoolean(int key) {
		return (boolean) get(key);
	}

	@Override
	public String toString() {
		return JSON.DEFAULT.write(this);
	}
}
