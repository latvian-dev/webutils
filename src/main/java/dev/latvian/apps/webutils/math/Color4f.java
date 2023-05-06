package dev.latvian.apps.webutils.math;

public record Color4f(float r, float g, float b, float a) {
	public static final Color4f TRANSPARENT = new Color4f(0F, 0F, 0F, 0F);
	public static final Color4f BLACK = new Color4f(0F, 0F, 0F, 1F);
	public static final Color4f WHITE = new Color4f(1F, 1F, 1F, 1F);

	public static Color4f ofHSB(float h, float s, float b, float a) {
		int i = (int) (h * 6F) % 6;
		float h6 = h * 6F - (float) i;
		float p = b * (1F - s);
		float q = b * (1F - s * h6);
		float t = b * (1F - s * (1F - h6));

		return switch (i) {
			case 0 -> new Color4f(b, t, p, a);
			case 1 -> new Color4f(q, b, p, a);
			case 2 -> new Color4f(p, b, t, a);
			case 3 -> new Color4f(p, q, b, a);
			case 4 -> new Color4f(t, p, b, a);
			case 5 -> new Color4f(b, p, q, a);
			default -> Color4f.BLACK;
		};
	}

	public Color4f(float r, float g, float b) {
		this(r, g, b, 1F);
	}
}
