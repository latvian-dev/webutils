package dev.latvian.apps.webutils.html;

import java.util.ArrayList;
import java.util.List;

public class HTMLTable implements TagFunction {
	public static class Col {
		public final HTMLTable table;
		public final int x;
		private Cell label;
		private TagFunction applyToCell = null;

		private Col(HTMLTable table, int x) {
			this.table = table;
			this.x = x;
		}

		public Col applyToCell(TagFunction applyToCell) {
			this.applyToCell = applyToCell;
			return this;
		}

		public Col set(int y, TagFunction value) {
			table.set(x, y, value);
			return this;
		}
	}

	public static class Row {
		public final HTMLTable table;
		public final int y;
		public final List<Cell> cells;
		private TagFunction apply = null;
		private TagFunction applyToCell = null;

		public Row(HTMLTable table, int y) {
			this.table = table;
			this.y = y;
			this.cells = new ArrayList<>(2);
		}

		public Row apply(TagFunction apply) {
			this.apply = apply;
			return this;
		}

		public Row applyToCell(TagFunction applyToCell) {
			this.applyToCell = applyToCell;
			return this;
		}

		public Row set(int x, TagFunction value) {
			cells.get(x).set(value);
			return this;
		}
	}

	public static class Cell {
		public final Col col;
		public final Row row;
		private TagFunction value = TagFunction.IDENTITY;

		private Cell(Col col, Row row) {
			this.col = col;
			this.row = row;
		}

		public Cell set(TagFunction value) {
			this.value = value;
			return this;
		}
	}

	public final Row headRow;
	private final List<Col> cols;
	private final List<Row> rows;

	public HTMLTable() {
		this.headRow = new Row(this, -1);
		this.cols = new ArrayList<>(2);
		this.rows = new ArrayList<>(2);
	}

	public HTMLTable(TagFunction... labels) {
		this();

		for (var label : labels) {
			addCol(label);
		}
	}

	public HTMLTable(String... labels) {
		this();

		for (var label : labels) {
			addCol(t -> t.string(label));
		}
	}

	public Col col(int x) {
		return cols.get(x);
	}

	public int cols() {
		return cols.size();
	}

	public Row row(int y) {
		return rows.get(y);
	}

	public int rows() {
		return rows.size();
	}

	public Col addCol(TagFunction label) {
		var col = new Col(this, cols.size());
		cols.add(col);

		col.label = new Cell(col, headRow);
		headRow.cells.add(col.label);
		col.label.value = label;

		for (var row : rows) {
			row.cells.add(new Cell(col, row));
		}

		return col;
	}

	public Row addRow() {
		var row = new Row(this, rows.size());
		rows.add(row);

		for (var col : cols) {
			row.cells.add(new Cell(col, row));
		}

		return row;
	}

	public void addRow(TagFunction... h) {
		var r = addRow();

		for (int i = 0; i < h.length; i++) {
			r.set(i, h[i]);
		}
	}

	public HTMLTable set(int x, int y, TagFunction value) {
		rows.get(y).set(x, value);
		return this;
	}

	@Override
	public void acceptTag(Tag tag) {
		var table = tag.table();
		var theadTag = table.thead();
		var headTag = theadTag.tr().add(headRow.apply);

		for (var cell : headRow.cells) {
			headTag.th().add(cell.value).add(headRow.applyToCell).add(cell.col.applyToCell);
		}

		var tbodyTag = table.tbody();

		for (var row : rows) {
			var rowTag = tbodyTag.tr().add(row.apply);

			for (var cell : row.cells) {
				rowTag.td().add(cell.value).add(row.applyToCell).add(cell.col.applyToCell);
			}
		}
	}
}
