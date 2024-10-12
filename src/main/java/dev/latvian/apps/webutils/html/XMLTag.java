package dev.latvian.apps.webutils.html;

import dev.latvian.apps.tinyserver.content.MimeType;
import dev.latvian.apps.tinyserver.http.response.HTTPResponse;
import dev.latvian.apps.tinyserver.http.response.HTTPStatus;

public class XMLTag extends PairedTag {
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
	public HTTPResponse asResponse() {
		return asResponse(HTTPStatus.OK, true);
	}

	@Override
	public HTTPResponse asResponse(HTTPStatus status, boolean header) {
		return status.content(toTagString(header), MimeType.XML_TEXT).gzip();
	}
}
