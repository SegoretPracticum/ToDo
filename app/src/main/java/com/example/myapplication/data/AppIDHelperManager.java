package com.example.myapplication.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.myapplication.interfaces.AppIdentification;

public class AppIDHelperManager implements AppIdentification {
    TodoListDBHelper todoListDBHelper;

    public AppIDHelperManager(TodoListDBHelper todoListDBHelper) {
        this.todoListDBHelper = todoListDBHelper;
    }

    @Override
    public String getAppID() {
        SQLiteDatabase dataBase = todoListDBHelper.getReadableDatabase();
        String[] projection = {
                TodoDBContract.TodoListID.COLUMN_TODOLIST_ID
        };
        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = dataBase.query(
                TodoDBContract.TodoListID.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
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
        ContentValues values = new ContentValues();
        values.put(TodoDBContract.TodoListID.COLUMN_TODOLIST_ID,appID);
        dataBase.insert(TodoDBContract.TodoListID.TABLE_NAME, null, values);
    }
}