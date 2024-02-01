package dev.latvian.apps.webutils.test;

import dev.latvian.apps.webutils.ansi.Ansi;
import dev.latvian.apps.webutils.html.PairedTag;
import org.junit.jupiter.api.Test;

public class AnsiTests {
	@Test
	public void components() {
		var c = Ansi.of();
		c.append("GET 200 ");
		c.append(Ansi.cyan("Test").underline());
		c.append(" /test");
		var c1 = Ansi.of("WEB ").green().append(c);

		System.out.println(c1);
		System.out.println();
		System.out.println(c1.toDebugString());
		System.out.println();

		Ansi.log("! Hello");
	}

	@Test
	public void html() {
		var section = new PairedTag("section");
		section.classes("test test-2");
		section.img("img.png");
		section.p().string("Hello!").p().raw("abc").spanstr("yo").raw("test");
		Ansi.log("Tag:\n" + section.toAnsi(true));
	}
}
