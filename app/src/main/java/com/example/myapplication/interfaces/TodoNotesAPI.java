package com.example.myapplication.interfaces;

import com.example.myapplication.model.TodoNotes;

import java.io.IOException;
import java.util.List;

public interface TodoNotesAPI {

    void sendTodo(TodoNotes todoNotes, TodoCallback<TodoNotes> todoCallback, TodoNotesDAO todoNotesDAO) throws IOException;
    void getTodoNotesListFromServer(TodoCallback<List<TodoNotes>> todoCallback, TodoNotesDAO todoNotesDAO) throws IOException;
    void initApp(TodoCallback<String> todoCallback, TodoNotesDAO todoNotesDAO) throws IOException;
    String getRequestMethod();
}