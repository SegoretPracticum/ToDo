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
    private static final String REQUEST_PUT = "PUT";
    private static final String REQUEST_GET = "GET";
    private static final String URL_SERVER = "https://segoret-todo-default-rtdb.firebaseio.com/";
    private static final String JSON = ".json";
    private String todoID;

    public void sendTodo(TodoNotes todoNotes, TodoCallback<TodoNotes> todoCallback, String requestMethod) throws IOException {
        try {
            todoID = todoNotes.getTodoId();
            connectionSettings(requestMethod);
            OutputStream out = httpURLConnection.getOutputStream();
            todoJsonWriter.writeJsonStream(out, todoNotes);
            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                InputStream inputStream = httpURLConnection.getInputStream();
                if (todoNotes.getTodoId() == null) {
                    String idServer = todoJsonReader.serverID(inputStream);
                    todoNotes.setTodoId(idServer);
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

    public void getTodoNotesListFromServer(TodoCallback<List<TodoNotes>> todoCallback, String requestMethod) throws IOException {
        try {
            connectionSettings(requestMethod);
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

    private void connectionSettings(String requestMethod) throws IOException {
        if (requestMethod.equals(REQUEST_PUT)) {
        httpURLConnection = (HttpURLConnection) new URL(URL_SERVER + todoID + "/" + JSON).openConnection();
        }
        else {
        httpURLConnection = (HttpURLConnection) new URL(URL_SERVER + JSON).openConnection();
        }
        httpURLConnection.setRequestMethod(requestMethod);
        if (!requestMethod.equals(REQUEST_GET)) {
            httpURLConnection.setDoOutput(true);
        }
        if (requestMethod.equals(REQUEST_GET)){
        httpURLConnection.setDoInput(true);
        }
        httpURLConnection.connect();
    }
}