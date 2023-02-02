package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TodoNotesViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<TodoNotes>> todoList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addTodoEvent = new MutableLiveData<>();
    private final MutableLiveData<TodoNotes> editTodoEvent = new MutableLiveData<>();

    public LiveData<? extends List<TodoNotes>> getTodoList() {
        return todoList;
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