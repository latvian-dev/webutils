package dev.latvian.apps.webutils.html;

import dev.latvian.apps.ansi.ANSI;
import dev.latvian.apps.ansi.color.Color16;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface TagANSI {
	static ANSI of(Tag tag) {
		return of(tag, false);
	}

	static ANSI of(Tag tag, boolean indent) {
		var component = ANSI.empty();
		ansi(tag, component, 0, indent ? 0 : -1);
		return component;
	}

	private static void ansi(Tag tag, ANSI component, int depth, int indent) {
		if (tag instanceof PairedTag paired) {
			paired(paired, component, depth, indent);
		} else if (tag instanceof UnpairedTag unpaired) {
			unpaired(unpaired, component, depth, indent);
		} else {
			component.append(tag.toRawString());
		}
	}

	private static void unpaired(UnpairedTag tag, ANSI component, int depth, int indent) {
		var col = Color16.tree(depth);

		component.append(ANSI.of("<" + tag.name).foreground(col));
		TagANSI.attributes(component, tag.attributes, depth);
		component.append(ANSI.of(" />").foreground(col));
	}

	private static void paired(PairedTag tag, ANSI component, int depth, int indent) {
		var col = Color16.tree(depth);

		if (!tag.name.isEmpty()) {
			component.append(ANSI.of("<" + tag.name).foreground(col));
			TagANSI.attributes(component, tag.attributes, depth);
			component.append(ANSI.of(">").foreground(col));
		}

		boolean shouldIndent = indent >= 0 && !tag.name.isEmpty();

		if (shouldIndent) {
			shouldIndent = false;

			if (tag.content != null && !tag.content.isEmpty()) {
				for (var c : tag.content) {
					if (c instanceof UnpairedTag) {
						shouldIndent = true;
						break;
					}
				}
			}
		}

		if (tag.content != null && !tag.content.isEmpty()) {
			for (var c : tag.content) {
				if (shouldIndent) {
					component.append('\n');
					component.append("  ".repeat(indent + 1));
					ansi(c, component, depth + 1, indent + 1);
				} else {
					ansi(c, component, depth + 1, indent);
				}
			}
		}

		if (shouldIndent) {
			component.append('\n');
			component.append("  ".repeat(indent));
		}

		if (!tag.name.isEmpty()) {
			component.append(ANSI.of("</" + tag.name + ">").foreground(col));
		}
	}

	static void attributes(ANSI component, @Nullable Map<String, String> attributes, int depth) {
		var col = Color16.tree(depth);

		if (attributes != null) {
			var sb = new StringBuilder();

			for (var entry : attributes.entrySet()) {
				component.append(' ');
				component.append(ANSI.lime(entry.getKey()));

				if (!entry.getValue().equals("<NO_VALUE>")) {
					component.append(ANSI.of("=\"").foreground(col));
					TagUtils.encode(sb, entry.getValue());
					component.append(ANSI.yellow(sb.toString()));
					sb.setLength(0);
					component.append(ANSI.of("\"").foreground(col));
				}
			}
		}
	}
}
