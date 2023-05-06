package dev.latvian.apps.webutils.html;

import java.io.Writer;

public class RawTag extends StringTag {
	public RawTag(String string) {
		super(string);
	}

	@Override
	public String getRawContent() {
		return string;
	}

	@Override
	public StringTag child(String string) {
		return new RawTag(string);
	}

	@Override
	public void write(Writer writer) throws Throwable {
		writer.write(string);
	}
}
