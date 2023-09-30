package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.data.Lazy;

public class LazyRawTag extends RawTag {
	private final Lazy<String> lazyString;

	public LazyRawTag(Lazy<String> lazyString) {
		super("");
		this.lazyString = lazyString;
	}

	@Override
	public String getRawContent() {
		var s = lazyString.get();
		return s == null ? "" : s;
	}

	@Override
	public boolean isEmpty() {
		return getRawContent().isEmpty();
	}

	@Override
	public boolean isEmptyRecursively() {
		return getRawContent().isEmpty();
	}
}
