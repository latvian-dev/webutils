package dev.latvian.apps.webutils.net;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public record FileResponse(HttpStatus status, String contentType, byte[] data) implements Response {
	public static Response of(HttpStatus code, String contentType, byte[] data) {
		return new FileResponse(code, contentType, data);
	}

	public static Response plainText(String text) {
		return of(HttpStatus.OK, "text/plain; charset=utf-8", text.getBytes(StandardCharsets.UTF_8));
	}

	public static Response image(BufferedImage img) throws Exception {
		var out = new ByteArrayOutputStream();
		ImageIO.write(img, "PNG", out);
		return of(HttpStatus.OK, "image/png", out.toByteArray());
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public void result(Context ctx) {
		ctx.status(status);
		ctx.contentType(contentType);
		ctx.result(data);
	}
}
