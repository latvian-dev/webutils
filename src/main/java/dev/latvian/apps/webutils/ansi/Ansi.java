package dev.latvian.apps.webutils.ansi;

import dev.latvian.apps.webutils.FormattingUtils;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * See <a href="https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797">ANSI Escape Sequences</a>
 */
public interface Ansi {
	Pattern PATTERN = Pattern.compile("\u001B\\[(?:\\d;)?\\d+[mD]");
	char CODE = '\u001B';

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

	static Date log(Object message, boolean newline) {
		var c = of();
		var now = new Date();
		c.append(cyan(FormattingUtils.formatDate(new StringBuilder(), now).toString()));

		var c1 = of(message);

		exit:
		if (c1.text.length() >= 3 && c1.text.charAt(1) == ' ') {
			switch (c1.text.charAt(0)) {
				case '|' -> c1.blue();
				case '+' -> c1.green();
				case '*' -> c1.orange();
				case '~' -> c1.yellow();
				case '-' -> c1.red();
				case '!' -> c1.error();
				case '?' -> c1.lightGray();
				case '`' -> c1.teal();
				default -> {
					break exit;
				}
			}

			c1.text = c1.text.substring(2);
		}

		c.append(' ');
		c.append(c1);

		if (newline) {
			System.out.println(c);
		} else {
			System.out.print(c);
		}

		return now;
	}

	static Date log(Object message) {
		return log(message, true);
	}
}
