package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.ansi.AnsiComponent;

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
		writer.write(getRawContent());
	}

	@Override
	public void ansi(AnsiComponent component, int depth, int indent) {
		component.append(getRawContent());
	}
}
