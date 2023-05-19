package dev.latvian.apps.webutils;

import dev.latvian.apps.webutils.math.MathUtils;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ImageUtils {
	static BufferedImage resize(BufferedImage image, int width, int height) {
		int w0 = image.getWidth();
		int h0 = image.getHeight();

		if (w0 == width && h0 == height) {
			return image;
		}

		double ratio;

		if (w0 > h0) {
			ratio = width / (float) w0;
		} else {
			ratio = height / (float) h0;
		}

		int w1 = Math.max(1, Math.min(MathUtils.ceil(w0 * ratio), width));
		int h1 = Math.max(1, Math.min(MathUtils.ceil(h0 * ratio), height));

		// App.info("Resizing image from " + w0 + "x" + h0 + " to " + w1 + "x" + h1 + " with ratio " + ratio + " and target size " + width + "x" + height);

		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, (width - w1) / 2, (height - h1) / 2, w1, h1, null);
		g.dispose();
		return resized;
	}

	static OutputStream resize(InputStream in, OutputStream out, String format, int width, int height) throws IOException {
		ImageIO.write(resize(ImageIO.read(in), width, height), format, out);
		return out;
	}
}
