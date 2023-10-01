package dev.latvian.apps.webutils.html;

import dev.latvian.apps.webutils.ansi.Ansi;
import dev.latvian.apps.webutils.ansi.AnsiComponent;
import dev.latvian.apps.webutils.data.Lazy;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Tag extends TagConvertible {
	@Override
	default void appendHTMLTag(Tag parent) {
		parent.add(this);
	}

	default Tag copy() {
		return this;
	}

	void append(StringBuilder builder, boolean header);

	void appendRaw(StringBuilder builder);

	default void ansi(AnsiComponent component, int depth, int indent) {
		component.append(toRawString());
	}

	default String toRawString() {
		var builder = new StringBuilder();

		try {
			appendRaw(builder);
		} catch (OutOfMemoryError error) {
			Ansi.log("! Out of memory while generating HTML:");
			error.printStackTrace();
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
			Ansi.log("! Out of memory while generating HTML:");
			error.printStackTrace();
		} catch (Throwable error) {
			error.printStackTrace();
		}

		return builder.toString();
	}

	default AnsiComponent toAnsi(boolean indent) {
		var component = Ansi.of();
		ansi(component, 0, indent ? 0 : -1);
		return component;
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

	default Tag add(TagConvertible tag) {
		tag.appendHTMLTag(this);
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

	default Tag lazy(Lazy<? extends TagConvertible> lazy) {
		return add(new LazyTagConvertible(lazy));
	}

	default UnpairedTag unpaired(String name) {
		var tag = new UnpairedTag(name);
		add(tag);
		return tag;
	}

	default PairedTag paired(String name) {
		var tag = new PairedTag(name);
		add(tag);
		return tag;
	}

	// Attributes

	default Tag id(String id) {
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

	default Tag value(String value) {
		return attr("value", value);
	}

	default Tag value(String value, String min, String max, String step) {
		var t = attr("value", value).attr("min", min).attr("max", max);

		if (!step.isEmpty()) {
			t.attr("step", step);
		}

		return t;
	}

	default Tag pattern(String value) {
		return attr("pattern", value);
	}

	default Tag required() {
		return attr("required");
	}

	default Tag lazyLoading() {
		return attr("loading", "lazy");
	}

	default Tag target(String target) {
		return attr("target", target);
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

	default Tag main() {
		return paired("main");
	}

	default Tag nav() {
		return paired("nav");
	}

	default Tag section() {
		return paired("section");
	}

	default Tag section(String id) {
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

	default FormTag form(String method) {
		var form = new FormTag();
		form.attr("method", method);
		add(form);
		return form;
	}

	default FormTag form(String method, String action) {
		return (FormTag) form(method).attr("action", action);
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

	default Tag input(String type, String name) {
		return input(type).attr("id", getPrefix() + name).attr("name", name);
	}

	default Tag select(String name) {
		return paired("select").attr("id", getPrefix() + name).attr("name", name);
	}

	default Tag label(String forId) {
		return paired("label").attr("for", getPrefix() + forId);
	}

	default Tag label(String forId, Object string) {
		return label(forId).string(string);
	}

	default Tag textarea(String name, int rows, int cols) {
		return paired("textarea").attr("id", getPrefix() + name).attr("name", name).attr("rows", rows).attr("cols", cols);
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
}
