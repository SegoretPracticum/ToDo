package com.example.myapplication.repository;

import com.example.myapplication.appmessages.ErrorMessage;
import com.example.myapplication.Item.TodoNotes;
import com.example.myapplication.interfaces.ConnectCheck;
import com.example.myapplication.interfaces.TodoNotesDAO;
import com.example.myapplication.interfaces.TodoCallback;
import com.example.myapplication.interfaces.TodoNotesAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TodoRepository {

    private final ConnectCheck connectCheck;
    private final TodoNotesAPI todoNotesAPI;
    private final TodoNotesDAO todoNotesDAO;
    private static final String TODOLIST_NUMBER = "1";
    private String repositoryOperation;
    private final String GET_LIST = "get list";
    private final String SEND_TODO = "send Todo";
    private TodoNotes todoNotes;
    private TodoCallback<TodoNotes> viewModelCallback;
    private TodoCallback<List<TodoNotes>> viewModelListCallback;
    private final TodoCallback<TodoNotes> repositoryTodoCallback = new TodoCallback<TodoNotes>() {
        @Override
        public void onSuccess(TodoNotes result) {
        todoNotesDAO.updateDB(result,TODOLIST_NUMBER);
        viewModelCallback.onSuccess(result);
        }

        @Override
        public void onFail(ErrorMessage errorMessage) {
        viewModelCallback.onFail(errorMessage);
        }
    };

    private final TodoCallback<List<TodoNotes>> repositoryCallback = new TodoCallback<List<TodoNotes>>() {
        @Override
        public void onSuccess(List<TodoNotes> result) {
                viewModelListCallback.onSuccess(result);
        }

        @Override
        public void onFail(ErrorMessage errorMessage) {
            viewModelListCallback.onFail(errorMessage);
        }
    };

    private final TodoCallback <String> appIDCallback = new TodoCallback<String>() {
        @Override
        public void onSuccess(String appID) {
            todoNotesDAO.setAppIdFromServer(appID);
            if (repositoryOperation.equals(GET_LIST)){
                viewModelListCallback.onSuccess(new ArrayList<>());
            }
            else {
                sendTodo();
            }
            repositoryOperation = null;
        }

        @Override
        public void onFail(ErrorMessage errorMessage) {
            if (repositoryOperation.equals(GET_LIST)) {
                viewModelListCallback.onFail(errorMessage);
            }
            else {
            viewModelCallback.onFail(errorMessage);
            }
            repositoryOperation = null;
        }
    };

    public TodoRepository(TodoNotesDAO todoNotesDAO, ConnectCheck connectCheck, TodoNotesAPI todoNotesAPI) {
        this.todoNotesDAO = todoNotesDAO;
        this.connectCheck = connectCheck;
        this.todoNotesAPI = todoNotesAPI;
    }

    public void checkAndSendTodoNote(TodoNotes todoNotes, TodoCallback<TodoNotes> viewModelCallback) {
        this.todoNotes = todoNotes;
        this.viewModelCallback = viewModelCallback;
        if (connectCheck.isOffline()){
            viewModelCallback.onFail(ErrorMessage.CONNECTION_ERROR);
        }
        else if (todoNotes.getNoteText().length() == 0){
            viewModelCallback.onFail(ErrorMessage.EMPTY_TEXT);
        }
        else {
            Thread thread = new Thread(() -> {
                try {
                    if (todoNotesDAO.getAppID() == null) {
                        repositoryOperation = SEND_TODO;
                        getAppID(appIDCallback);
                    }
                    else {
                        todoNotesAPI.sendTodo(todoNotes, repositoryTodoCallback, todoNotesDAO);
                    }
                } catch (IOException e) {
                    repositoryCallback.onFail(ErrorMessage.SERVER_ERROR);
                }
            });
            thread.start();
        }
    }

    public void getTodoList(TodoCallback <List<TodoNotes>> viewModelListCallback){
        this.viewModelListCallback = viewModelListCallback;
        Thread thread = new Thread(() -> {
            try {
                if (connectCheck.isOffline())
                {
                    viewModelListCallback.onFail(ErrorMessage.CONNECTION_ERROR);
                    repositoryCallback.onSuccess(todoNotesDAO.getTodoNotesList(TODOLIST_NUMBER));
                }
                else {
                    if (todoNotesDAO.getAppID() == null){
                        repositoryOperation = GET_LIST;
                        getAppID(appIDCallback);
                    }
                    else {
                        todoNotesAPI.getTodoNotesListFromServer(repositoryCallback, todoNotesDAO);
                    }
                }
            } catch (IOException e) {
                repositoryCallback.onFail(ErrorMessage.SERVER_ERROR);
            }
        });
        thread.start();
    }

    public void getAppID(TodoCallback <String> todoCallbackAppID) {
        Thread thread = new Thread(() -> {
            try {
                todoNotesAPI.initApp(todoCallbackAppID, todoNotesDAO);
            } catch (IOException e) {
                todoCallbackAppID.onFail(ErrorMessage.SERVER_ERROR);
            }
        });
        thread.start();
    }

    private void sendTodo(){
        Thread thread = new Thread(() -> {
            try {
                todoNotesAPI.sendTodo(todoNotes, repositoryTodoCallback, todoNotesDAO);
            } catch (IOException e) {
                repositoryCallback.onFail(ErrorMessage.SERVER_ERROR);
            }
        });
        thread.start();
    }
}