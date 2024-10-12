package dev.latvian.apps.webutils.html;

import dev.latvian.apps.tinyserver.http.response.HTTPStatus;
import org.jetbrains.annotations.Nullable;

public class UserError extends RuntimeException {
	public final HTTPStatus status;
	public final Tag displayMessage;

	public UserError(HTTPStatus status, String message, @Nullable Tag displayMessage) {
		super(message);
		this.status = status;
		this.displayMessage = displayMessage;
	}

	public UserError(HTTPStatus status, String message) {
		this(status, message, null);
	}
}
