package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.net.MimeType;

public class XMLTag extends PairedTag implements ResponseTag {
	public XMLTag(String tag) {
		super(tag);
	}

	@Override
	public void append(StringBuilder builder, boolean header) {
		if (header) {
			builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		}

		super.append(builder, header);
	}

	@Override
	protected XMLTag copy0() {
		return new XMLTag(name);
	}

	@Override
	public String getMimeType() {
		return MimeType.XML_TEXT;
	}
}
