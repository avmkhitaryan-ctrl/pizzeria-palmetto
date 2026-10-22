
package com.pizzeria.common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class JsonWriter {

    private static final JsonWriter INSTANCE = new JsonWriter();

    private JsonWriter() {
    }

    public static JsonWriter getInstance() {
        return INSTANCE;
    }

    public JSONArray readArray(String fileName) throws IOException {
        Path path = Paths.get(fileName);

        if (Files.notExists(path)) {
            return new JSONArray();
        }

        String content = Files.readString(path, StandardCharsets.UTF_8);

        if (content.isBlank()) {
            return new JSONArray();
        }

        return new JSONArray(content);
    }

    public void writeArray(String fileName, JSONArray data)
            throws IOException {

        Path path = Paths.get(fileName);
        Path parent = path.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(
                path,
                data.toString(4),
                StandardCharsets.UTF_8
        );
    }

    public JSONObject readObject(String fileName) throws IOException {
        Path path = Paths.get(fileName);

        if (Files.notExists(path)) {
            return new JSONObject();
        }

        String content = Files.readString(path, StandardCharsets.UTF_8);

        if (content.isBlank()) {
            return new JSONObject();
        }

        return new JSONObject(content);
    }

    public void writeObject(String fileName, JSONObject data)
            throws IOException {

        Path path = Paths.get(fileName);
        Path parent = path.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(
                path,
                data.toString(4),
                StandardCharsets.UTF_8
        );
    }
}
