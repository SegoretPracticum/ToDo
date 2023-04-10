package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NoteEditModelFactory implements ViewModelProvider.Factory {
    private final TodoNotes todoNote;
    private final ConnectCheck connectCheck;

    public NoteEditModelFactory(TodoNotes todoNote, ConnectCheck connectCheck) {
        super();
        this.todoNote = todoNote;
        this.connectCheck = connectCheck;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NoteEditViewModel.class) {
            return (T) new NoteEditViewModel(todoNote, connectCheck);
        }
        throw new IllegalArgumentException();
    }
}