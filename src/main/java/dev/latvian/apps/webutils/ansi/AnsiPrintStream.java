package dev.latvian.apps.webutils.ansi;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;

// WIP for printing exceptions to console with ANSI colors
public class AnsiPrintStream extends PrintStream {
	public final PrintStream parent;

	public AnsiPrintStream(@NotNull PrintStream parent) {
		super(parent);
		this.parent = parent;
	}
}
