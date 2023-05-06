package dev.latvian.apps.webutils.net;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

record ResponseWithHeader(Response original, String key, String value) implements Response {
	@Override
	public HttpStatus getStatus() {
		return original.getStatus();
	}

	@Override
	public void result(Context ctx) throws Exception {
		original.result(ctx);
		ctx.header(key, value);
	}
}
