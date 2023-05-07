package dev.latvian.apps.webutils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.apps.webutils.net.FileResponse;
import dev.latvian.apps.webutils.net.Response;
import io.javalin.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public interface JsonResponse {
	Response SUCCESS = of(HttpStatus.OK, GsonUtils.object("success", new JsonPrimitive(true)));

	static Response of(HttpStatus status, JsonElement json) {
		return FileResponse.of(status, "application/json; charset=utf-8", GsonUtils.GSON.toJson(json).getBytes(StandardCharsets.UTF_8));
	}

	static Response of(JsonElement json) {
		return of(HttpStatus.OK, json);
	}

	static Response object(final Consumer<JsonObject> consumer) {
		var json = new JsonObject();
		consumer.accept(json);
		return of(HttpStatus.OK, json);
	}

	static Response array(final Consumer<JsonArray> consumer) {
		var json = new JsonArray();
		consumer.accept(json);
		return of(HttpStatus.OK, json);
	}
}