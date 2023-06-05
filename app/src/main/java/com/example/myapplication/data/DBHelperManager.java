package com.example.myapplication.data;

import static com.example.myapplication.repository.TodoRepository.TODOLIST_NUMBER;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Item.TodoNotes;
import com.example.myapplication.interfaces.TodoNotesDAO;

import java.util.ArrayList;
import java.util.List;

public class DBHelperManager implements TodoNotesDAO {
    private final static String EDIT_TODO_QUERY = "UPDATE " + TodoDBContract.TodoList.TABLE_NAME + " SET " +
            TodoDBContract.TodoList.COLUMN_TODO_TEXT + " = '%s' WHERE " + TodoDBContract.TodoList.COLUMN_TODO_ID +
            " = '%s'";
    private final static String ADD_TODO_QUERY = "INSERT INTO " + TodoDBContract.TodoList.TABLE_NAME + " (" +
            TodoDBContract.TodoList.COLUMN_TODO_TEXT + ", " + TodoDBContract.TodoList.COLUMN_TODO_ID +
            ", " + TodoDBContract.TodoList.COLUMN_APP_ID + ") VALUES ( '%s', '%s', '%s' )";
    private final static String GET_TODO_LIST_QUERY = "SELECT " + TodoDBContract.TodoList.COLUMN_TODO_ID + ", " +
            TodoDBContract.TodoList.COLUMN_TODO_TEXT + " FROM " + TodoDBContract.TodoList.TABLE_NAME +
            " WHERE " + TodoDBContract.TodoList.COLUMN_APP_ID + " = '%s'";
    private final static String FIND_APP_ID = "SELECT " + TodoDBContract.TodoListID.COLUMN_TODOLIST_ID + " FROM " +
            TodoDBContract.TodoListID.TABLE_NAME;
    private final static String UPDATE_LIST_QUERY = "REPLACE INTO " + TodoDBContract.TodoList.TABLE_NAME + " (" +
            TodoDBContract.TodoList.COLUMN_TODO_ID + ", " + TodoDBContract.TodoList.COLUMN_TODO_TEXT + ", "
            + TodoDBContract.TodoList.COLUMN_APP_ID + ") VALUES ( '%s', '%s', '%s')";
    private final static String ADD_APP_ID = "INSERT INTO " + TodoDBContract.TodoListID.TABLE_NAME + " (" +
            TodoDBContract.TodoListID.COLUMN_TODOLIST_ID + ") " + "VALUES ('%s')";
    private final TodoNotesDBHelper todoNotesDBHelper;
    private final TodoListDBHelper todoListDBHelper;

    public DBHelperManager(TodoNotesDBHelper todoNotesDBHelper, TodoListDBHelper todoListDBHelper) {
        this.todoNotesDBHelper = todoNotesDBHelper;
        this.todoListDBHelper = todoListDBHelper;
    }

    @Override
    public void addTodoToDB(TodoNotes todoNotes, String appID) {
        SQLiteDatabase dataBase = todoNotesDBHelper.getWritableDatabase();
        dataBase.execSQL(String.format(ADD_TODO_QUERY, todoNotes.getNoteText(), todoNotes.getTodoId(), appID));
    }

    @Override
    public void editTodoInDB(TodoNotes todoNotes, String appID) {
        SQLiteDatabase dataBase = todoNotesDBHelper.getWritableDatabase();
        dataBase.execSQL(String.format(EDIT_TODO_QUERY, todoNotes.getNoteText(), todoNotes.getTodoId()));
    }

    @Override
    public ArrayList<TodoNotes> getTodoNotesList(String appID) {
        SQLiteDatabase dataBase = todoNotesDBHelper.getReadableDatabase();
        ArrayList<TodoNotes> todoList = new ArrayList<>();
        try (Cursor cursor = dataBase.rawQuery(String.format(GET_TODO_LIST_QUERY, appID), null)) {
            while (cursor.moveToNext()) {
                String todoID = cursor.getString(
                        cursor.getColumnIndexOrThrow(TodoDBContract.TodoList.COLUMN_TODO_ID));
                String todoText = cursor.getString(cursor.getColumnIndexOrThrow(TodoDBContract.TodoList.COLUMN_TODO_TEXT));
                todoList.add(new TodoNotes(todoText, todoID));
            }
            return todoList;
        }
    }

    @Override
    public String getAppID() {
        SQLiteDatabase dataBase = todoListDBHelper.getReadableDatabase();
        try (Cursor cursor = dataBase.rawQuery(FIND_APP_ID, null)) {
            String appID = null;
            int index = cursor.getColumnIndex(TodoDBContract.TodoListID.COLUMN_TODOLIST_ID);
            if (cursor.moveToFirst()) {
                appID = cursor.getString(index);
            }
            return appID;
        }
    }

    @Override
    public void setAppIdFromServer(String appID) {
        SQLiteDatabase dataBase = todoListDBHelper.getWritableDatabase();
        dataBase.execSQL(String.format(ADD_APP_ID, appID));
    }

    @Override
    public void updateTodoList(List<TodoNotes> todoNotesList) {
        SQLiteDatabase database = todoNotesDBHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            for (TodoNotes todoNotes :
                    todoNotesList) {
                database.execSQL(String.format(UPDATE_LIST_QUERY, todoNotes.getTodoId(), todoNotes.getNoteText(), TODOLIST_NUMBER));
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}