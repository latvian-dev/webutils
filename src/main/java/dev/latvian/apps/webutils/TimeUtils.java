package dev.latvian.apps.webutils;

import org.jetbrains.annotations.Nullable;

import java.util.Calendar;
import java.util.Date;

public interface TimeUtils {
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

	Calendar FORMAT_CALENDAR = Calendar.getInstance();

	static StringBuilder formatDate(StringBuilder sb, @Nullable Date date) {
		if (date == null) {
			return sb.append("Unknown");
		}

		FORMAT_CALENDAR.setTime(date);
		FormattingUtils.pad0(sb, FORMAT_CALENDAR.get(Calendar.DAY_OF_MONTH));
		sb.append('-');
		sb.append(MONTH_NAMES[FORMAT_CALENDAR.get(Calendar.MONTH)]);
		sb.append('-');
		sb.append(FORMAT_CALENDAR.get(Calendar.YEAR));
		sb.append(' ');
		FormattingUtils.pad0(sb, FORMAT_CALENDAR.get(Calendar.HOUR_OF_DAY));
		sb.append(':');
		FormattingUtils.pad0(sb, FORMAT_CALENDAR.get(Calendar.MINUTE));
		sb.append(':');
		FormattingUtils.pad0(sb, FORMAT_CALENDAR.get(Calendar.SECOND));
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

	static String simpleTimeString(long timeInSeconds) {
		StringBuilder builder = new StringBuilder();

		long days = timeInSeconds / 86400L;
		long hours = (timeInSeconds / 3600L) % 24L;
		long minutes = (timeInSeconds / 60L) % 60L;
		long seconds = timeInSeconds % 60L;

		if (days > 0L) {
			builder.append(days);
			builder.append(':');
		}

		if (hours < 10L) {
			builder.append('0');
		}

		builder.append(hours);

		builder.append(':');

		if (minutes < 10L) {
			builder.append('0');
		}

		builder.append(minutes);

		builder.append(':');

		if (seconds < 10L) {
			builder.append('0');
		}

		builder.append(seconds);
		return builder.toString();
	}

	static String prettyTimeString(long seconds) {
		if (seconds <= 0L) {
			return "0 seconds";
		}

		StringBuilder builder = new StringBuilder();
		prettyTimeString(builder, seconds, true);
		return builder.toString();
	}

	private static void prettyTimeString(StringBuilder builder, long seconds, boolean addAnother) {
		if (seconds <= 0L) {
			return;
		} else if (!addAnother) {
			builder.append(" and ");
		}

		if (seconds < 60L) {
			builder.append(seconds);
			builder.append(seconds == 1L ? " second" : " seconds");
		} else if (seconds < 3600L) {
			builder.append(seconds / 60L);
			builder.append(seconds / 60L == 1L ? " minute" : " minutes");

			if (addAnother) {
				prettyTimeString(builder, seconds % 60L, false);
			}
		} else if (seconds < 86400L) {
			builder.append(seconds / 3600L);
			builder.append(seconds / 3600L == 1L ? " hour" : " hours");

			if (addAnother) {
				prettyTimeString(builder, seconds % 3600L, false);
			}
		} else {
			builder.append(seconds / 86400L);
			builder.append(seconds / 86400L == 1L ? " day" : " days");

			if (addAnother) {
				prettyTimeString(builder, seconds % 86400L, false);
			}
		}
	}
}
