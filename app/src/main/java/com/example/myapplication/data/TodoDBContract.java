package com.example.myapplication.data;

import android.provider.BaseColumns;

public final class TodoDBContract {

    private TodoDBContract() {
    }

    public static class TodoListID implements BaseColumns {
        public static final String TABLE_NAME = "APP_ID";
        public static final String COLUMN_TODOLIST_ID = "TodoList_ID";
    }

    public static class TodoList implements BaseColumns {
        public static final String TABLE_NAME = "TodoNotes";
        public static final String COLUMN_TODO_ID = "Todo_ID";
        public static final String COLUMN_TODO_TEXT = "Todo_Text";
        public static final String COLUMN_APP_ID = "App_ID";
    }
}