package com.example.myapplication.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.interfaces.TodoMotesDAO;
import com.example.myapplication.Item.TodoNotes;


import java.util.ArrayList;

public class DBHelperManager implements TodoMotesDAO {
    TodoNotesDBHelper todoNotesDBHelper;

    public DBHelperManager(TodoNotesDBHelper todoNotesDBHelper) {
        this.todoNotesDBHelper = todoNotesDBHelper;
    }

    @Override
    public void updateDB(TodoNotes todoNotes, String appID) {
        SQLiteDatabase dataBase = todoNotesDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TodoDBContract.TodoList.COLUMN_TODO_ID, todoNotes.getTodoId());
        values.put(TodoDBContract.TodoList.COLUMN_TODO_TEXT, todoNotes.getNoteText());
        values.put(TodoDBContract.TodoList.COLUMN_APP_ID, appID);
        String selection = TodoDBContract.TodoList.COLUMN_TODO_ID + " LIKE ?";
        String[] selectionArgs = {todoNotes.getTodoId()};
        String[] projection = {
                TodoDBContract.TodoList.COLUMN_TODO_ID
        };
        Cursor cursor = dataBase.query(
                TodoDBContract.TodoList.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        String todoID = "";
        while (cursor.moveToNext()) {
            todoID = cursor.getString(
                    cursor.getColumnIndexOrThrow(TodoDBContract.TodoList.COLUMN_TODO_ID));
        }
        cursor.close();
        if (todoID.equals("")) {
            dataBase.insert(TodoDBContract.TodoList.TABLE_NAME, null, values);
        } else {
            dataBase.update(TodoDBContract.TodoList.TABLE_NAME, values, selection, selectionArgs);
        }
    }

    @Override
    public ArrayList<TodoNotes> getTodoNotesList(String appID) {
        SQLiteDatabase dataBase = todoNotesDBHelper.getReadableDatabase();
        ArrayList<TodoNotes> todoList = new ArrayList<>();
        String[] projection = {
                TodoDBContract.TodoList.COLUMN_TODO_ID,
                TodoDBContract.TodoList.COLUMN_TODO_TEXT
        };
        String selection = TodoDBContract.TodoList.COLUMN_APP_ID + " = ?";
        String[] selectionArgs = {appID};
        Cursor cursor = dataBase.query(
                TodoDBContract.TodoList.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            String todoID = cursor.getString(
                    cursor.getColumnIndexOrThrow(TodoDBContract.TodoList.COLUMN_TODO_ID));
            String todoText = cursor.getString(cursor.getColumnIndexOrThrow(TodoDBContract.TodoList.COLUMN_TODO_TEXT));
            todoList.add(new TodoNotes(todoText, todoID));
        }
        cursor.close();
        return todoList;
    }
}