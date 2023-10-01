package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.ansi.Ansi;
import dev.latvian.apps.webutils.ansi.AnsiComponent;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class UnpairedTag implements Tag {
	public final String name;
	protected Map<String, String> attributes;

	public UnpairedTag(String name) {
		this.name = name;
		this.attributes = null;
	}

	@Override
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
	public UnpairedTag copy() {
		var tag = copy0();
		tag.attributes = attributes == null ? null : new LinkedHashMap<>(attributes);
		return tag;
	}

	protected UnpairedTag copy0() {
		return new UnpairedTag(name);
	}

	@Override
	public void append(StringBuilder builder, boolean header) {
		builder.append('<');
		builder.append(this.name);
		TagUtils.writeAttributes(builder, this.attributes);
		builder.append(" />");
	}

	@Override
	public void appendRaw(StringBuilder builder) {
	}

	@Override
	public void ansi(AnsiComponent component, int depth, int indent) {
		int col = TagUtils.ANSI_COLORS[depth % TagUtils.ANSI_COLORS.length];

		component.append(Ansi.of("<" + this.name).color(col));
		TagUtils.ansiAttributes(component, this.attributes, depth);
		component.append(Ansi.of(" />").color(col));
	}
}
