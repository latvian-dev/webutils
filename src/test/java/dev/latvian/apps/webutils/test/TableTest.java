package dev.latvian.apps.webutils.test;

import dev.latvian.apps.ansi.ANSI;
import dev.latvian.apps.ansi.log.Log;
import dev.latvian.apps.webutils.html.HTMLTable;
import dev.latvian.apps.webutils.html.PairedTag;
import dev.latvian.apps.webutils.html.TagANSI;
import org.junit.jupiter.api.Test;

public class TableTest {
	@Test
	public void table() {
		var table = new HTMLTable("Col 1", "Col 2", "Col 3");

		for (int i = 0; i < 10; i++) {
			var j = i;
			table.addRow(t -> t.string("Row " + j), t -> t.string("Value " + j), t -> t.string("Another value " + j));
		}

		Log.info(ANSI.empty().append(ANSI.LINE).append(TagANSI.of(new PairedTag("").add(table), true)));
	}
}
