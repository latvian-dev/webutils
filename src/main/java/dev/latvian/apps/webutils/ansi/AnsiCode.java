package dev.latvian.apps.webutils.ansi;

public enum AnsiCode implements AnsiAppendable {
	RESET("[0m"),
	LEFT("[1000D"),
	BOLD("[1m"),
	UNDERLINE("[4m"),
	REVERSE("[7m");

	public final String code;

	AnsiCode(String c) {
		code = c;
	}

	@Override
	public void appendAnsi(StringBuilder builder, AnsiContext ctx) {
		if (!ctx.unformatted()) {
			builder.append(ctx.debug() ? '§' : Ansi.CODE);
			builder.append(code);
		}
	}

	@Override
	public String toString() {
		return Ansi.CODE + code;
	}
}
