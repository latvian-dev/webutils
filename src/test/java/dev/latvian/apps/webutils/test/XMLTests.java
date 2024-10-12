package dev.latvian.apps.webutils.test;

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
		Log.info("XML:\n" + TagANSI.of(xml, true));
	}
}
