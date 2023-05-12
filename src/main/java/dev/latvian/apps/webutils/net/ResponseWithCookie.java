package dev.latvian.apps.webutils.net;

import io.javalin.http.Context;
import io.javalin.http.Cookie;
import io.javalin.http.HttpStatus;

record ResponseWithCookie(Response original, Cookie cookie) implements Response {
	@Override
	public HttpStatus getStatus() {
		return original.getStatus();
	}

	@Override
	public void result(Context ctx) {
		original.result(ctx);
		ctx.cookie(cookie);
	}
}
