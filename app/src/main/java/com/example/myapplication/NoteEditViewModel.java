package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.UUID;

public class NoteEditViewModel extends ViewModel {

    private final MutableLiveData<Boolean> toolbarNavigationEvent = new MutableLiveData<>();
    private final MutableLiveData<TodoNotes> sendTodo = new MutableLiveData<>();
    private final MutableLiveData<String> todoTextChange = new MutableLiveData<>();
    private final MutableLiveData<Boolean> emptyTodoInput = new MutableLiveData<>();
    private final TodoNotes todoNote;

    public NoteEditViewModel(TodoNotes todoNote) {
        if (todoNote != null) {
            this.todoNote = todoNote;
            String textNote = todoNote.getNoteText();
            todoTextChange.setValue(textNote);
        } else {
            this.todoNote = new TodoNotes("", UUID.randomUUID().toString());
        }
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
        todoNote.setNoteText(todoText);
        if (todoText.length() == 0) {
            emptyTodoInput.setValue(true);
        } else {
            sendTodo.setValue(todoNote);
        }
    }

    public LiveData<Boolean> getEmptyTodoInput() {
        return emptyTodoInput;
    }
}