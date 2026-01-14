package dev.latvian.apps.webutils.html;

public class FormTag extends PairedTag {
	private final String prefix;

	public FormTag(String prefix) {
		super("form");
		this.prefix = "";
	}

	@Override
	public String getPrefix() {
		return prefix;
	}
}
