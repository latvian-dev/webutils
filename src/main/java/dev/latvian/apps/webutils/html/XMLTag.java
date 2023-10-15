package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.net.FileResponse;
import dev.latvian.apps.webutils.net.MimeType;
import dev.latvian.apps.webutils.net.Response;
import io.javalin.http.HttpStatus;

import java.nio.charset.StandardCharsets;

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
	public Response asResponse() {
		return asResponse(HttpStatus.OK, true);
	}

	@Override
	public Response asResponse(HttpStatus status, boolean header) {
		return FileResponse.of(status, MimeType.XML_TEXT, toTagString(header).getBytes(StandardCharsets.UTF_8));
	}
}
