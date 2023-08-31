package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.net.FileResponse;
import dev.latvian.apps.webutils.net.Response;
import io.javalin.http.HttpStatus;

import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class XMLTag extends PairedTag {
	public XMLTag(String tag) {
		super(tag);
	}

	@Override
	public Tag getRoot() {
		return this;
	}

	@Override
	public void write(Writer writer) throws Throwable {
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		super.write(writer);
	}

	@Override
	public Response asResponse(HttpStatus status) {
		return FileResponse.of(status, "text/xml; charset=utf-8", toString().getBytes(StandardCharsets.UTF_8));
	}
}
