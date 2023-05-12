package dev.latvian.apps.webutils.net;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

record RedirectResponse(HttpStatus status, String location) implements Response {
	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public void result(Context ctx) {
		ctx.redirect(location, status);
	}
}