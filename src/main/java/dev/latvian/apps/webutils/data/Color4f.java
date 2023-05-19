package dev.latvian.apps.webutils.data;

public record Color4f(float r, float g, float b, float a) {
	public static final Color4f TRANSPARENT = new Color4f(0F, 0F, 0F, 0F);
	public static final Color4f BLACK = new Color4f(0F, 0F, 0F, 1F);
	public static final Color4f WHITE = new Color4f(1F, 1F, 1F, 1F);

	private static final Color4f[] CHART_COLORS = new Color4f[24];

	static {
		float off = 4F / 6F;

		for (int i = 0; i < 6; i++) {
			float f = off - i / 6F;
			CHART_COLORS[i] = ofHSB(f, 0.8F, 0.8F, 1F);
			CHART_COLORS[i + 6] = ofHSB(f + 2F / 24F, 0.8F, 0.8F, 1F);
			CHART_COLORS[i + 12] = ofHSB(f + 1F / 24F, 0.8F, 0.8F, 1F);
			CHART_COLORS[i + 18] = ofHSB(f + 3F / 24F, 0.8F, 0.8F, 1F);
		}
	}

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

	public static Color4f getChartColor(int id) {
		int i = id % CHART_COLORS.length;
		return CHART_COLORS[i < 0 ? (i + CHART_COLORS.length) : i];
	}

	public Color4f(float r, float g, float b) {
		this(r, g, b, 1F);
	}

	public Color4i to4i() {
		if (this == BLACK) {
			return Color4i.BLACK;
		} else if (this == WHITE) {
			return Color4i.WHITE;
		} else {
			return new Color4i((int) (r * 255F), (int) (g * 255F), (int) (b * 255F), (int) (a * 255F));
		}
	}

	public int argb() {
		return (int) (a * 255F) << 24 | (int) (r * 255F) << 16 | (int) (g * 255F) << 8 | (int) (b * 255F);
	}

	public int rgb() {
		return (int) (r * 255F) << 16 | (int) (g * 255F) << 8 | (int) (b * 255F);
	}

	@Override
	public int hashCode() {
		return argb();
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || obj instanceof Color4f c && argb() == c.argb();
	}

	@Override
	public String toString() {
		return String.format("%08X", argb());
	}
}
