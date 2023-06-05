package dev.latvian.apps.webutils.net;

import dev.latvian.apps.webutils.html.Tag;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;

public class UserError extends HttpResponseException {
	public final Tag displayMessage;

	public UserError(HttpStatus status, String message, Tag displayMessage) {
		super(status.getCode(), message);
		this.displayMessage = displayMessage;
	}

	public UserError(String message) {
		this(HttpStatus.BAD_REQUEST, message, null);
	}
}
