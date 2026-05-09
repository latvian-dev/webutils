package dev.latvian.apps.webutils.html;

import dev.latvian.apps.tinyhttp.content.MimeType;
import dev.latvian.apps.tinyhttp.content.ResponseContent;

import java.io.IOException;
import java.io.OutputStream;

public record TagResponseContent(Tag tag, boolean header) implements ResponseContent {
	@Override
	public String type() {
		return MimeType.HTML;
	}

	@Override
	public void write(OutputStream outputStream) throws IOException {
	}
}
