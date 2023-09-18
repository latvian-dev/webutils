package dev.latvian.apps.webutils.html;

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
}
