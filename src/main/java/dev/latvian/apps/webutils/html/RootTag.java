package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.net.MimeType;

import java.util.List;

public class RootTag extends PairedTag implements ResponseTag {
	public static String SITE_NAME = "";
	public static String ROOT_URL = "";
	public static String ICON_PATH = "";
	public static int ICON_SIZE = 48;
	public static List<String> KEYWORDS = List.of();
	public static String AUTHOR = "";

	public final String path;
	public final String title;
	public final String description;
	public final Tag head;
	public final Tag body;

	public RootTag(String path, String title, String description) {
		super("html");

		if (ROOT_URL.isEmpty()) {
			throw new IllegalStateException("You must set RootTag.ROOT_URL!");
		}

		this.path = path;
		this.title = title;
		this.description = description;
		attr("lang", "en");
		this.head = paired("head");
		this.head.meta("charset", "utf-8");
		this.head.meta("http-equiv", "X-UA-Compatible", "content", "IE=edge");
		this.head.meta("name", "viewport", "content", "width=device-width, initial-scale=1");

		head.titleTag().string(title);

		if (!description.isEmpty()) {
			head.meta("name", "description", "content", description);
		}

		if (!KEYWORDS.isEmpty()) {
			head.meta("name", "keywords", "content", String.join(", ", KEYWORDS));
		}

		if (!AUTHOR.isEmpty()) {
			head.meta("name", "author", "content", AUTHOR);
		}

		this.head.meta("property", "og:type", "content", "website");
		this.head.meta("property", "og:url", "content", ROOT_URL + path);

		if (!SITE_NAME.isEmpty()) {
			this.head.meta("property", "og:site_name", "content", SITE_NAME);
		}

		this.head.meta("property", "og:title", "content", title);

		if (!description.isEmpty()) {
			this.head.meta("property", "og:description", "content", description);
		}

		if (!ICON_PATH.isEmpty()) {
			head.meta("property", "og:image", "content", ROOT_URL + ICON_PATH);
			head.meta("property", "og:image:width", "content", ICON_SIZE);
			head.meta("property", "og:image:height", "content", ICON_SIZE);
		}

		this.body = paired("body");
	}

	public void refresh(String url) {
		head.meta("http-equiv", "refresh", "content", "0; url='" + url + "'");
	}

	@Override
	public void append(StringBuilder builder, boolean header) {
		if (header) {
			builder.append("<!DOCTYPE html>");
		}

		super.append(builder, header);
	}

	@Override
	public String getMimeType() {
		return MimeType.HTML;
	}
}
