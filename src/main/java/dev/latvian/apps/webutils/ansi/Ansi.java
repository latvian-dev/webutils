package dev.latvian.apps.webutils.ansi;

import dev.latvian.apps.webutils.json.JSON;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * See <a href="https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797">ANSI Escape Sequences</a>
 */
public interface Ansi {
	Pattern PATTERN = Pattern.compile("\u001B\\[(?:\\d;)?\\d+[mD]");
	char CHAR = '\u001B';

	String[] COLOR_NAMES = {
			"black",
			"darkRed",
			"green",
			"orange",
			"navy",
			"purple",
			"teal",
			"lightGray",
			"darkGray",
			"red",
			"lime",
			"yellow",
			"blue",
			"magenta",
			"cyan",
			"white",
	};

	static AnsiComponent of() {
		return new AnsiComponent("");
	}

	static AnsiComponent of(Object text) {
		return text instanceof AnsiComponent ac ? ac : new AnsiComponent(text);
	}

	static AnsiComponent ofObject(Object object) {
		return ofObject0(object, 0);
	}

	private static AnsiComponent ofObject0(@Nullable Object object, int depth) {
		if (object == null || object == JSON.NULL) {
			return Ansi.darkRed("null");
		} else if (object instanceof Map<?, ?> map) {
			var builder = Ansi.of().treeColor(depth);
			builder.append('{');
			boolean first = true;

			for (var entry : map.entrySet()) {
				if (first) {
					first = false;
				} else {
					builder.append(',');
				}

				builder.append(Ansi.cyan("\"" + entry.getKey() + "\""));
				builder.append(':');
				builder.append(ofObject0(entry.getValue(), depth + 1));
			}

			builder.append('}');
			return builder;
		} else if (object instanceof Iterable<?> itr) {
			var builder = Ansi.of();
			builder.append('[');
			boolean first = true;

			for (var e : itr) {
				if (first) {
					first = false;
				} else {
					builder.append(',');
				}

				builder.append(ofObject0(e, depth + 1));
			}

			builder.append(']');
			return builder;
		} else if (object instanceof CharSequence) {
			return Ansi.orange("\"" + object + "\"");
		} else if (object instanceof Boolean b) {
			return b ? Ansi.green("true") : Ansi.red("false");
		} else if (object instanceof Number) {
			return Ansi.purple(object);
		} else if (object.getClass().isRecord()) {
			var builder = Ansi.of().treeColor(depth);
			builder.append('{');
			boolean first = true;

			for (var component : object.getClass().getRecordComponents()) {
				if (first) {
					first = false;
				} else {
					builder.append(',');
				}

				builder.append(Ansi.cyan("\"" + component.getName() + "\""));
				builder.append(':');

				try {
					builder.append(ofObject0(component.getAccessor().invoke(object), depth + 1));
				} catch (Exception ex) {
					builder.append(darkRed("<error>").whiteBg());
					ex.printStackTrace();
				}
			}

			builder.append('}');
			return builder;
		} else if (object.getClass().isEnum()) {
			return Ansi.orange(object);
		} else {
			return Ansi.lightGray(object.toString());
		}
	}

	static AnsiComponent bold(Object text) {
		return of(text).bold();
	}

	static AnsiComponent italic(Object text) {
		return of(text).italic();
	}

	static AnsiComponent underline(Object text) {
		return of(text).underline();
	}

	static AnsiComponent blink(Object text) {
		return of(text).blink();
	}

	static AnsiComponent reverse(Object text) {
		return of(text).reverse();
	}

	static AnsiComponent hidden(Object text) {
		return of(text).hidden();
	}

	static AnsiComponent strikethrough(Object text) {
		return of(text).strikethrough();
	}

	static AnsiComponent black(Object text) {
		return of(text).black();
	}

	static AnsiComponent darkRed(Object text) {
		return of(text).darkRed();
	}

	static AnsiComponent green(Object text) {
		return of(text).green();
	}

	static AnsiComponent orange(Object text) {
		return of(text).orange();
	}

	static AnsiComponent navy(Object text) {
		return of(text).navy();
	}

	static AnsiComponent purple(Object text) {
		return of(text).purple();
	}

	static AnsiComponent teal(Object text) {
		return of(text).teal();
	}

	static AnsiComponent lightGray(Object text) {
		return of(text).lightGray();
	}

	static AnsiComponent darkGray(Object text) {
		return of(text).darkGray();
	}

	static AnsiComponent red(Object text) {
		return of(text).red();
	}

	static AnsiComponent lime(Object text) {
		return of(text).lime();
	}

	static AnsiComponent yellow(Object text) {
		return of(text).yellow();
	}

	static AnsiComponent blue(Object text) {
		return of(text).blue();
	}

	static AnsiComponent magenta(Object text) {
		return of(text).magenta();
	}

	static AnsiComponent cyan(Object text) {
		return of(text).cyan();
	}

	static AnsiComponent white(Object text) {
		return of(text).white();
	}
}
