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
    private static final String REQUEST_POST = "POST";
    private final static String SLASH = "/";
    private static final String EMPTY_LINK = "";
    private static final String URL_SERVER = "https://segoret-todo-default-rtdb.firebaseio.com/TodoApp/";
    private static final String JSON = ".json";

    public void sendTodo(TodoNotes todoNotes, TodoCallback<TodoNotes> todoCallback, AppIdentification appIdentification) throws IOException {
        try {
            String appLink = appIdentification.getAppID() + SLASH;
            String todoID = todoNotes.getTodoId();
            if (todoID == null) {
                connectionSettings(REQUEST_POST, appLink, EMPTY_LINK, true);
            } else {
                String todoLink = todoID + SLASH;
                connectionSettings(REQUEST_PUT, appLink, todoLink, true);
            }
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

    public void getTodoNotesListFromServer(TodoCallback<List<TodoNotes>> todoCallback, AppIdentification appIdentification) throws IOException {
        try {
            String appLink = appIdentification.getAppID() + SLASH;
            connectionSettings(REQUEST_GET, appLink, EMPTY_LINK, false);
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

    public void initApp(TodoCallback<String> todoCallback, AppIdentification appIdentification) throws IOException {
        try {
            connectionSettings(REQUEST_POST, EMPTY_LINK, EMPTY_LINK, true);
            OutputStream out = httpURLConnection.getOutputStream();
            todoJsonWriter.writeInitJSON(out);
            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()) {
                InputStream inputStream = httpURLConnection.getInputStream();
                String appID = todoJsonReader.serverID(inputStream);
                appIdentification.setAppIdFromServer(appID);
                todoCallback.onSuccess(appID);
            }
        } catch (IOException e) {
            todoCallback.onFail();
        } finally {
            httpURLConnection.disconnect();
        }
    }

    private void connectionSettings(String requestMethod, String appLink, String todoLink, Boolean setDoOutput) throws IOException {
        httpURLConnection = (HttpURLConnection) new URL(URL_SERVER + appLink + todoLink + JSON).openConnection();
        httpURLConnection.setRequestMethod(requestMethod);
        httpURLConnection.setDoOutput(setDoOutput);
        httpURLConnection.setDoInput(true);
        httpURLConnection.connect();
    }
}