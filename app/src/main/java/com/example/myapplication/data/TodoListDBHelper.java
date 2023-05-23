package com.example.myapplication.data;

import static android.provider.BaseColumns._ID;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TodoListDBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_TODOLIST_DB =
            "CREATE TABLE " + TodoDBContract.TodoListID.TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TodoDBContract.TodoListID.COLUMN_TODOLIST_ID + " TEXT)";

    private static final String SQL_DELETE_TODOLIST_DB =
            "DROP TABLE IF EXISTS " + TodoDBContract.TodoList.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TodoList.db";

     public TodoListDBHelper(@Nullable Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TODOLIST_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TODOLIST_DB);
        onCreate(db);
    }
}