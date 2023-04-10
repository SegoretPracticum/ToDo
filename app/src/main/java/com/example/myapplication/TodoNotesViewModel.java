package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TodoNotesViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<TodoNotes>> todoList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addTodoEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> refreshTodoListEvent = new MutableLiveData<>();
    private final MutableLiveData<TodoNotes> editTodoEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> internetConnectionError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> errorWorkingWithServer = new MutableLiveData<>();
    private final HttpConnect httpConnect = new HttpConnect();
    private final ConnectCheck connectChecker;
    private static final String REQUEST_GET = "GET";

    private final TodoCallback<List<TodoNotes>> todoCallback = new TodoCallback<List<TodoNotes>>() {
        @Override
        public void onSuccess(List<TodoNotes> todoNoteList) {
            todoList.postValue((ArrayList<TodoNotes>) todoNoteList);
            refreshTodoListEvent.postValue(false);
        }

        @Override
        public void onFail() {
            errorWorkingWithServer.postValue(true);
            refreshTodoListEvent.postValue(false);
            errorWorkingWithServer.postValue(false);
            internetConnectionError.postValue(false);
            refreshTodoListEvent.postValue(false);
        }
    };

    public TodoNotesViewModel(ConnectCheck connectChecker) {
        this.connectChecker = connectChecker;
        if (connectChecker.isOffline()) {
            internetConnectionError.setValue(true);
        } else {
            getNotesList();
        }
    }

    public MutableLiveData<Boolean> getInternetConnectionError() {
        return internetConnectionError;
    }

    public LiveData<Boolean> getErrorWorkingWithServer() {
        return errorWorkingWithServer;
    }

    List<TodoNotes> todoNotesList = new ArrayList<>();

    void getNotesList() {
        if (connectChecker.isOffline()) {
            internetConnectionError.setValue(true);
            internetConnectionError.setValue(false);
            refreshTodoListEvent.postValue(false);
        } else {
            Thread thread = new Thread(() -> {
                try {
                    httpConnect.getTodoNotesListFromServer(todoCallback, REQUEST_GET);
                } catch (IOException e) {
                    todoCallback.onFail();
                }
            });
            thread.start();
            refreshTodoListEvent.postValue(true);
        }
    }

    public LiveData<Boolean> getRefreshTodoList() {
        return refreshTodoListEvent;
    }

    public LiveData<? extends List<TodoNotes>> getTodoList() {
        return todoList;
    }

    public void onResultReceived(TodoNotes todoNotes) {
        todoNotesList = todoList.getValue();
        if (todoNotesList == null) {
            todoNotesList = new ArrayList<>();
        }
        if (!todoNotesList.contains(todoNotes)) {
            todoNotesList.add(todoNotes);
        } else {
            int index = todoNotesList.indexOf(todoNotes);
            todoNotesList.set(index, todoNotes);
        }
        todoList.setValue((ArrayList<TodoNotes>) todoNotesList);
    }

    public LiveData<Boolean> getAddTodoEvent() {
        return addTodoEvent;
    }

    public void buttonClicked() {
        addTodoEvent.setValue(true);
    }

    public void todoItemClicked(TodoNotes todoNotes) {
        editTodoEvent.setValue(todoNotes);
    }

    public LiveData<TodoNotes> getEditTodoEvent() {
        return editTodoEvent;
    }

    public void clickReset() {
        addTodoEvent.setValue(false);
        editTodoEvent.setValue(null);
    }
}