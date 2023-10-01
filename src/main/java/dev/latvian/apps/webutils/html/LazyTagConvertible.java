package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.data.Lazy;

public record LazyTagConvertible(Lazy<? extends TagConvertible> lazy) implements TagConvertible {
	@Override
	public void appendHTMLTag(Tag parent) {
		var t = lazy.get();

		if (t != null) {
			t.appendHTMLTag(parent);
		}
	}
}
