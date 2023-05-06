package dev.latvian.apps.webutils.ansi;

public interface AnsiAppendable {
	void appendAnsi(StringBuilder builder, AnsiContext ctx);

	default String toAnsiString() {
		var builder = new StringBuilder();
		appendAnsi(builder, AnsiContext.NONE);
		return builder.toString();
	}

	default String toDebugString() {
		var builder = new StringBuilder();
		appendAnsi(builder, new AnsiContext(1));
		return builder.toString();
	}

	default String toUnformattedString() {
		var builder = new StringBuilder();
		appendAnsi(builder, AnsiContext.UNFORMATTED);
		return builder.toString();
	}
}
