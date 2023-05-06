package dev.latvian.apps.webutils.test;

import dev.latvian.apps.webutils.ansi.Ansi;
import org.junit.jupiter.api.Test;

public class AnsiTests {
	@Test
	public void components() {
		var c = Ansi.of().append(Ansi.red("This text is red ").append(Ansi.of("this text is pink ").color(225).append(Ansi.of("error").white().redBg())).append(" this text should be red")).append(" and this should be reset");
		System.out.println(c);
		System.out.println();
		System.out.println(c.toDebugString());
		System.out.println();

		Ansi.log("! Hello");
	}
}
