package dev.latvian.apps.webutils.html;

public class RawTag extends StringTag {
	public RawTag(String string) {
		super(string);
	}

	@Override
	public String toRawString() {
		return string;
	}

	@Override
	public String toTagString(boolean header) {
		return string;
	}

	@Override
	public void append(StringBuilder builder, boolean header) {
		builder.append(string);
	}

	@Override
	public void appendRaw(StringBuilder builder) {
		builder.append(string);
	}

	@Override
	public StringTag child(String string) {
		return new RawTag(string);
	}
}
