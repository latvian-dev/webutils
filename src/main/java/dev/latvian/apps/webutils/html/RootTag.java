package dev.latvian.apps.webutils.html;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.io.Writer;
import java.util.function.Consumer;

public class RootTag extends PairedTag {
	public static Consumer<RootTag> DEFAULT = root -> {
		root.head.titleTag().raw(root.title.getRawContent());
		root.head.meta("http-equiv", "X-UA-Compatible", "content", "IE=edge");
		root.head.meta("name", "viewport", "content", "width=device-width, initial-scale=1");
		root.head.meta("property", "og:title", "content", root.title.getRawContent());
		root.head.meta("property", "og:description", "content", root.description.getRawContent());
		root.head.link("rel", "stylesheet", "href", "/style.css");
		root.head.deferScript("/script.js");
		var h = root.body.header();
		h.h1().add(root.title);
		h.h2().add(root.description);
		root.body.hr();
	};

	public static RootTag create(Tag title, Tag description) {
		var root = new RootTag(title, description);
		root.head.meta("charset", "utf-8");
		DEFAULT.accept(root);
		return root;
	}

	public final Tag title;
	public final Tag description;
	public final Tag head;
	public final Tag body;

	private RootTag(Tag title, Tag description) {
		super("html");
		attr("lang", "en");
		this.title = title;
		this.description = description;
		this.head = paired("head");
		this.body = paired("body");
	}

	@Override
	public Tag getRoot() {
		return this;
	}

	@Override
	public void write(Writer writer) throws Throwable {
		writer.write("<!DOCTYPE html>");
		super.write(writer);
	}

	public void result(Context ctx) {
		result(ctx, HttpStatus.OK);
	}

	public void result(Context ctx, HttpStatus status) {
		ctx.status(status);
		ctx.contentType("text/html");
		ctx.result(toString());
	}
}
