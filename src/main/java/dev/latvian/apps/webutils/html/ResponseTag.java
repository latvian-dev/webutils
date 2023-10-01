package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.net.FileResponse;
import dev.latvian.apps.webutils.net.Response;
import io.javalin.http.HttpStatus;

import java.nio.charset.StandardCharsets;

public interface ResponseTag {
	String getMimeType();

	default Response asResponse() {
		return asResponse(HttpStatus.OK, true);
	}

	default Response asResponse(HttpStatus status, boolean header) {
		return FileResponse.of(status, getMimeType(), ((Tag) this).toTagString(header).getBytes(StandardCharsets.UTF_8));
	}
}
