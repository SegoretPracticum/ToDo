package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class NoteEditModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final TodoNotes todoNote;

    public NoteEditModelFactory(TodoNotes todoNote) {
        super();
        this.todoNote = todoNote;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NoteEditViewModel.class) {
            return (T) new NoteEditViewModel(todoNote);
        }
        return null;
    }
}