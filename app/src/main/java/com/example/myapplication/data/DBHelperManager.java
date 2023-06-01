package com.example.myapplication.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.myapplication.Item.TodoNotes;
import com.example.myapplication.interfaces.TodoNotesDAO;

import java.util.ArrayList;

public class DBHelperManager implements TodoNotesDAO {
    private final TodoNotesDBHelper todoNotesDBHelper;
    private final TodoListDBHelper todoListDBHelper;


    public DBHelperManager(TodoNotesDBHelper todoNotesDBHelper, TodoListDBHelper todoListDBHelper) {
        this.todoNotesDBHelper = todoNotesDBHelper;
        this.todoListDBHelper = todoListDBHelper;
    }

    @Override
    public void updateDB(TodoNotes todoNotes, String appID) {
        SQLiteDatabase dataBase = todoNotesDBHelper.getWritableDatabase();
        String findTodoByIDQuery = "SELECT " + TodoDBContract.TodoList.COLUMN_TODO_ID +
                " FROM " + TodoDBContract.TodoList.TABLE_NAME + " WHERE " +
                TodoDBContract.TodoList.COLUMN_TODO_ID + " = '%s'";
        Cursor cursor = dataBase.rawQuery(String.format(findTodoByIDQuery,todoNotes.getTodoId()),null);
        String todoID = "";
        while (cursor.moveToNext()) {
            todoID = cursor.getString(
                    cursor.getColumnIndexOrThrow(TodoDBContract.TodoList.COLUMN_TODO_ID));
        }
        cursor.close();
        if (todoID.equals("")) {
            String addTodoQuery = "INSERT INTO " + TodoDBContract.TodoList.TABLE_NAME + " (" +
                    TodoDBContract.TodoList.COLUMN_TODO_TEXT + ", " + TodoDBContract.TodoList.COLUMN_TODO_ID +
                    ", " + TodoDBContract.TodoList.COLUMN_APP_ID + ") VALUES ( '%s', '%s', '%s' )";
            dataBase.execSQL(String.format(addTodoQuery,todoNotes.getNoteText(),todoNotes.getTodoId(), appID));
        } else {
            String editTodoQuery = "UPDATE " + TodoDBContract.TodoList.TABLE_NAME + " SET " +
                    TodoDBContract.TodoList.COLUMN_TODO_TEXT + " = '%s' WHERE " + TodoDBContract.TodoList.COLUMN_TODO_ID +
                    " = '%s'";
            dataBase.execSQL(String.format(editTodoQuery,todoNotes.getNoteText(),todoID));
        }
    }

    @Override
    public ArrayList<TodoNotes> getTodoNotesList(String appID) {
        SQLiteDatabase dataBase = todoNotesDBHelper.getReadableDatabase();
        String getTodoListQuery = "SELECT " + TodoDBContract.TodoList.COLUMN_TODO_ID + ", " +
                TodoDBContract.TodoList.COLUMN_TODO_TEXT + " FROM " + TodoDBContract.TodoList.TABLE_NAME +
                " WHERE " + TodoDBContract.TodoList.COLUMN_APP_ID + " = '%s'";
        ArrayList<TodoNotes> todoList = new ArrayList<>();
        Cursor cursor = dataBase.rawQuery(String.format(getTodoListQuery,appID), null);
        while (cursor.moveToNext()) {
            String todoID = cursor.getString(
                    cursor.getColumnIndexOrThrow(TodoDBContract.TodoList.COLUMN_TODO_ID));
            String todoText = cursor.getString(cursor.getColumnIndexOrThrow(TodoDBContract.TodoList.COLUMN_TODO_TEXT));
            todoList.add(new TodoNotes(todoText, todoID));
        }
        cursor.close();
        return todoList;
    }

    @Override
    public String getAppID() {
        SQLiteDatabase dataBase = todoListDBHelper.getReadableDatabase();
        String findAppID = "SELECT "+ TodoDBContract.TodoListID.COLUMN_TODOLIST_ID + " FROM " +
                TodoDBContract.TodoListID.TABLE_NAME + " WHERE " + BaseColumns._ID + " = '%s'";
        String appIdNumber = "1";
        Cursor cursor = dataBase.rawQuery(String.format(findAppID, appIdNumber),null);
        String appID = null;
        while (cursor.moveToNext()) {
            appID = cursor.getString(
                    cursor.getColumnIndexOrThrow(TodoDBContract.TodoListID.COLUMN_TODOLIST_ID));
        }
        cursor.close();
        return appID;
    }

    @Override
    public void setAppIdFromServer(String appID) {
        SQLiteDatabase dataBase = todoListDBHelper.getWritableDatabase();
        String addAppId = "INSERT INTO " + TodoDBContract.TodoListID.TABLE_NAME + " (" +
                TodoDBContract.TodoListID.COLUMN_TODOLIST_ID + ") " + "VALUES ('%s')";
        dataBase.execSQL(String.format(addAppId, appID));
    }
}