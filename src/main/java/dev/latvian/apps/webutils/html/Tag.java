package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.ansi.Ansi;
import dev.latvian.apps.webutils.net.FileResponse;
import dev.latvian.apps.webutils.net.Response;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Tag implements TagConvertible {
	public Tag parent = null;

	public Tag end() {
		return parent;
	}

	public Tag getRoot() {
		var p = this;

		while (p.parent != null) {
			p = p.parent;
		}

		return p;
	}

	@Override
	public final void appendHTMLTag(Tag parent) {
		parent.add(this);
	}

	public String getRawContent() {
		return "";
	}

	public Tag add(Tag tag) {
		throw new IllegalStateException("This tag type does not support children tags");
	}

	public Tag getChild(int index) {
		throw new IllegalStateException("This tag type does not support children tags");
	}

	public final Tag add(TagConvertible tag) {
		tag.appendHTMLTag(this);
		return this;
	}

	@ApiStatus.Internal
	public Tag attr(String key, Object value) {
		throw new IllegalStateException("This tag type does not support attributes");
	}

	@ApiStatus.Internal
	public Tag attr(String key) {
		return attr(key, "<NO_VALUE>");
	}

	@Nullable
	public String getAttr(String key) {
		throw new IllegalStateException("This tag type does not support attributes");
	}

	public Tag classes(String classes) {
		var attr = getAttr("class");

		if (classes.isBlank()) {
			return this;
		}

		if (attr == null) {
			attr("class", classes);
		} else {
			attr("class", attr + " " + classes);
		}

		return this;
	}

	public boolean isEmpty() {
		return true;
	}

	public boolean isEmptyRecursively() {
		return true;
	}

	public void replace(Pattern pattern, BiConsumer<Tag, Matcher> replace) {
	}

	public abstract void write(Writer writer) throws Throwable;

	@Override
	public String toString() {
		var writer = new StringWriter();

		try {
			write(writer);
		} catch (OutOfMemoryError error) {
			Ansi.log("! Out of memory while generating HTML:");
			error.printStackTrace();
		} catch (Throwable error) {
			error.printStackTrace();
		}

		return writer.toString();
	}

	public void result(Context ctx) {
		result(ctx, HttpStatus.OK);
	}

	public void result(Context ctx, HttpStatus status) {
		ctx.status(status);
		ctx.contentType("text/html; charset=utf-8");
		ctx.result(getRoot().toString().getBytes(StandardCharsets.UTF_8));
	}

	public Response asResponse() {
		return asResponse(HttpStatus.OK);
	}

	public Response asResponse(HttpStatus status) {
		return FileResponse.of(status, "text/html; charset=utf-8", getRoot().toString().getBytes(StandardCharsets.UTF_8));
	}

	public Tag string(Object string) {
		return add(new StringTag(String.valueOf(string)));
	}

	public Tag space(int space) {
		if (space <= 0) {
			return this;
		} else if (space == 1) {
			return raw(" ");
		} else {
			return raw("&nbsp;".repeat(space));
		}
	}

	public Tag space() {
		return space(1);
	}

	public Tag nbsp() {
		return raw("&nbsp;");
	}

	public Tag raw(Object string) {
		return add(new RawTag(String.valueOf(string)));
	}

	@ApiStatus.Internal
	public UnpairedTag unpaired(String name) {
		var tag = new UnpairedTag(name);
		add(tag);
		return tag;
	}

	@ApiStatus.Internal
	public PairedTag paired(String name) {
		var tag = new PairedTag(name);
		add(tag);
		return tag;
	}

	// Attributes

	public Tag id(String id) {
		return attr("id", id);
	}

	public Tag title(String title) {
		return title.isEmpty() ? this : attr("title", title);
	}

	public Tag confirm(String s) {
		return attr("onclick", "return confirm('" + s + "')");
	}

	public Tag href(String href) {
		return href.isEmpty() ? this : attr("href", href);
	}

	public Tag style(String style) {
		return attr("style", style);
	}

	public Tag value(String value) {
		return attr("value", value);
	}

	public Tag value(String value, String min, String max, String step) {
		var t = attr("value", value).attr("min", min).attr("max", max);

		if (!step.isEmpty()) {
			t.attr("step", step);
		}

		return t;
	}

	public Tag pattern(String value) {
		return attr("pattern", value);
	}

	public Tag required() {
		return attr("required");
	}

	public Tag lazyLoading() {
		return attr("loading", "lazy");
	}

	public Tag target(String target) {
		return attr("target", target);
	}

	// Unpaired

	public Tag meta(String key, Object value) {
		return unpaired("meta").attr(key, value);
	}

	public Tag meta(String key1, Object value1, String key2, Object value2) {
		return meta(key1, value1).attr(key2, value2);
	}

	public Tag link(String key, Object value) {
		return unpaired("link").attr(key, value);
	}

	public Tag link(String key1, Object value1, String key2, Object value2) {
		return link(key1, value1).attr(key2, value2);
	}

	public Tag br() {
		unpaired("br");
		return this;
	}

	public Tag hr() {
		return unpaired("hr");
	}

	public Tag img(String src) {
		return unpaired("img").attr("src", src);
	}

	public Tag video(String src, boolean controls) {
		var tag = unpaired("video").attr("src", src).attr("preload", "metadata");

		if (controls) {
			tag.attr("controls");
		}

		return tag;
	}

	// Paired

	public Tag titleTag() {
		return paired("title");
	}

	public Tag div() {
		return paired("div");
	}

	public Tag div(String classes) {
		return div().classes(classes);
	}

	public Tag main() {
		return paired("main");
	}

	public Tag nav() {
		return paired("nav");
	}

	public Tag section() {
		return paired("section");
	}

	public Tag section(String id) {
		return section().id(id);
	}

	public Tag article() {
		return paired("article");
	}

	public Tag header() {
		return paired("header");
	}

	public Tag footer() {
		return paired("footer");
	}

	public Tag span() {
		return paired("span");
	}

	public Tag span(String classes) {
		return span().classes(classes);
	}

	public Tag span(String classes, Object string) {
		return span(classes).string(string);
	}

	public Tag spanstr(Object string) {
		return span().string(string).end();
	}

	public Tag p() {
		return paired("p");
	}

	public Tag h1() {
		return paired("h1");
	}

	public Tag h2() {
		return paired("h2");
	}

	public Tag h3() {
		return paired("h3");
	}

	public Tag ol() {
		return paired("ol");
	}

	public Tag ul() {
		return paired("ul");
	}

	public Tag li() {
		return paired("li");
	}

	public Tag a(String url) {
		return paired("a").href(url);
	}

	public Tag a(String url, Object string) {
		return a(url).string(string);
	}

	public Tag aClick(String click) {
		return a("#").attr("onclick", click);
	}

	public Tag time(Instant instant) {
		return paired("time").attr("datetime", instant.toString());
	}

	public Tag table() {
		return paired("table");
	}

	public Tag thead() {
		return paired("thead");
	}

	public Tag tbody() {
		return paired("tbody");
	}

	public Tag tr() {
		return paired("tr");
	}

	public Tag th() {
		return paired("th");
	}

	public Tag td() {
		return paired("td");
	}

	public Tag script(String src) {
		return paired("script").attr("src", src);
	}

	public Tag deferScript(String src) {
		return script(src).attr("defer");
	}

	public FormTag form(String method) {
		var form = new FormTag();
		form.attr("method", method);
		add(form);
		return form;
	}

	public FormTag form(String method, String action) {
		return (FormTag) form(method).attr("action", action);
	}

	public Tag option(String value) {
		return paired("option").attr("value", value);
	}

	public Tag iframe(String name) {
		return paired("iframe").attr("name", name);
	}

	// Form

	public String getPrefix() {
		return "";
	}

	public Tag input() {
		return paired("input");
	}

	public Tag input(String type) {
		return input().attr("type", type);
	}

	public Tag input(String type, String name) {
		return input(type).attr("id", getPrefix() + name).attr("name", name);
	}

	public Tag select(String name) {
		return paired("select").attr("id", getPrefix() + name).attr("name", name);
	}

	public Tag label(String forId) {
		return paired("label").attr("for", getPrefix() + forId);
	}

	public Tag label(String forId, Object string) {
		return label(forId).string(string);
	}

	public Tag textarea(String name, int rows, int cols) {
		return paired("textarea").attr("id", getPrefix() + name).attr("name", name).attr("rows", rows).attr("cols", cols);
	}

	public Tag checkbox(String name, boolean checked) {
		var t = input("checkbox", name);

		if (checked) {
			t.attr("checked");
		}

		return t;
	}
}
