package com.example.myapplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class HttpConnect {

    private HttpURLConnection httpURLConnection;
    private final TodoJsonReader todoJsonReader = new TodoJsonReader();
    private final TodoJsonWriter todoJsonWriter = new TodoJsonWriter();
    private static final String URL_SERVER = "https://segoret-todo-default-rtdb.firebaseio.com/";
    private static final String JSON = ".json";

    public void sendTodo(TodoNotes todoNotes, TodoCallback<TodoNotes> todoCallback) throws IOException {
        try {
            String todoID = todoNotes.getIdTodo();
            if (todoID == null) {
                httpURLConnection = (HttpURLConnection) new URL(URL_SERVER + JSON).openConnection();
                httpURLConnection.setRequestMethod("POST");
            } else {
                httpURLConnection = (HttpURLConnection) new URL(URL_SERVER + todoID + "/" + JSON).openConnection();
                httpURLConnection.setRequestMethod("PUT");
            }
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            OutputStream out = httpURLConnection.getOutputStream();
            todoJsonWriter.writeJsonStream(out, todoNotes);
            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                InputStream inputStream = httpURLConnection.getInputStream();
                if (todoNotes.getIdTodo() == null) {
                    String idServer = todoJsonReader.serverID(inputStream);
                    todoNotes.setIdTodo(idServer);
                    todoCallback.onSuccess(todoNotes);
                } else {
                    todoCallback.onSuccess(todoNotes);
                }
            }
        } catch (IOException e) {
            todoCallback.onFail();
        } finally {
            httpURLConnection.disconnect();
        }
    }

    public void getTodoNotesListFromServer(TodoCallback<List<TodoNotes>> todoCallback) throws IOException {
        try {
            httpURLConnection = (HttpURLConnection) new URL(URL_SERVER + JSON).openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                InputStream inputStream = httpURLConnection.getInputStream();
                List<TodoNotes> todoNotesList = todoJsonReader.readJsonStream(inputStream);
                todoCallback.onSuccess(todoNotesList);
            }
        } catch (IOException e) {
            todoCallback.onFail();
        } finally {
            httpURLConnection.disconnect();
        }
    }
}