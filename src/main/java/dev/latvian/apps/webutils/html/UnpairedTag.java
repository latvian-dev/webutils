package dev.latvian.apps.webutils.html;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

public class UnpairedTag extends Tag {
	public final String name;
	protected Map<String, String> attributes;

	public UnpairedTag(String name) {
		this.name = name;
		this.attributes = null;
	}

	@Override
	@ApiStatus.Internal
	public Tag attr(String key, Object value) {
		if (key.isEmpty()) {
			return this;
		}

		if (this.attributes == null) {
			this.attributes = new LinkedHashMap<>();
		}

		this.attributes.put(key, String.valueOf(value));
		return this;
	}

	@Override
	@Nullable
	public String getAttr(String key) {
		return attributes == null ? null : attributes.get(key);
	}

	@Override
	public void write(Writer writer) throws Throwable {
		writer.write('<');
		writer.write(this.name);
		TagUtils.writeAttributes(writer, this.attributes);
		writer.write(" />");
	}
}
