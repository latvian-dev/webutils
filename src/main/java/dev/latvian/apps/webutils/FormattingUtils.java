package dev.latvian.apps.webutils;

import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface FormattingUtils {
	DecimalFormat LONG_FORMAT = new DecimalFormat("#,###");
	Pattern REGEX_PATTERN = Pattern.compile("/(.*)/([a-z]*)");
	Pattern REMOVE_WEIRD_CHARACTERS = Pattern.compile("[^\\w-.]");
	Pattern REMOVE_DASHES = Pattern.compile("-{2,}");
	Pattern NON_W_PATTERN = Pattern.compile("\\W");
	Pattern STACK_AT_PATTERN = Pattern.compile("([ \\t]+at )([\\w./$@]+)\\.([\\w/$]+)\\.(<init>|[\\w$]+)\\((Unknown Source|\\.dynamic|Native Method|[\\w.$]+:\\d+)\\)(?: ~?\\[.*:.*])?(?: \\{.*})?");
	Calendar FORMAT_CALENDAR = Calendar.getInstance();

	String[] MONTH_NAMES = {
			"Jan",
			"Feb",
			"Mar",
			"Apr",
			"May",
			"Jun",
			"Jul",
			"Aug",
			"Sep",
			"Oct",
			"Nov",
			"Dec",
	};

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

	static StringBuilder formatDate(StringBuilder sb, @Nullable Date date) {
		if (date == null) {
			return sb.append("Unknown");
		}

		FORMAT_CALENDAR.setTime(date);
		pad0(sb, FORMAT_CALENDAR.get(Calendar.DAY_OF_MONTH));
		sb.append('-');
		sb.append(MONTH_NAMES[FORMAT_CALENDAR.get(Calendar.MONTH)]);
		sb.append('-');
		sb.append(FORMAT_CALENDAR.get(Calendar.YEAR));
		sb.append(' ');
		pad0(sb, FORMAT_CALENDAR.get(Calendar.HOUR_OF_DAY));
		sb.append(':');
		pad0(sb, FORMAT_CALENDAR.get(Calendar.MINUTE));
		sb.append(':');
		pad0(sb, FORMAT_CALENDAR.get(Calendar.SECOND));
		return sb;
	}

	static StringBuilder relativeTime(StringBuilder sb, long millis) {
		if (millis == 0L) {
			return sb.append("now");
		}

		boolean neg = millis < 0L;
		millis = Math.abs(millis);

		long seconds = millis / 1000L;
		long days = seconds / 86400L;
		long hours = seconds / 3600L % 24L;
		long minutes = seconds / 60L % 60L;
		long secs = seconds % 60L;

		if (millis < 1000L) {
			sb.append(millis);
			sb.append("ms");
			sb.append(' ');
		} else {
			if (days > 0) {
				sb.append(days);
				sb.append('d');
				sb.append(' ');
			}

			if (days > 0 || hours > 0) {
				sb.append(hours);
				sb.append('h');
				sb.append(' ');
			}

			if (days == 0) {
				if (hours > 0 || minutes > 0) {
					sb.append(minutes);
					sb.append('m');
					sb.append(' ');
				}

				sb.append(secs);
				sb.append('s');
				sb.append(' ');
			}
		}

		sb.append(neg ? "ago" : "from now");
		return sb;
	}
}
