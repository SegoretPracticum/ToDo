package com.example.myapplication;

import android.util.JsonWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TodoJsonWriter {
    private final static String TODO_TEXT = "todoText";

    public void writeJsonStream(OutputStream out, TodoNotes todoNotes) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        writer.setIndent("");
        writeMessage(writer, todoNotes);
        writer.close();
    }

        private void writeMessage(JsonWriter writer, TodoNotes todoNotes) throws IOException {
        writer.beginObject();
        writer.name(TODO_TEXT).value(todoNotes.getNoteText());
        writer.endObject();
    }
}