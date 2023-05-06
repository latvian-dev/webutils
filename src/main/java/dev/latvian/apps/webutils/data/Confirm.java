package dev.latvian.apps.webutils.data;

public enum Confirm {
	QUERY,
	YES,
	NO;

	public static Confirm of(String string) {
		return switch (string) {
			case "yes" -> YES;
			case "no" -> NO;
			default -> QUERY;
		};
	}

	public static Confirm of(String[] path, int index) {
		return path.length > index ? of(path[index]) : QUERY;
	}
}
