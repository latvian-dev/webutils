package dev.latvian.apps.webutils.ansi;

import dev.latvian.apps.webutils.TimeUtils;

import java.util.Date;

public class Log {
	public enum Type {
		INFO,
		WARN,
		ERROR,
		DEBUG,
		SUCCESS,
		FAIL,
		IMPORTANT,
		CODE
	}

	public static Log INSTANCE = new Log();

	public static void info(Object message) {
		INSTANCE.log(Type.INFO, Ansi.of(message));
	}

	public static void warn(Object message) {
		INSTANCE.log(Type.WARN, Ansi.of(message).yellow());
	}

	public static void error(Object message) {
		INSTANCE.log(Type.ERROR, Ansi.of(message).error());
	}

	public static void debug(Object message) {
		INSTANCE.log(Type.DEBUG, Ansi.of(message).lightGray());
	}

	public static void success(Object message) {
		INSTANCE.log(Type.SUCCESS, Ansi.of(message).green());
	}

	public static void fail(Object message) {
		INSTANCE.log(Type.FAIL, Ansi.of(message).red());
	}

	public static void success(Object message, boolean success) {
		if (success) {
			success(message);
		} else {
			fail(message);
		}
	}

	public static void important(Object message) {
		INSTANCE.log(Type.IMPORTANT, Ansi.of(message).orange());
	}

	public static void code(Object message) {
		INSTANCE.log(Type.CODE, Ansi.of(message).teal());
	}

	public void log(Type type, AnsiComponent message) {
		var now = new Date();
		var c = Ansi.of();
		c.append(Ansi.cyan(TimeUtils.formatDate(new StringBuilder(), now).toString()));
		c.append(' ');
		c.append(message);
		System.out.println(c);
	}
}
