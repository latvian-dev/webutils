package dev.latvian.apps.webutils.ansi;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AnsiJava {
	public static AnsiComponent of(Object object) {
		return of0(object, 0);
	}

	private static AnsiComponent of0(@Nullable Object object, int depth) {
		if (object == null) {
			return Ansi.darkRed("null");
		} else if (object instanceof Map<?, ?> map) {
			var builder = Ansi.of().debugColor(depth);
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
				builder.append(of0(entry.getValue(), depth + 1));
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

				builder.append(of0(e, depth + 1));
			}

			builder.append(']');
			return builder;
		} else if (object instanceof CharSequence) {
			return Ansi.orange("\"" + object + "\"");
		} else if (object instanceof Boolean b) {
			return b ? Ansi.green("true") : Ansi.red("false");
		} else if (object instanceof Number) {
			return Ansi.purple(object);
		} else {
			return Ansi.lightGray(object.toString());
		}
	}
}
