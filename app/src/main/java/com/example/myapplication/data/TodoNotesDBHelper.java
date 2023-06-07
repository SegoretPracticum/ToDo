package com.example.myapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TodoNotesDBHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_TODO_NOTES_DB =
            "CREATE TABLE " + TodoDBContract.TodoList.TABLE_NAME + " (" +
                    TodoDBContract.TodoList.COLUMN_TODO_ID + " TEXT PRIMARY KEY, " +
                    TodoDBContract.TodoList.COLUMN_TODO_TEXT + " TEXT, " + TodoDBContract.TodoList.COLUMN_APP_ID + " TEXT)";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TodoNotes.db";
    public TodoNotesDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TODO_NOTES_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        do nothing
    }
}