package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.ansi.Ansi;
import dev.latvian.apps.webutils.ansi.AnsiComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PairedTag extends UnpairedTag {
	public List<Tag> content;

	public PairedTag(String name) {
		super(name);
	}

	@Override
	public Tag addAnd(Tag content) {
		if (content instanceof PairedTag t && t.content != null && t.name.isEmpty()) {
			if (t.attributes != null) {
				if (this.attributes == null) {
					this.attributes = new HashMap<>();
				}

				this.attributes.putAll(t.attributes);
			}

			for (var t1 : t.content) {
				add(t1);
			}

			return this;
		}

		if (this.content == null) {
			this.content = new ArrayList<>();
		}

		this.content.add(content);
		return content;
	}

	@Override
	public Tag getChild(int index) {
		if (content == null || content.isEmpty()) {
			throw new IndexOutOfBoundsException("This tag does not have any children");
		}

		return content.get(index);
	}

	@Override
	public boolean isEmpty() {
		return content == null || content.isEmpty();
	}

	@Override
	public boolean isEmptyRecursively() {
		if (content == null || content.isEmpty()) {
			return true;
		}

		for (var tag : content) {
			if (!tag.isEmptyRecursively()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void replace(Pattern pattern, BiConsumer<Tag, Matcher> replace) {
		if (content != null && !content.isEmpty()) {
			var original = content;
			content = new ArrayList<>(original.size());
			var sb = new StringBuilder();

			for (var tag : original) {
				if (tag instanceof StringTag str) {
					var matcher = pattern.matcher(str.string);

					while (matcher.find()) {
						matcher.appendReplacement(sb, "");
						content.add(str.child(sb.toString()));
						sb.setLength(0);
						replace.accept(this, matcher);
					}

					matcher.appendTail(sb);
					content.add(str.child(sb.toString()));
					sb.setLength(0);
				} else {
					content.add(tag);
					tag.replace(pattern, replace);
				}
			}
		}
	}

	@Override
	public PairedTag copy() {
		var tag = copy0();
		tag.attributes = attributes == null ? null : new LinkedHashMap<>(attributes);

		if (content != null) {
			tag.content = new ArrayList<>(content.size());

			for (var t : content) {
				tag.content.add(t.copy());
			}
		}

		return tag;
	}

	@Override
	protected PairedTag copy0() {
		return new PairedTag(name);
	}

	@Override
	public void append(StringBuilder builder, boolean header) {
		if (!this.name.isEmpty()) {
			builder.append('<');
			builder.append(this.name);
			TagUtils.writeAttributes(builder, this.attributes);
			builder.append('>');
		}

		if (this.content != null && !this.content.isEmpty()) {
			for (var tag : this.content) {
				tag.append(builder, header);
			}
		}

		if (!this.name.isEmpty()) {
			builder.append("</");
			builder.append(this.name);
			builder.append('>');
		}
	}

	@Override
	public void appendRaw(StringBuilder builder) {
		if (content != null && !content.isEmpty()) {
			for (var tag : content) {
				tag.appendRaw(builder);
			}
		}
	}

	@Override
	public void ansi(AnsiComponent component, int depth, int indent) {
		int col = TagUtils.ANSI_COLORS[depth % TagUtils.ANSI_COLORS.length];

		if (!this.name.isEmpty()) {
			component.append(Ansi.of("<" + this.name).color(col));
			TagUtils.ansiAttributes(component, this.attributes, depth);
			component.append(Ansi.of(">").color(col));
		}

		boolean shouldIndent = indent >= 0 && !this.name.isEmpty();

		if (shouldIndent) {
			shouldIndent = false;

			if (this.content != null && !this.content.isEmpty()) {
				for (var tag : this.content) {
					if (tag instanceof UnpairedTag) {
						shouldIndent = true;
						break;
					}
				}
			}
		}

		if (this.content != null && !this.content.isEmpty()) {
			for (var tag : this.content) {
				if (shouldIndent) {
					component.append('\n');
					component.append("  ".repeat(indent + 1));
					tag.ansi(component, depth + 1, indent + 1);
				} else {
					tag.ansi(component, depth + 1, indent);
				}
			}
		}

		if (shouldIndent) {
			component.append('\n');
			component.append("  ".repeat(indent));
		}

		if (!this.name.isEmpty()) {
			component.append(Ansi.of("</" + this.name + ">").color(col));
		}
	}
}
