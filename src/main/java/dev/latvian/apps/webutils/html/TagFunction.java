package dev.latvian.apps.webutils.html;

@FunctionalInterface
public interface TagFunction {
	TagFunction IDENTITY = t -> {
	};

	void acceptTag(Tag tag);
}
