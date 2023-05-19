package dev.latvian.apps.webutils.net;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public record FileResponse(HttpStatus status, String contentType, byte[] data) implements Response {
	public static FileResponse of(HttpStatus code, String contentType, byte[] data) {
		return new FileResponse(code, contentType, data);
	}

	public static FileResponse plainText(String text) {
		return of(HttpStatus.OK, MimeType.TEXT, text.getBytes(StandardCharsets.UTF_8));
	}

	public static FileResponse png(BufferedImage img) throws Exception {
		var out = new ByteArrayOutputStream();
		ImageIO.write(img, "PNG", out);
		return of(HttpStatus.OK, MimeType.PNG, out.toByteArray());
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
