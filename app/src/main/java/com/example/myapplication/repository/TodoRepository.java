package com.example.myapplication.repository;

import com.example.myapplication.appmessages.ErrorMessage;
import com.example.myapplication.Item.TodoNotes;
import com.example.myapplication.interfaces.AppIdentification;
import com.example.myapplication.interfaces.ConnectCheck;
import com.example.myapplication.interfaces.TodoMotesDAO;
import com.example.myapplication.interfaces.TodoCallback;
import com.example.myapplication.interfaces.TodoNotesAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TodoRepository {

    private final ConnectCheck connectCheck;
    private List<TodoNotes> todoNotesList = new ArrayList<>();
    private final AppIdentification appIdentification;
    private final TodoNotesAPI todoNotesAPI;
    private final TodoMotesDAO todoMotesDAO;
    private TodoCallback<TodoNotes> viewModelCallback;
    private final TodoCallback<TodoNotes> todoCallback = new TodoCallback<TodoNotes>() {
        @Override
        public void onSuccess(TodoNotes result) {
        todoMotesDAO.updateDB(result,"1");
        viewModelCallback.onSuccess(result);
        }

        @Override
        public void onFail(ErrorMessage errorMessage) {

        }
    };

    private final TodoCallback<List<TodoNotes>> todoCallbackList = new TodoCallback<List<TodoNotes>>() {
        @Override
        public void onSuccess(List<TodoNotes> result) {
        todoNotesList = result;
        }

        @Override
        public void onFail(ErrorMessage errorMessage) {
            todoNotesList = todoMotesDAO.getTodoNotesList("1");
        }
    };

    public TodoRepository(TodoMotesDAO todoMotesDAO, AppIdentification appIdentification,
                          ConnectCheck connectCheck, TodoNotesAPI todoNotesAPI) {
        this.todoMotesDAO = todoMotesDAO;
        this.appIdentification = appIdentification;
        this.connectCheck = connectCheck;
        this.todoNotesAPI = todoNotesAPI;
    }

    public void sendTodoNote(TodoNotes todoNotes, TodoCallback<String> supportiveCallback, TodoCallback<TodoNotes> viewModelCallback) {
        this.viewModelCallback = viewModelCallback;
        if (connectCheck.isOffline()){
            supportiveCallback.onFail(ErrorMessage.CONNECTION_ERROR);
        }
        else if (todoNotes.getNoteText().length() == 0){
            supportiveCallback.onFail(ErrorMessage.EMPTY_TEXT);
        }
        else {
            Thread thread = new Thread(() -> {
                try {
                    if (appIdentification == null) {
                        getAppID(supportiveCallback);
                    }
                    todoNotesAPI.sendTodo(todoNotes, todoCallback, appIdentification);

                } catch (IOException e) {
                    viewModelCallback.onFail(ErrorMessage.SERVER_ERROR);
                }
            });
            thread.start();
        }
    }

    public void getTodoList(TodoCallback <List<TodoNotes>> repositoryCallback,TodoCallback <String> supportiveCallback){
        Thread thread = new Thread(() -> {
            try {
                if (connectCheck.isOffline())
                {
                    supportiveCallback.onFail(ErrorMessage.CONNECTION_ERROR);
                    todoNotesList = todoMotesDAO.getTodoNotesList("1");
                    repositoryCallback.onSuccess(todoNotesList);
                }
                else {
                    if (appIdentification == null){
                        getAppID(supportiveCallback);
                    }
                    else {
                        todoNotesAPI.getTodoNotesListFromServer(repositoryCallback, appIdentification);
                    }
                }
            } catch (IOException e) {
                todoCallbackList.onFail(ErrorMessage.SERVER_ERROR);
            }
        });
        thread.start();
    }

    public void getAppID(TodoCallback <String> todoCallbackAppID) {
        Thread thread = new Thread(() -> {
            try {
                todoNotesAPI.initApp(todoCallbackAppID, appIdentification);
            } catch (IOException e) {
                todoCallbackAppID.onFail(ErrorMessage.SERVER_ERROR);
            }
        });
        thread.start();
    }
}