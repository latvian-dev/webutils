package dev.latvian.apps.webutils.gson;

import com.google.gson.JsonElement;

@FunctionalInterface
public interface GsonSerializable {
	JsonElement toJson();
}
