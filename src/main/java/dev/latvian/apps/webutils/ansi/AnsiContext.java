package dev.latvian.apps.webutils.ansi;

public class AnsiContext {
	public static final AnsiContext UNFORMATTED = new AnsiContext(-1);
	public static final AnsiContext NONE = new AnsiContext(0);

	public int index;

	public AnsiContext(int index) {
		this.index = index;
	}

	public boolean unformatted() {
		return index == -1;
	}

	public boolean debug() {
		return index != 0;
	}
}
