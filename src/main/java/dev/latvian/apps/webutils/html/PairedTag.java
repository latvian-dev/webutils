package dev.latvian.apps.webutils.html;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
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
	public Tag add(Tag content) {
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

		content.parent = this;
		this.content.add(content);
		return this;
	}

	@Override
	public Tag getChild(int index) {
		if (content == null || content.isEmpty()) {
			throw new IndexOutOfBoundsException("This tag does not have any children");
		}

		return content.get(index);
	}

	@Override
	public String getRawContent() {
		if (content != null && !content.isEmpty()) {
			var builder = new StringBuilder();

			for (var tag : content) {
				builder.append(tag.getRawContent());
			}

			return builder.toString();
		}

		return "";
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
	public void write(Writer writer) throws Throwable {
		if (!this.name.isEmpty()) {
			writer.write('<');
			writer.write(this.name);
			TagUtils.writeAttributes(writer, this.attributes);
			writer.write('>');
		}

		if (this.content != null && !this.content.isEmpty()) {
			for (var tag : this.content) {
				tag.write(writer);
			}
		}

		if (!this.name.isEmpty()) {
			writer.write("</");
			writer.write(this.name);
			writer.write('>');
		}
	}
}
