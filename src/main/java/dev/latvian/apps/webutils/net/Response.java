package dev.latvian.apps.webutils.net;

import io.javalin.http.Context;
import io.javalin.http.Cookie;
import io.javalin.http.HttpStatus;

@FunctionalInterface
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

	default HttpStatus getStatus() {
		return HttpStatus.OK;
	}

	default Response withCookie(Cookie cookie) {
		return new ResponseWithCookie(this, cookie);
	}

	default Response withCookie(String key, String value, int maxAge) {
		return withCookie(new Cookie(key, value, "/", maxAge));
	}

	default Response withHeader(String key, String value) {
		return new ResponseWithHeader(this, key, value);
	}

	void result(Context ctx);
}