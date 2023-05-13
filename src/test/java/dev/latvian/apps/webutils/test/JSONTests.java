package dev.latvian.apps.webutils.test;

import dev.latvian.apps.webutils.json.JSON;
import dev.latvian.apps.webutils.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JSONTests {
	@Test
	public void serialize() {
		var map = JSONObject.of("a", 10).append("b", "Hi").append("c", List.of(1, 2, 3));

		Assertions.assertEquals(JSON.DEFAULT.write(map), """
				{"a":10,"b":"Hi","c":[1,2,3]}""");
	}

	@Test
	public void deserialize() {
		var map = JSON.DEFAULT.read("""
				{ "a"   : 10    , "b":"Hi","c":[  1,  2,3  ] }""").readObject();

		Assertions.assertEquals(map.asArray("c").asInt(2), 3);
	}

	@Test
	public void adapt() {
		var config = JSON.DEFAULT.read("""
				{"database":"https://lat:test@fakedb.com:1234/","discord":[{"clientId":"7"}]}""").adapt(TestConfig.class);

		Assertions.assertEquals(config.database.toString(), "https://lat:test@fakedb.com:1234/");
		Assertions.assertEquals(config.discord[0].clientId, "7");
		Assertions.assertEquals(config.discord[0].clientSecret, "shh");
	}
}
