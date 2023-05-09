package dev.latvian.apps.webutils.test;

import java.net.URI;

public class TestConfig {
	public URI database = null;
	public WebConfig web = new WebConfig();
	public DiscordConfig discord = new DiscordConfig();

	public static class WebConfig {
		public int port = 123456;
		public String title = "Test";
	}

	public static class DiscordConfig {
		public String botToken = "1a2b3c";
		public String clientId = "12345";
		public String clientSecret = "shh";
	}
}
