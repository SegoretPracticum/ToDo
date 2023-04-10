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
    private static final String REQUEST_PUT = "PUT";
    private static final String REQUEST_POST = "POST";

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
    private final HttpConnect httpConnect = new HttpConnect();

    public NoteEditViewModel(TodoNotes todoNote, ConnectCheck connectChecker) {
        this.connectChecker = connectChecker;
        this.todoNote = todoNote;
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

    public void navigationClicked() {
        toolbarNavigationEvent.setValue(true);
    }

    public void clickReset() {
        emptyTodoInput.setValue(false);
    }

    public LiveData<TodoNotes> getSendTodo() {
        return sendTodo;
    }

    public LiveData<String> getTodoText() {
        return todoTextChange;
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
            } else {
                sendTodoToServer(todoNote, todoCallback);
                sendTodoProcessing.setValue(true);
            }
        }
    }

    public void sendTodoToServer(TodoNotes todoNotes, TodoCallback<TodoNotes> todoCallback) {
        Thread thread = new Thread(() -> {
            try {
                if (todoNotes.getTodoId() == null)
                {
                httpConnect.sendTodo(todoNotes, todoCallback, REQUEST_POST);
                }
                else {
                    httpConnect.sendTodo(todoNotes, todoCallback, REQUEST_PUT);
                }
            } catch (IOException e) {
                todoCallback.onFail();
            }
        });
        thread.start();
    }

    public LiveData<Boolean> getEmptyTodoInput() {
        return emptyTodoInput;
    }
}