package dev.latvian.apps.webutils;

import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface FormattingUtils {
	DecimalFormat LONG_FORMAT = new DecimalFormat("#,###");
	Pattern REGEX_PATTERN = Pattern.compile("/(.*)/([a-z]*)");
	Pattern REMOVE_WEIRD_CHARACTERS = Pattern.compile("[^\\w-.]");
	Pattern REMOVE_DASHES = Pattern.compile("-{2,}");
	Pattern NON_W_PATTERN = Pattern.compile("\\W");
	Pattern STACK_AT_PATTERN = Pattern.compile("([ \\t]+at )([\\w./$@]+)\\.([\\w/$]+)\\.(<init>|[\\w$]+)\\((Unknown Source|\\.dynamic|Native Method|[\\w.$]+:\\d+)\\)(?: ~?\\[.*:.*])?(?: \\{.*})?");

	static String format(long number) {
		return LONG_FORMAT.format(number);
	}

	static String normalize(@Nullable String s) {
		if (s == null || s.isEmpty()) {
			return "";
		}

		s = REMOVE_DASHES.matcher(REMOVE_WEIRD_CHARACTERS.matcher(s.toLowerCase()).replaceAll("-")).replaceAll("-");

		while (s.startsWith("-")) {
			s = s.substring(1);
		}

		while (s.endsWith("-")) {
			s = s.substring(0, s.length() - 1);
		}

		return s;
	}

	static String trim(String string, int max) {
		string = string.trim();

		if (string.length() > max) {
			return string.substring(0, max - 1) + '…';
		}

		return string;
	}

	static String trimContent(String content) {
		return trim(content, 2000);
	}

	static String verifyMaxLength(String string, int max) {
		if (string.length() > max) {
			throw new IllegalArgumentException("String is too long!");
		}

		return string;
	}

	static void titleCase(StringBuilder sb, String string) {
		if (!string.isEmpty()) {
			sb.append(Character.toUpperCase(string.charAt(0)));
			sb.append(string, 1, string.length());
		}
	}

	@Nullable
	static Pattern parseSafeRegEx(String string, int defaultFlags) {
		if (string.length() < 3) {
			return null;
		}

		Matcher matcher = REGEX_PATTERN.matcher(string);

		if (!matcher.matches()) {
			return null;
		}

		int flags = defaultFlags;
		String f = matcher.group(2);

		for (int i = 0; i < f.length(); ++i) {
			switch (f.charAt(i)) {
				case 'U' -> flags |= Pattern.UNICODE_CHARACTER_CLASS;
				case 'd' -> flags |= Pattern.UNIX_LINES;
				case 'i' -> flags |= Pattern.CASE_INSENSITIVE;
				case 'm' -> flags |= Pattern.MULTILINE;
				case 's' -> flags |= Pattern.DOTALL;
				case 'u' -> flags |= Pattern.UNICODE_CASE;
				case 'x' -> flags |= Pattern.COMMENTS;
			}
		}

		String pattern = matcher.group(1);

		// check if pattern contains dangerous regex characters
		return Pattern.compile(pattern, flags);
	}

	static String toRegexString(Pattern pattern) {
		StringBuilder sb = new StringBuilder("/");
		sb.append(pattern.pattern());
		sb.append('/');
		int flags = pattern.flags();
		if ((flags & 1) != 0) {
			sb.append('d');
		}

		if ((flags & 2) != 0) {
			sb.append('i');
		}

		if ((flags & 4) != 0) {
			sb.append('x');
		}

		if ((flags & 8) != 0) {
			sb.append('m');
		}

		if ((flags & 32) != 0) {
			sb.append('s');
		}

		if ((flags & 64) != 0) {
			sb.append('u');
		}

		if ((flags & 256) != 0) {
			sb.append('U');
		}

		return sb.toString();
	}

	static void pad0(StringBuilder sb, int num) {
		if (num < 10) {
			sb.append('0');
		}

		sb.append(num);
	}

	static String percent(double d) {
		if (d <= 0D) {
			return "0%";
		} else if (d >= 1D) {
			return "100%";
		}

		if (d < 0.0001D) {
			return (d * 100D) + "%";
		}

		return ((int) (d * 10000L) / 100D) + "%";
	}

	static String percent(long value, long total) {
		return percent((double) value / (double) total);
	}
}
