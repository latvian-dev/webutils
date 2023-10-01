package dev.latvian.apps.webutils.html;

public class StringTag implements Tag {
	public final String string;

	public StringTag(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return string;
	}

	@Override
	public boolean isEmpty() {
		return string.isEmpty();
	}

	public StringTag child(String string) {
		return new StringTag(string);
	}

	@Override
	public void append(StringBuilder builder, boolean header) {
		TagUtils.encode(builder, string);
	}

	@Override
	public void appendRaw(StringBuilder builder) {
		TagUtils.encode(builder, string);
	}
}
