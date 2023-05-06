package dev.latvian.apps.webutils.data;

public record Color4i(int r, int g, int b, int a) {
	public static final Color4i BLACK = new Color4i(0, 0, 0);
	public static final Color4i WHITE = new Color4i(255, 255, 255);

	public static Color4i ofARGB(int argb) {
		int a = (argb >> 24) & 0xFF;
		int r = (argb >> 16) & 0xFF;
		int g = (argb >> 8) & 0xFF;
		int b = argb & 0xFF;
		return new Color4i(r, g, b, a);
	}

	public static Color4i ofRGB(int rgb) {
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;
		return new Color4i(r, g, b, 255);
	}

	public Color4i(int r, int g, int b) {
		this(r, g, b, 255);
	}

	public Color4f to4f() {
		if (this == BLACK) {
			return Color4f.BLACK;
		} else if (this == WHITE) {
			return Color4f.WHITE;
		} else {
			return new Color4f(r / 255F, g / 255F, b / 255F, a / 255F);
		}
	}

	public int argb() {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public int rgb() {
		return (r << 16) | (g << 8) | b;
	}

	@Override
	public int hashCode() {
		return argb();
	}

	@Override
	public String toString() {
		return String.format("%08X", argb());
	}
}
