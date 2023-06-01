package com.example.myapplication.interfaces;

import com.example.myapplication.Item.TodoNotes;

import java.util.ArrayList;

public interface TodoNotesDAO {

    void updateDB(TodoNotes todoNotes, String appID);
    ArrayList<TodoNotes> getTodoNotesList(String appID);
    String getAppID();
    void setAppIdFromServer(String appID);
}