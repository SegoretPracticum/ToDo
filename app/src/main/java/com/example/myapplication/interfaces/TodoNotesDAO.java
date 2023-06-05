package com.example.myapplication.interfaces;

import com.example.myapplication.Item.TodoNotes;

import java.util.ArrayList;
import java.util.List;

public interface TodoNotesDAO {

    void addTodoToDB(TodoNotes todoNotes, String appID);
    void editTodoInDB(TodoNotes todoNotes, String appID);
    ArrayList<TodoNotes> getTodoNotesList(String appID);
    String getAppID();
    void setAppIdFromServer(String appID);
    void updateTodoList(List<TodoNotes> todoNotesList);
}