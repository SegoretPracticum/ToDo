package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NoteEditModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final TodoNotes todoNote;
    private final ConnectChecker connectChecker;

    public NoteEditModelFactory(TodoNotes todoNote, ConnectChecker connectChecker) {
        super();
        this.todoNote = todoNote;
        this.connectChecker = connectChecker;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NoteEditViewModel.class) {
            return (T) new NoteEditViewModel(todoNote, connectChecker);
        }
        return null;
    }
}