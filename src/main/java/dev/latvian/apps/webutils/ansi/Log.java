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
		INSTANCE.log(Type.INFO, message);
	}

	public static void warn(Object message) {
		INSTANCE.log(Type.WARN, message);
	}

	public static void error(Object message) {
		INSTANCE.log(Type.ERROR, message);
	}

	public static void debug(Object message) {
		INSTANCE.log(Type.DEBUG, message);
	}

	public static void success(Object message) {
		INSTANCE.log(Type.SUCCESS, message);
	}

	public static void fail(Object message) {
		INSTANCE.log(Type.FAIL, message);
	}

	public static void success(Object message, boolean success) {
		if (success) {
			success(message);
		} else {
			fail(message);
		}
	}

	public static void important(Object message) {
		INSTANCE.log(Type.IMPORTANT, message);
	}

	public static void code(Object message) {
		INSTANCE.log(Type.CODE, message);
	}

	public void log(Type type, Object message) {
		var now = new Date();
		var c = Ansi.of();
		c.append(Ansi.cyan(TimeUtils.formatDate(new StringBuilder(), now).toString()));
		c.append(' ');
		c.append(format(message, type));
		System.out.println(c);
	}

	public AnsiComponent format(Object message, Type type) {
		return switch (type) {
			case WARN -> Ansi.of(message).yellow();
			case ERROR -> Ansi.of(message).error();
			case DEBUG -> Ansi.of(message).lightGray();
			case SUCCESS -> Ansi.of(message).green();
			case FAIL -> Ansi.of(message).red();
			case IMPORTANT -> Ansi.of(message).orange();
			case CODE -> Ansi.of(message).teal();
			default -> Ansi.of(message);
		};
	}
}
