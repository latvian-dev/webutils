package dev.latvian.apps.webutils.misc;

import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;
import java.io.PrintStream;

public class StringBuilderWriter extends PrintStream {
	private final StringBuilder stringBuilder;
	private int limit;

	public StringBuilderWriter(StringBuilder stringBuilder, int limit) {
		super(OutputStream.nullOutputStream());
		this.stringBuilder = stringBuilder;
		this.limit = limit;
	}

	public StringBuilderWriter(StringBuilder stringBuilder) {
		this(stringBuilder, 20);
	}

	@Override
	public void println(@Nullable Object x) {
		if (--limit >= 0) {
			stringBuilder.append('\n');
			stringBuilder.append(x);
		}
	}
}
