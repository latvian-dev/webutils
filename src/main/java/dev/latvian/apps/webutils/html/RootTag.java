package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.net.Response;
import io.javalin.http.HttpStatus;

import java.util.List;

public abstract class RootTag extends PairedTag {
	public final String path;
	public final String title;
	public final String description;
	public final Tag head;
	public final Tag body;

	public RootTag(String path, String title, String description) {
		super("html");

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

		var keywords = getKeywords();

		if (!keywords.isEmpty()) {
			head.meta("name", "keywords", "content", String.join(", ", keywords));
		}

		var author = getAuthor();

		if (!author.isEmpty()) {
			head.meta("name", "author", "content", author);
		}

		var rootUrl = getRootUrl();

		this.head.meta("property", "og:type", "content", "website");
		this.head.meta("property", "og:url", "content", rootUrl + path);

		var siteName = getSiteName();

		if (!siteName.isEmpty()) {
			this.head.meta("property", "og:site_name", "content", siteName);
		}

		this.head.meta("property", "og:title", "content", title);

		if (!description.isEmpty()) {
			this.head.meta("property", "og:description", "content", description);
		}

		var iconPath = getIconPath();

		if (!iconPath.isEmpty()) {
			var iconSize = getIconSize();

			head.meta("property", "og:image", "content", rootUrl + iconPath);
			head.meta("property", "og:image:width", "content", iconSize);
			head.meta("property", "og:image:height", "content", iconSize);
		}

		this.body = paired("body");
	}

	public void refresh(String url) {
		head.meta("http-equiv", "refresh", "content", "0; url='" + url + "'");
	}

	@Override
	protected RootTag copy0() {
		throw new IllegalStateException("Can't copy root tag");
	}

	@Override
	public void append(StringBuilder builder, boolean header) {
		if (header) {
			builder.append("<!DOCTYPE html>");
		}

		super.append(builder, header);
	}

	@Override
	public Response asResponse() {
		return asResponse(HttpStatus.OK, true);
	}

	public abstract String getSiteName();

	public abstract String getRootUrl();

	public String getAuthor() {
		return "";
	}

	public List<String> getKeywords() {
		return List.of();
	}

	public int getIconSize() {
		return 48;
	}

	public String getIconPath() {
		return "";
	}
}
