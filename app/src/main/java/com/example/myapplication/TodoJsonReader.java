package com.example.myapplication;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TodoJsonReader {
    private final static String TODO_TEXT = "todoText";

    public List<TodoNotes> readJsonStream(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return readTodoNotesList(reader);
        }
    }

    private List<TodoNotes> readTodoNotesList(JsonReader reader) throws IOException {
        List<TodoNotes> todoNotesList = new ArrayList<>();
        while (reader.hasNext()) {
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
            }
            if (reader.peek() == JsonToken.END_OBJECT) {
                reader.endObject();
            }
            if (reader.peek() == JsonToken.NAME) {
                String name = reader.nextName();
                if (!name.equals(TODO_TEXT)) {
                    String todoText = readTodoText(reader);
                    TodoNotes todoNotes = new TodoNotes(todoText, name);
                    todoNotesList.add(todoNotes);
                } else {
                    reader.skipValue();
                }
            }
        }
        return todoNotesList;
    }

    public String serverID(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String serverID = null;
        while (reader.hasNext()) {
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();
            } else if (reader.peek() == JsonToken.END_OBJECT) {
                reader.endObject();
            }
            if (reader.peek() == JsonToken.NAME) {
                String name = reader.nextName();
                if (name.equals("name")) {
                    serverID = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
        }
        return serverID;
    }

    private String readTodoText(JsonReader reader) throws IOException {
        String text = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(TODO_TEXT)) {
                text = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return text;
    }
}