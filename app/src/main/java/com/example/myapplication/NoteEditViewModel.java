package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

public class NoteEditViewModel extends ViewModel {

    private final MutableLiveData<Boolean> toolbarNavigationEvent = new MutableLiveData<>();
    private final MutableLiveData<TodoNotes> sendTodo = new MutableLiveData<>();
    private final MutableLiveData<String> todoTextChange = new MutableLiveData<>();
    private final MutableLiveData<Boolean> emptyTodoInput = new MutableLiveData<>();
    private final MutableLiveData<Boolean> internetConnectionError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sendTodoProcessing = new MutableLiveData<>();
    private final MutableLiveData<Boolean> errorWorkingWithServer = new MutableLiveData<>();
    private final ConnectCheck connectChecker;
    private TodoNotes todoNote;
    private final HttpConnect httpConnect = new HttpConnect();
    private final AppIdentification appIdentification;

    private final TodoCallback<TodoNotes> todoCallback = new TodoCallback<TodoNotes>() {
        @Override
        public void onSuccess(TodoNotes todoNotes) {
            sendTodo.postValue(todoNotes);
            sendTodoProcessing.postValue(false);
        }

        @Override
        public void onFail() {
            errorWorkingWithServer.postValue(true);
            sendTodoProcessing.postValue(false);
            errorWorkingWithServer.postValue(false);
        }
    };

    public NoteEditViewModel(TodoNotes todoNote, ConnectCheck connectChecker, AppIdentification appIdentification) {
        this.connectChecker = connectChecker;
        this.todoNote = todoNote;
        this.appIdentification = appIdentification;
        if (todoNote != null) {
            String textNote = todoNote.getNoteText();
            todoTextChange.setValue(textNote);
        }
    }

    public LiveData<Boolean> getErrorWorkingWithServer() {
        return errorWorkingWithServer;
    }

    public LiveData<Boolean> getInternetConnectionError() {
        return internetConnectionError;
    }

    public LiveData<Boolean> getSendTodoProcessing() {
        return sendTodoProcessing;
    }

    public LiveData<Boolean> getToolbarNavigationEvent() {
        return toolbarNavigationEvent;
    }

    public LiveData<TodoNotes> getSendTodo() {
        return sendTodo;
    }

    public LiveData<String> getTodoText() {
        return todoTextChange;
    }

    public LiveData<Boolean> getEmptyTodoInput() {
        return emptyTodoInput;
    }

    public void navigationClicked() {
        toolbarNavigationEvent.setValue(true);
    }

    public void onTextNoteChanged(String textNote) {
        todoTextChange.setValue(textNote);
    }

    public void onBtnToolbarClicked(String todoText) {
        if (connectChecker.isOffline()) {
            internetConnectionError.setValue(true);
            internetConnectionError.setValue(false);
        } else {
            if (todoNote == null) {
                todoNote = new TodoNotes(todoText, null);
            }
            todoNote.setNoteText(todoText);
            if (todoText.length() == 0) {
                emptyTodoInput.setValue(true);
                emptyTodoInput.setValue(false);
            } else {
                sendTodoToServer(todoNote, todoCallback);
                sendTodoProcessing.setValue(true);
            }
        }
    }

    public void sendTodoToServer(TodoNotes todoNotes, TodoCallback<TodoNotes> todoCallback) {
        Thread thread = new Thread(() -> {
            try {
                httpConnect.sendTodo(todoNotes, todoCallback, appIdentification);
            } catch (IOException e) {
                todoCallback.onFail();
            }
        });
        thread.start();
    }
}