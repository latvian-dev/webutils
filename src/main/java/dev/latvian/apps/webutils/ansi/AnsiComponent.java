package dev.latvian.apps.webutils.ansi;

import java.util.ArrayList;
import java.util.List;

public class AnsiComponent implements AnsiAppendable {
	public AnsiComponent parent = null;
	public String text;
	private int color = -1;
	private int bgColor = -1;
	private boolean bold = false;
	private boolean italic = false;
	private boolean underline = false;
	private boolean blink = false;
	private boolean reverse = false;
	private boolean hidden = false;
	private boolean strikethrough = false;
	private List<AnsiAppendable> children;

	public AnsiComponent(Object text) {
		this.text = String.valueOf(text);
	}

	private static boolean appendMode(StringBuilder builder, boolean semi, boolean prevMode, boolean mode, char code, char resetCode, AnsiContext ctx, String debug) {
		if (prevMode != mode) {
			if (semi) {
				builder.append(';');
			}

			if (ctx.debug()) {
				if (mode) {
					builder.append(debug);
				} else {
					builder.append('!');
					builder.append(debug);
				}
			} else if (mode) {
				builder.append(code);
			} else {
				builder.append('2');
				builder.append(resetCode);
			}

			return true;
		}

		return semi;
	}

	private static boolean appendColor(StringBuilder builder, boolean semi, int prevColor, int color, char code, String brightCode, AnsiContext ctx) {
		if (prevColor != color) {
			if (semi) {
				builder.append(';');
			}

			if (ctx.debug()) {
				if (color == -1) {
					builder.append("!color");
				} else if (color >= 0 && color < 16) {
					builder.append(Ansi.COLOR_NAMES[color]);
				} else {
					builder.append("#%06X".formatted(color & 0xFFFFFF));
				}

				return true;
			}

			int a = (color >> 24) & 0xFF;
			int r = (color >> 16) & 0xFF;
			int g = (color >> 8) & 0xFF;
			int b = color & 0xFF;

			if (color == -1) {
				builder.append(code);
				builder.append('9');
			} else if (a == 255) {
				builder.append(code);
				builder.append("8;2;");
				builder.append(r);
				builder.append(';');
				builder.append(g);
				builder.append(';');
				builder.append(b);
			} else if (color < 8) {
				builder.append(code);
				builder.append((char) (color + '0'));
			} else if (color < 16) {
				builder.append(brightCode);
				builder.append((char) (color - 8 + '0'));
			} else {
				builder.append(code);
				builder.append("8;5;");
				builder.append(color);
			}

			return true;
		}

		return semi;
	}

	@Override
	public void appendAnsi(StringBuilder builder, AnsiContext ctx) {
		if (ctx.unformatted()) {
			if (!text.isEmpty()) {
				builder.append(text);
			}

			if (children != null) {
				for (var c : children) {
					c.appendAnsi(builder, ctx);
				}
			}

			return;
		}

		int pColor = parent == null ? -1 : parent.color;
		int pBgColor = parent == null ? -1 : parent.bgColor;
		boolean pBold = parent != null && parent.bold;
		boolean pItalic = parent != null && parent.italic;
		boolean pUnderline = parent != null && parent.underline;
		boolean pBlink = parent != null && parent.blink;
		boolean pReverse = parent != null && parent.reverse;
		boolean pHidden = parent != null && parent.hidden;
		boolean pStrikethrough = parent != null && parent.strikethrough;
		boolean changed = color != pColor || bgColor != pBgColor || bold != pBold || italic != pItalic || underline != pUnderline || blink != pBlink || reverse != pReverse || hidden != pHidden || strikethrough != pStrikethrough;

		if (changed) {
			builder.append(Ansi.CODE);

			if (ctx.debug()) {
				builder.append("[9").append((ctx.index++ & 3) + 1).append("m§");
			}

			builder.append('[');
			boolean semi = false;
			semi = appendColor(builder, semi, pColor, color, '3', "9", ctx);
			semi = appendColor(builder, semi, pBgColor, bgColor, '4', "10", ctx);
			semi = appendMode(builder, semi, pBold, bold, '1', '2', ctx, "bold");
			semi = appendMode(builder, semi, pItalic, italic, '3', '3', ctx, "italic");
			semi = appendMode(builder, semi, pUnderline, underline, '4', '4', ctx, "underline");
			semi = appendMode(builder, semi, pBlink, blink, '5', '5', ctx, "blink");
			semi = appendMode(builder, semi, pReverse, reverse, '7', '7', ctx, "reverse");
			semi = appendMode(builder, semi, pHidden, hidden, '8', '8', ctx, "hidden");
			semi = appendMode(builder, semi, pStrikethrough, strikethrough, '9', '9', ctx, "strikethrough");

			if (ctx.debug()) {
				builder.append(']');
				builder.append(Ansi.CODE);
				builder.append(AnsiCode.RESET.code);
			} else {
				builder.append('m');
			}
		}

		if (!text.isEmpty()) {
			builder.append(text);
		}

		if (children != null) {
			for (var c : children) {
				c.appendAnsi(builder, ctx);
			}
		}

		if (changed) {
			builder.append(Ansi.CODE);

			if (ctx.debug()) {
				builder.append("[9").append((ctx.index++ & 3) + 1).append("m§");
			}

			builder.append('[');
			boolean semi = false;
			semi = appendColor(builder, semi, color, pColor, '3', "9", ctx);
			semi = appendColor(builder, semi, bgColor, pBgColor, '4', "10", ctx);
			semi = appendMode(builder, semi, bold, pBold, '1', '2', ctx, "bold");
			semi = appendMode(builder, semi, italic, pItalic, '3', '3', ctx, "italic");
			semi = appendMode(builder, semi, underline, pUnderline, '4', '4', ctx, "underline");
			semi = appendMode(builder, semi, blink, pBlink, '5', '5', ctx, "blink");
			semi = appendMode(builder, semi, reverse, pReverse, '7', '7', ctx, "reverse");
			semi = appendMode(builder, semi, hidden, pHidden, '8', '8', ctx, "hidden");
			semi = appendMode(builder, semi, strikethrough, pStrikethrough, '9', '9', ctx, "strikethrough");

			if (ctx.debug()) {
				builder.append(']');
				builder.append(Ansi.CODE);
				builder.append(AnsiCode.RESET.code);
			} else {
				builder.append('m');
			}
		}
	}

	@Override
	public String toString() {
		return toAnsiString();
	}

	public AnsiComponent end() {
		return parent;
	}

	public AnsiComponent append(Object text) {
		var c = text instanceof AnsiComponent ac ? ac : new AnsiComponent(text);
		c.parent = this;

		if (children == null) {
			children = new ArrayList<>(1);
		}

		if (c.color == -1) {
			c.color = color;
		}

		if (c.bgColor == -1) {
			c.bgColor = bgColor;
		}

		c.bold |= bold;
		c.italic |= italic;
		c.underline |= underline;
		c.blink |= blink;
		c.reverse |= reverse;
		c.hidden |= hidden;
		c.strikethrough |= strikethrough;
		children.add(c);
		return this;
	}

	public AnsiComponent bold() {
		bold = true;
		return this;
	}

	public AnsiComponent italic() {
		italic = true;
		return this;
	}

	public AnsiComponent underline() {
		underline = true;
		return this;
	}

	public AnsiComponent blink() {
		blink = true;
		return this;
	}

	public AnsiComponent reverse() {
		reverse = true;
		return this;
	}

	public AnsiComponent hidden() {
		hidden = true;
		return this;
	}

	public AnsiComponent strikethrough() {
		strikethrough = true;
		return this;
	}

	public AnsiComponent color(int color) {
		this.color = color;
		return this;
	}

	public AnsiComponent rgb(int color) {
		return color(0xFF000000 | color);
	}

	public AnsiComponent black() {
		return color(0);
	}

	public AnsiComponent darkRed() {
		return color(1);
	}

	public AnsiComponent green() {
		return color(2);
	}

	public AnsiComponent orange() {
		return color(3);
	}

	public AnsiComponent navy() {
		return color(4);
	}

	public AnsiComponent purple() {
		return color(5);
	}

	public AnsiComponent teal() {
		return color(6);
	}

	public AnsiComponent lightGray() {
		return color(7);
	}

	public AnsiComponent darkGray() {
		return color(8);
	}

	public AnsiComponent red() {
		return color(9);
	}

	public AnsiComponent lime() {
		return color(10);
	}

	public AnsiComponent yellow() {
		return color(11);
	}

	public AnsiComponent blue() {
		return color(12);
	}

	public AnsiComponent magenta() {
		return color(13);
	}

	public AnsiComponent cyan() {
		return color(14);
	}

	public AnsiComponent white() {
		return color(15);
	}

	public AnsiComponent error() {
		return white().darkRedBg();
	}

	public AnsiComponent debugColor(int index) {
		return switch (index & 3) {
			case 0 -> red();
			case 1 -> green();
			case 2 -> blue();
			case 3 -> yellow();
			default -> this;
		};
	}

	public AnsiComponent bgColor(int color) {
		bgColor = color;
		return this;
	}

	public AnsiComponent rgbBg(int color) {
		return bgColor(0xFF000000 | color);
	}

	public AnsiComponent blackBg() {
		return bgColor(0);
	}

	public AnsiComponent darkRedBg() {
		return bgColor(1);
	}

	public AnsiComponent greenBg() {
		return bgColor(2);
	}

	public AnsiComponent orangeBg() {
		return bgColor(3);
	}

	public AnsiComponent navyBg() {
		return bgColor(4);
	}

	public AnsiComponent purpleBg() {
		return bgColor(5);
	}

	public AnsiComponent tealBg() {
		return bgColor(6);
	}

	public AnsiComponent lightGrayBg() {
		return bgColor(7);
	}

	public AnsiComponent darkGrayBg() {
		return bgColor(8);
	}

	public AnsiComponent redBg() {
		return bgColor(9);
	}

	public AnsiComponent limeBg() {
		return bgColor(10);
	}

	public AnsiComponent yellowBg() {
		return bgColor(11);
	}

	public AnsiComponent blueBg() {
		return bgColor(12);
	}

	public AnsiComponent magentaBg() {
		return bgColor(13);
	}

	public AnsiComponent cyanBg() {
		return bgColor(14);
	}

	public AnsiComponent whiteBg() {
		return bgColor(15);
	}
}
