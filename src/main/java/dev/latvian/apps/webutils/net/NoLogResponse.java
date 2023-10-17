package dev.latvian.apps.webutils.net;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.net.http.HttpRequest;

record NoLogResponse(Response original) implements Response {
	@Override
	public HttpStatus getStatus() {
		return original.getStatus();
	}

	@Override
	public void result(Context ctx) {
		original.result(ctx);
		ctx.attribute("nolog", true);
	}

	@Override
	public HttpRequest.BodyPublisher bodyPublisher() {
		return original.bodyPublisher();
	}
}
