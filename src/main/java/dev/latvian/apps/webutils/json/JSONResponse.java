package dev.latvian.apps.webutils.json;

import dev.latvian.apps.webutils.net.FileResponse;
import dev.latvian.apps.webutils.net.Response;
import io.javalin.http.HttpStatus;

import java.nio.charset.StandardCharsets;

public interface JSONResponse {
	Response SUCCESS = of(HttpStatus.OK, JSONObject.of("success", true));

	static Response of(HttpStatus status, Object json) {
		return FileResponse.of(status, "application/json; charset=utf-8", JSON.DEFAULT.write(json).getBytes(StandardCharsets.UTF_8));
	}

	static Response of(Object json) {
		return of(HttpStatus.OK, json);
	}
}