package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.UUID;

public class NoteEditViewModel extends ViewModel {

    private final MutableLiveData<Boolean> backToMainActivityEvent = new MutableLiveData<>();
    private final MutableLiveData<TodoNotes> sendTodo = new MutableLiveData<>();
    private final MutableLiveData<String> todoTextChange = new MutableLiveData<>();
    private final MutableLiveData<Boolean> emptyTodoInput = new MutableLiveData<>();
    private TodoNotes todoNote;


    public LiveData<Boolean> getBackToMainActivityEvent() {
        return backToMainActivityEvent;
    }

    public void navigationClicked() {
        backToMainActivityEvent.setValue(true);
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
        if (textNote.length() == 0) {
            emptyInput();
            todoTextChange.setValue(textNote);
        }
    }

    public void setNewTodo(TodoNotes todoNote) {
        this.todoNote = todoNote;
        if (todoNote != null) {
            String textNote = todoNote.getNoteText();
            todoTextChange.setValue(textNote);
        }
    }

    public void onBtnToolbarClicked(String todoText) {
        if (todoNote == null) {
            todoNote = new TodoNotes("", UUID.randomUUID().toString());
        } else {
            todoNote.setNoteText(todoText);
        }
        if (todoText.length() == 0) {
            emptyTodoInput.setValue(true);
        } else {
            sendTodo.setValue(todoNote);
        }
    }

    public LiveData<Boolean> getEmptyTodoInput() {
        return emptyTodoInput;
    }

    public void emptyInput() {
        emptyTodoInput.setValue(true);
    }

}
