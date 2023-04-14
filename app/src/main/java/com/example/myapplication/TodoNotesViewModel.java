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
    private final AppIdentification appIdentification;

    public LiveData<Boolean> getInternetConnectionError() {
        return internetConnectionError;
    }

    public LiveData<Boolean> getErrorWorkingWithServer() {
        return errorWorkingWithServer;
    }

    public LiveData<Boolean> getRefreshTodoList() {
        return refreshTodoListEvent;
    }

    public LiveData<? extends List<TodoNotes>> getTodoList() {
        return todoList;
    }

    public LiveData<Boolean> getAddTodoEvent() {
        return addTodoEvent;
    }

    public LiveData<TodoNotes> getEditTodoEvent() {
        return editTodoEvent;
    }

    private final TodoCallback<List<TodoNotes>> todoCallbackList = new TodoCallback<List<TodoNotes>>() {
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

    private final TodoCallback<String> todoCallbackAppID = new TodoCallback<String>() {
        @Override
        public void onSuccess(String result) {
            getNotesList();
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

    public TodoNotesViewModel(ConnectCheck connectChecker, AppIdentification appIdentification) {
        this.connectChecker = connectChecker;
        this.appIdentification = appIdentification;
        if (connectChecker.isOffline()) {
            internetConnectionError.setValue(true);
        } else if (appIdentification.getAppID() == null) {
            getAppID();
            refreshTodoListEvent.postValue(true);
        } else getNotesList();
    }

    public void onResultReceived(TodoNotes todoNotes) {
        ArrayList<TodoNotes> todoNotesList = todoList.getValue();
        if (todoNotesList == null) {
            todoNotesList = new ArrayList<>();
        }
        if (!todoNotesList.contains(todoNotes)) {
            todoNotesList.add(todoNotes);
        } else {
            int index = todoNotesList.indexOf(todoNotes);
            todoNotesList.set(index, todoNotes);
        }
        todoList.setValue(todoNotesList);
    }

    public void getNotesList() {
        if (connectChecker.isOffline()) {
            internetConnectionError.setValue(true);
            internetConnectionError.setValue(false);
            refreshTodoListEvent.postValue(false);
        } else {
            Thread thread = new Thread(() -> {
                try {
                    httpConnect.getTodoNotesListFromServer(todoCallbackList, appIdentification);
                } catch (IOException e) {
                    todoCallbackList.onFail();
                }
            });
            thread.start();
            refreshTodoListEvent.postValue(true);
        }
    }

    private void getAppID() {
        Thread thread = new Thread(() -> {
            try {
                httpConnect.initApp(todoCallbackAppID, appIdentification);
            } catch (IOException e) {
                todoCallbackAppID.onFail();
            }
        });
        thread.start();
        refreshTodoListEvent.postValue(true);
    }

    public void buttonClicked() {
        if (connectChecker.isOffline()) {
            internetConnectionError.setValue(true);
            internetConnectionError.setValue(false);
        } else {
            addTodoEvent.setValue(true);
        }
    }

    public void todoItemClicked(TodoNotes todoNotes) {
        editTodoEvent.setValue(todoNotes);
    }

    public void clickReset() {
        addTodoEvent.setValue(false);
        editTodoEvent.setValue(null);
    }
}