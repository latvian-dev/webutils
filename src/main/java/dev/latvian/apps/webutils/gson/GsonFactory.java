package dev.latvian.apps.webutils.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.latvian.apps.webutils.MiscUtils;
import dev.latvian.apps.webutils.data.HexId32;
import dev.latvian.apps.webutils.data.HexId64;

import java.io.IOException;

public record GsonFactory<T>(Class<T> type, TypeAdapter<T> adapter) implements TypeAdapterFactory {
	public static final GsonFactory<HexId32> HEX32 = new GsonFactory<>(HexId32.class, new TypeAdapter<>() {
		@Override
		public void write(JsonWriter out, HexId32 value) throws IOException {
			out.value(value.toString());
		}

		@Override
		public HexId32 read(JsonReader in) throws IOException {
			return HexId32.of(in.nextString());
		}
	});

	public static final GsonFactory<HexId64> HEX64 = new GsonFactory<>(HexId64.class, new TypeAdapter<>() {
		@Override
		public void write(JsonWriter out, HexId64 value) throws IOException {
			out.value(value.toString());
		}

		@Override
		public HexId64 read(JsonReader in) throws IOException {
			return HexId64.of(in.nextString());
		}
	});

	@Override
	public <A> TypeAdapter<A> create(Gson gson, TypeToken<A> typeToken) {
		return type == typeToken.getRawType() ? MiscUtils.cast(adapter) : null;
	}
}
