package com.example.myapplication.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.appmessages.ErrorMessage;
import com.example.myapplication.Item.TodoNotes;
import com.example.myapplication.interfaces.TodoCallback;
import com.example.myapplication.repository.TodoRepository;

public class NoteEditViewModel extends ViewModel {

    private final MutableLiveData<Boolean> toolbarNavigationEvent = new MutableLiveData<>();
    private final MutableLiveData<TodoNotes> sendTodo = new MutableLiveData<>();
    private final MutableLiveData<String> todoTextChange = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> sendTodoProcessing = new MutableLiveData<>();
    private final TodoRepository todoRepository;
    private TodoNotes todoNote;

    private final TodoCallback<TodoNotes> todoCallback = new TodoCallback<TodoNotes>() {
        @Override
        public void onSuccess(TodoNotes todoNotes) {
            sendTodo.postValue(todoNotes);
            sendTodoProcessing.postValue(false);
        }

        @Override
        public void onFail(ErrorMessage errorMessage) {
            onFailResult(errorMessage);
        }
    };

    public NoteEditViewModel(TodoNotes todoNote, TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
        this.todoNote = todoNote;
        if (todoNote != null) {
            String textNote = todoNote.getNoteText();
            todoTextChange.setValue(textNote);
        }
    }

    public LiveData<String> getError() {
        return error;
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

    public void navigationClicked() {
        toolbarNavigationEvent.setValue(true);
    }

    public void onTextNoteChanged(String textNote) {
        todoTextChange.setValue(textNote);
    }

    public void onBtnToolbarClicked(String todoText) {
        if (todoNote == null) {
            todoNote = new TodoNotes(todoText, null);
            checkAndSendTodo(todoText);
        } else {
            checkAndSendTodo(todoText);
        }
    }

    public void resetConnectionErrors() {
        error.setValue("");
    }

    private void checkAndSendTodo(String todoText) {
        todoNote.setNoteText(todoText);
        todoRepository.checkAndSendTodoNote(todoNote, todoCallback);
        sendTodoProcessing.setValue(true);
    }

    private void onFailResult(ErrorMessage errorMessage) {
        error.postValue(errorMessage.getTextMessage());
        sendTodoProcessing.postValue(false);
    }
}