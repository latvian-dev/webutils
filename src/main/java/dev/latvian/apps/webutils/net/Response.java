package dev.latvian.apps.webutils.net;

import io.javalin.http.Context;
import io.javalin.http.Cookie;
import io.javalin.http.HttpStatus;

import java.net.http.HttpRequest;
import java.time.Duration;

public interface Response {
	Response NO_CONTENT = new Response() {
		@Override
		public HttpStatus getStatus() {
			return HttpStatus.NO_CONTENT;
		}

		@Override
		public void result(Context ctx) {
			ctx.status(HttpStatus.NO_CONTENT);
		}
	};

	static Response redirect(String path) {
		return new RedirectResponse(HttpStatus.FOUND, path);
	}

	static Response permanentRedirect(String path) {
		return new RedirectResponse(HttpStatus.MOVED_PERMANENTLY, path);
	}

	HttpStatus getStatus();

	void result(Context ctx);

	default Response withCookie(Cookie cookie) {
		return new ResponseWithCookie(this, cookie);
	}

	default Response withCookie(String key, String value, int maxAge) {
		return withCookie(new Cookie(key, value, "/", maxAge));
	}

	default Response withHeader(String key, String value) {
		return new ResponseWithHeader(this, key, value);
	}

	default Response cache(Duration duration, boolean privateCache) {
		return withHeader("Cache-Control", (privateCache ? "private" : "public") + ", max-age=" + duration.getSeconds());
	}

	default Response cache(Duration duration) {
		return cache(duration, false);
	}

	default Response privateCache(Duration duration) {
		return cache(duration, true);
	}

	default HttpRequest.BodyPublisher bodyPublisher() {
		throw new IllegalStateException("Body publisher not supported");
	}
}