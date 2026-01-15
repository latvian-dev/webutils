package dev.latvian.apps.webutils.html;

import dev.latvian.apps.tinyserver.http.response.HTTPResponse;
import dev.latvian.apps.tinyserver.http.response.HTTPStatus;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface Tag extends TagFunction {
	@Override
	default void acceptTag(Tag parent) {
		parent.add(this);
	}

	default Tag copy() {
		return this;
	}

	default Tag end() {
		throw new IllegalStateException("This tag type does not support end()");
	}

	void append(StringBuilder builder, boolean header);

	void appendRaw(StringBuilder builder);

	default String toRawString() {
		var builder = new StringBuilder();

		try {
			appendRaw(builder);
		} catch (OutOfMemoryError error) {
			throw new RuntimeException("Out of memory while generating HTML:", error);
		} catch (Throwable error) {
			error.printStackTrace();
		}

		return builder.toString();
	}

	default String toTagString(boolean header) {
		var builder = new StringBuilder();

		try {
			append(builder, header);
		} catch (OutOfMemoryError error) {
			throw new RuntimeException("Out of memory while generating HTML:", error);
		} catch (Throwable error) {
			error.printStackTrace();
		}

		return builder.toString();
	}

	default Tag add(Tag tag) {
		addAnd(tag);
		return this;
	}

	default Tag addAnd(Tag tag) {
		throw new IllegalStateException("This tag type does not support children tags");
	}

	default Tag getChild(int index) {
		throw new IllegalStateException("This tag type does not support children tags");
	}

	default Tag add(@Nullable TagFunction function) {
		if (function != null) {
			function.acceptTag(this);
		}

		return this;
	}

	default Tag attr(String key, Object value) {
		throw new IllegalStateException("This tag type does not support attributes");
	}

	default Tag attr(String key) {
		return attr(key, "<NO_VALUE>");
	}

	@Nullable
	default String getAttr(String key) {
		throw new IllegalStateException("This tag type does not support attributes");
	}

	default Tag classes(String classes) {
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

	default boolean isEmpty() {
		return false;
	}

	default boolean isEmptyRecursively() {
		return isEmpty();
	}

	default void replace(Pattern pattern, BiConsumer<Tag, Matcher> replace) {
	}

	default HTTPResponse asResponse() {
		return asResponse(HTTPStatus.OK, false);
	}

	default HTTPResponse asResponse(HTTPStatus status, boolean header) {
		return status.html(toTagString(header));
	}

	default Tag string(Object string) {
		var str = String.valueOf(string);
		return str.isEmpty() ? this : add(new StringTag(str));
	}

	default Tag space(int space) {
		return switch (space) {
			case 1 -> raw("&nbsp;");
			case 2 -> raw("&nbsp;&nbsp;");
			case 3 -> raw("&nbsp;&nbsp;&nbsp;");
			default -> space <= 0 ? this : raw("&nbsp;".repeat(space));
		};
	}

	default Tag space() {
		return space(1);
	}

	default Tag raw(Object string) {
		var str = String.valueOf(string);
		return str.isEmpty() ? this : add(new RawTag(str));
	}

	default UnpairedTag unpaired(String name) {
		var tag = new UnpairedTag(name);
		tag.parent = this;
		add(tag);
		return tag;
	}

	default PairedTag paired(String name) {
		var tag = new PairedTag(name);
		tag.parent = this;
		add(tag);
		return tag;
	}

	// Attributes

	default Tag id(Object id) {
		return attr("id", id);
	}

	default Tag title(String title) {
		return title.isEmpty() ? this : attr("title", title);
	}

	default Tag confirm(String s) {
		return attr("onclick", "return confirm('" + s + "')");
	}

	default Tag href(String href) {
		return href.isEmpty() ? this : attr("href", href);
	}

	default Tag style(String style) {
		return attr("style", style);
	}

	default Tag value(Object value) {
		return attr("value", value);
	}

	default Tag value(Object value, Object min, Object max, String step) {
		var t = attr("value", value).attr("min", min).attr("max", max);

		if (!step.isEmpty()) {
			t.attr("step", step);
		}

		return t;
	}

	default Tag pattern(Object value) {
		return attr("pattern", value);
	}

	default Tag required() {
		return attr("required");
	}

	default Tag lazyLoading() {
		return attr("loading", "lazy");
	}

	default Tag target(Object target) {
		return attr("target", target);
	}

	default Tag name(Object name) {
		return attr("name", name);
	}

	// Unpaired

	default Tag meta(String key, Object value) {
		return unpaired("meta").attr(key, value);
	}

	default Tag meta(String key1, Object value1, String key2, Object value2) {
		return meta(key1, value1).attr(key2, value2);
	}

	default Tag link(String key, Object value) {
		return unpaired("link").attr(key, value);
	}

	default Tag link(String key1, Object value1, String key2, Object value2) {
		return link(key1, value1).attr(key2, value2);
	}

	default Tag stylesheet(String path) {
		return link("rel", "stylesheet", "href", path);
	}

	default Tag br() {
		unpaired("br");
		return this;
	}

	default Tag hr() {
		return unpaired("hr");
	}

	default Tag img(String src) {
		return unpaired("img").attr("src", src);
	}

	default Tag video(String src, boolean controls) {
		var tag = unpaired("video").attr("src", src).attr("preload", "metadata");

		if (controls) {
			tag.attr("controls");
		}

		return tag;
	}

	// Paired

	default Tag titleTag() {
		return paired("title");
	}

	default Tag div() {
		return paired("div");
	}

	default Tag div(String classes) {
		return div().classes(classes);
	}

	@SuppressWarnings("ConfusingMainMethod")
	default Tag main() {
		return paired("main");
	}

	default Tag nav() {
		return paired("nav");
	}

	default Tag section() {
		return paired("section");
	}

	default Tag section(Object id) {
		return section().id(id);
	}

	default Tag article() {
		return paired("article");
	}

	default Tag header() {
		return paired("header");
	}

	default Tag footer() {
		return paired("footer");
	}

	default Tag span() {
		return paired("span");
	}

	default Tag span(String classes) {
		return span().classes(classes);
	}

	default Tag span(String classes, Object string) {
		return span(classes).string(string);
	}

	default Tag spanstr(Object string) {
		span().string(string);
		return this;
	}

	default Tag p() {
		return paired("p");
	}

	default Tag h1() {
		return paired("h1");
	}

	default Tag h2() {
		return paired("h2");
	}

	default Tag h3() {
		return paired("h3");
	}

	default Tag h4() {
		return paired("h4");
	}

	default Tag h5() {
		return paired("h5");
	}

	default Tag h6() {
		return paired("h6");
	}

	default Tag heading(int heading) {
		return switch (heading) {
			case 1 -> h1();
			case 2 -> h2();
			case 3 -> h3();
			case 4 -> h4();
			case 5 -> h5();
			default -> heading <= 0 ? this : h6();
		};
	}

	default Tag ol() {
		return paired("ol");
	}

	default Tag ul() {
		return paired("ul");
	}

	default Tag li() {
		return paired("li");
	}

	default Tag a(String url) {
		return paired("a").href(url);
	}

	default Tag a(String url, Object string) {
		return a(url).string(string);
	}

	default Tag aClick(String click) {
		return a("#").attr("onclick", click);
	}

	default Tag time(Instant instant) {
		return paired("time").attr("datetime", instant.toString());
	}

	default Tag table() {
		return paired("table");
	}

	default Tag thead() {
		return paired("thead");
	}

	default Tag tbody() {
		return paired("tbody");
	}

	default Tag tr() {
		return paired("tr");
	}

	default Tag th() {
		return paired("th");
	}

	default Tag td() {
		return paired("td");
	}

	default Tag code() {
		return paired("code");
	}

	default Tag codestr(Object string) {
		code().string(string);
		return this;
	}

	default Tag script(String src) {
		return paired("script").attr("src", src);
	}

	default Tag deferScript(String src) {
		return script(src).attr("defer");
	}

	default Tag asyncScript(String src) {
		return script(src).attr("async");
	}

	default FormTag form(String prefix, String method) {
		var form = new FormTag(prefix);
		form.attr("method", method);
		add(form);
		return form;
	}

	default FormTag form(String prefix, String method, String action) {
		return (FormTag) form(prefix, method).attr("action", action);
	}

	default Tag option(String value) {
		return paired("option").attr("value", value);
	}

	default Tag iframe() {
		return paired("iframe");
	}

	default Tag iframe(String name) {
		return iframe().attr("name", name);
	}

	default Tag styleTag() {
		return paired("style");
	}

	default Tag pre() {
		return paired("pre");
	}

	default Tag i() {
		return paired("i");
	}

	default Tag em() {
		return paired("em");
	}

	default Tag mark() {
		return paired("mark");
	}

	default Tag b() {
		return paired("b");
	}

	default Tag strong() {
		return paired("strong");
	}

	default Tag s() {
		return paired("s");
	}

	default Tag small() {
		return paired("small");
	}

	default Tag sub() {
		return paired("sub");
	}

	default Tag sup() {
		return paired("sup");
	}

	default Tag u() {
		return paired("u");
	}

	default Tag svg(PairedTag xml) {
		var svg = paired("svg");
		svg.attr("xmlns", "http://www.w3.org/2000/svg");
		svg.attr("fill", "currentcolor");

		if (xml.attributes != null) {
			svg.attributes.putAll(xml.attributes);
		}

		for (var tag : xml.content) {
			svg.add(tag);
		}

		return svg;
	}

	// Form

	default String getPrefix() {
		return "";
	}

	default Tag input() {
		return paired("input");
	}

	default Tag input(String type) {
		return input().attr("type", type);
	}

	default Tag input(String type, Object name) {
		return input(type).id(getPrefix() + name).name(name);
	}

	default Tag select(Object name) {
		return paired("select").id(getPrefix() + name).name(name);
	}

	default Tag label(String forId) {
		return paired("label").attr("for", getPrefix() + forId);
	}

	default Tag label(String forId, Object string) {
		return label(forId).string(string);
	}

	default Tag textarea(String name, int rows) {
		return paired("textarea").id(getPrefix() + name).name(name).attr("rows", rows);
	}

	default Tag checkbox(String name, boolean checked) {
		var t = input("checkbox", name);

		if (checked) {
			t.attr("checked");
		}

		return t;
	}

	default Tag button() {
		return paired("button");
	}

	default Tag radio(String group, Object value) {
		return input("radio").id(getPrefix() + group + "-" + value).name(group).value(value);
	}
}
