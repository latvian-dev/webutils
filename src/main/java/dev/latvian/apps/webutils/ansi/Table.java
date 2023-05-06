package dev.latvian.apps.webutils.ansi;

import dev.latvian.apps.webutils.html.PairedTag;
import dev.latvian.apps.webutils.html.Tag;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Table {
	public static class Cell {
		private AnsiComponent value = Ansi.of("");
		private Tag tag = null;
		private char padding = ' ';
		private boolean alignRight = false;

		private Cell() {
		}

		public Cell value(Object v) {
			value = Ansi.of(v);
			return this;
		}

		public Tag tag() {
			tag = new PairedTag("");
			return tag;
		}

		public Cell padding(char p) {
			padding = p;
			return this;
		}

		public Cell alignRight() {
			alignRight = true;
			return this;
		}

		public void fill(Tag cellTag) {
			if (tag != null) {
				cellTag.add(tag);
			} else {
				cellTag.string(value.toUnformattedString());
			}
		}

		public String unformattedValue() {
			return tag == null ? value.toUnformattedString() : tag.getRawContent();
		}

		public int unformattedLength() {
			return unformattedValue().length();
		}
	}

	public static String escapeCSVSpecialCharacters(@Nullable String data) {
		if (data == null) {
			return "null";
		}

		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			data = data.replace("\"", "\"\"");
			escapedData = "\"" + data + "\"";
		}
		return escapedData;

		//return "\"" + data.replace("\"", "\"\"") + "\"";
	}

	public final Cell[] head;
	public final List<Cell[]> rows;

	public Table(String... h) {
		head = new Cell[h.length];
		rows = new ArrayList<>();

		for (int i = 0; i < head.length; i++) {
			head[i] = new Cell().value(Ansi.bold(h[i]));
		}
	}

	public Cell[] addRow() {
		Cell[] cells = new Cell[head.length];

		for (int i = 0; i < head.length; i++) {
			cells[i] = new Cell();
		}

		rows.add(cells);
		return cells;
	}

	public void addRow(Object... h) {
		Cell[] c = addRow();

		for (int i = 0; i < h.length; i++) {
			c[i].value(h[i]);
		}
	}

	private void printLine(List<String> lines, boolean colors, StringBuilder sb, int[] max, int type) {
		sb.setLength(0);
		sb.append(type == 0 ? '┌' : type == 1 ? '├' : '└');

		for (int i : max) {
			for (int k = 0; k < i + 2; k++) {
				sb.append('─');
			}

			sb.append(type == 0 ? '┬' : type == 1 ? '┼' : '┴');
		}

		sb.setCharAt(sb.length() - 1, type == 0 ? '┐' : type == 1 ? '┤' : '┘');

		if (colors) {
			lines.add(sb.toString());
		} else {
			lines.add(Ansi.PATTERN.matcher(sb.toString()).replaceAll(""));
		}
	}

	private void printRow(List<String> lines, boolean colors, StringBuilder sb, int[] max, Cell[] cells) {
		sb.setLength(0);
		sb.append('│');

		for (int i = 0; i < max.length; i++) {
			sb.append(' ');

			int l = max[i] - cells[i].unformattedLength();

			if (cells[i].alignRight) {
				for (int j = 0; j < l; j++) {
					sb.append(cells[i].padding);
				}
			}

			sb.append(cells[i].value);
			sb.append(AnsiCode.RESET);

			if (!cells[i].alignRight) {
				for (int j = 0; j < l; j++) {
					sb.append(cells[i].padding);
				}
			}

			sb.append(" │");
		}

		if (colors) {
			lines.add(sb.toString());
		} else {
			lines.add(Ansi.PATTERN.matcher(sb.toString()).replaceAll(""));
		}
	}

	public List<String> getCSVLines(boolean includeHead) {
		List<String> lines = new ArrayList<>();

		if (includeHead) {
			addCSVLine(lines, head);
		}

		for (Cell[] c : rows) {
			addCSVLine(lines, c);
		}

		return lines;
	}

	public byte[] getCSVBytes(boolean includeHead) {
		return String.join("\n", getCSVLines(includeHead)).getBytes(StandardCharsets.UTF_8);
	}

	private void addCSVLine(List<String> lines, Cell[] cells) {
		lines.add(Arrays.stream(cells).map(c -> escapeCSVSpecialCharacters(c.unformattedValue())).collect(Collectors.joining(",")));
	}

	public List<String> getLines(boolean colors) {
		List<String> lines = new ArrayList<>();
		if (head.length == 0) {
			lines.add("┌───────┐");
			lines.add("│ Empty │");
			lines.add("└───────┘");
			return lines;
		}

		int[] max = new int[head.length];

		for (int i = 0; i < head.length; i++) {
			max[i] = head[i].unformattedLength();
		}

		for (Cell[] cells : rows) {
			for (int i = 0; i < head.length; i++) {
				max[i] = Math.max(max[i], cells[i].unformattedLength());
			}
		}

		StringBuilder sb = new StringBuilder();
		printLine(lines, colors, sb, max, 0);
		printRow(lines, colors, sb, max, head);
		printLine(lines, colors, sb, max, 1);

		for (Cell[] cells : rows) {
			printRow(lines, colors, sb, max, cells);
		}

		printLine(lines, colors, sb, max, 2);
		return lines;
	}

	public void print() {
		Ansi.log("\n" + String.join("\n", getLines(true)));
	}

	public Tag toTag() {
		Tag table = new PairedTag("table");

		var theadTag = table.thead();
		var headTag = theadTag.tr();

		for (var cell : head) {
			cell.fill(headTag.th());
		}

		var tbodyTag = table.tbody();

		for (var cells : rows) {
			var rowTag = tbodyTag.tr();

			for (var cell : cells) {
				cell.fill(rowTag.td());
			}
		}

		return table;
	}
}