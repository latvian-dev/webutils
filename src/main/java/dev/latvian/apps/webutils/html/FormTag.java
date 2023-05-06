package dev.latvian.apps.webutils.html;

import java.util.Random;

public class FormTag extends PairedTag {
	public static final Random RANDOM = new Random();

	private final String prefix;

	public FormTag() {
		super("form");
		var str = String.format("%06x", RANDOM.nextInt(0xFFFFFF));
		id(str);
		prefix = str + "_";
	}

	@Override
	public String getPrefix() {
		return prefix;
	}
}
