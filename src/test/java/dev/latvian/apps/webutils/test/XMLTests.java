package dev.latvian.apps.webutils.test;

import dev.latvian.apps.ansi.ANSI;
import dev.latvian.apps.ansi.color.ANSIColor;
import dev.latvian.apps.ansi.log.Log;
import dev.latvian.apps.webutils.html.TagANSI;
import dev.latvian.apps.webutils.html.XMLParser;
import org.junit.jupiter.api.Test;

public class XMLTests {
	@Test
	public void xml() {
		var xml = XMLParser.parse("""
				<?xml version="1.0" encoding="UTF-8"?>
				<note>
				  <!--Comment-->
				  <to>Tove</to>
				  <from when="yesterday">Jani</from>
				  <heading>Reminder</heading>
				  <body a="b"c="d">Don't <b>forget</b> me this weekend!</body>
				</note>""", false);
		Log.info(ANSI.empty(3).append(ANSI.of("XML:").foreground(ANSIColor.of(218))).append(ANSI.LINE).append(TagANSI.of(xml, true)));
	}
}
