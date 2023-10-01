package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.misc.StringParser;
import dev.latvian.apps.webutils.misc.StringToken;

import java.util.regex.Pattern;

public class XMLParser {
	public static final Pattern CLEANUP_PATTERN = Pattern.compile("<\\?xml.*\\?>|<!--.*-->");
	public static final StringToken OPEN = StringToken.of("<");
	public static final StringToken CLOSE = StringToken.of(">");
	public static final StringToken SLASH = StringToken.of("/");
	public static final StringToken PAIRED_END = StringToken.of("</");
	public static final StringToken UNPAIRED_END = StringToken.of("/>");
	public static final StringToken QUOTE = StringToken.of("\"");
	public static final StringToken EQ = StringToken.of("=");

	public static final StringToken CLOSE_OR_SPACE = CLOSE.or(StringToken.SPACE);
	public static final StringToken EQ_OR_SPACE = EQ.or(StringToken.SPACE);

	public static XMLTag parse(String input, boolean preserveWhitespace) {
		var reader = new StringParser(CLEANUP_PATTERN.matcher(input).replaceAll(""));
		reader.skipSpace();
		reader.readOrError(OPEN);
		return (XMLTag) new XMLParser(reader, preserveWhitespace).readTag(true);
	}

	private final StringParser parser;
	private final boolean preserveWhitespace;

	private XMLParser(StringParser parser, boolean preserveWhitespace) {
		this.parser = parser;
		this.preserveWhitespace = preserveWhitespace;
	}

	private PairedTag readTag(boolean root) {
		if (root) {
			parser.skipSpace();
		}

		var tagName = parser.readUntil(CLOSE_OR_SPACE, false);
		var tag = root ? new XMLTag(tagName) : new PairedTag(tagName);

		if (!parser.ifRead(CLOSE)) {
			while (true) {
				parser.skipSpace();

				if (parser.ifRead(UNPAIRED_END)) {
					return tag;
				} else if (parser.ifRead(CLOSE)) {
					break;
				} else {
					var name = parser.readUntil(EQ_OR_SPACE, false);
					parser.skipSpace();
					parser.readOrError(EQ);
					parser.skipSpace();
					parser.readOrError(QUOTE);
					tag.attr(name, parser.readUntil(QUOTE, true));
				}
			}
		}

		var current = new StringBuilder();

		while (true) {
			var str = parser.readUntil(OPEN, false);

			if (!preserveWhitespace) {
				str = str.trim();
			}

			if (!str.isEmpty()) {
				current.append(str);
			}

			if (parser.ifRead(OPEN)) {
				if (parser.ifRead(SLASH)) {
					parser.skipSpace();
					var endName = parser.readUntil(CLOSE_OR_SPACE, false);

					if (!endName.equals(tagName)) {
						throw new IllegalStateException("Tag mismatch: " + tagName + " != " + endName);
					}

					parser.skipSpace();
					parser.readOrError(CLOSE);
					var str1 = current.toString();

					if (!preserveWhitespace) {
						str1 = str1.trim();
					}

					tag.raw(str1);
					return tag;
				} else {
					var str1 = current.toString();

					if (!preserveWhitespace) {
						str1 = str1.trim();
					}

					tag.raw(str1);
					tag.add(readTag(false));
					current.setLength(0);
				}
			} else {
				current.append(parser.read());
			}
		}
	}
}
