package dev.latvian.apps.webutils.net;

import dev.latvian.apps.webutils.CodingUtils;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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

	public static FileResponse formData(Map<String, Object> map) {
		return of(HttpStatus.OK, MimeType.FORM, CodingUtils.formData(map).getBytes(StandardCharsets.UTF_8));
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

	@Override
	public HttpRequest.BodyPublisher bodyPublisher() {
		return HttpRequest.BodyPublishers.ofByteArray(data);
	}
}
