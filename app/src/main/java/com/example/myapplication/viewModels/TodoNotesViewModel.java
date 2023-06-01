package com.example.myapplication.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.appmessages.ErrorMessage;
import com.example.myapplication.Item.TodoNotes;
import com.example.myapplication.interfaces.TodoCallback;
import com.example.myapplication.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;

public class TodoNotesViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<TodoNotes>> todoList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addTodoEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> refreshTodoListEvent = new MutableLiveData<>();
    private final MutableLiveData<TodoNotes> editTodoEvent = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final TodoRepository todoRepository;

    public LiveData<String> getError() {
        return error;
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
        public void onFail(ErrorMessage errorMessage) {
            onFailResult(errorMessage);
        }
    };

    public TodoNotesViewModel(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
        refreshTodoListEvent.setValue(true);
        todoRepository.getTodoList(todoCallbackList);
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
        refreshTodoListEvent.postValue(true);
        todoRepository.getTodoList(todoCallbackList);
    }

    public void buttonClicked() {
        addTodoEvent.setValue(true);
    }

    public void todoItemClicked(TodoNotes todoNotes) {
        editTodoEvent.setValue(todoNotes);
    }

    public void clickReset() {
        addTodoEvent.setValue(false);
        editTodoEvent.setValue(null);
    }

    public void resetConnectionErrors() {
        error.setValue("");
    }

    private void onFailResult(ErrorMessage errorMessage) {
        error.postValue(errorMessage.getTextMessage());
        refreshTodoListEvent.postValue(false);
    }
}