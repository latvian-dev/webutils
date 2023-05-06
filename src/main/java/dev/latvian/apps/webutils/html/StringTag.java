package dev.latvian.apps.webutils.html;

import java.io.Writer;

public class StringTag extends Tag {
	public final String string;

	public StringTag(String string) {
		this.string = string;
	}

	@Override
	public String getRawContent() {
		return TagUtils.encode(string);
	}

	@Override
	public boolean isEmpty() {
		return string.isEmpty();
	}

	@Override
	public boolean isEmptyRecursively() {
		return string.isEmpty();
	}

	public StringTag child(String string) {
		return new StringTag(string);
	}

	@Override
	public void write(Writer writer) throws Throwable {
		TagUtils.encode(writer, string);
	}
}
