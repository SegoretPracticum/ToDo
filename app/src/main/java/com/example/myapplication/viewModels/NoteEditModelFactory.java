package com.example.myapplication.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.model.TodoNotes;
import com.example.myapplication.repository.TodoRepository;

public class NoteEditModelFactory implements ViewModelProvider.Factory {
    private final TodoNotes todoNote;
    private final TodoRepository todoRepository;

    public NoteEditModelFactory(TodoNotes todoNote, TodoRepository todoRepository) {
        this.todoNote = todoNote;
        this.todoRepository = todoRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == NoteEditViewModel.class) {
            return (T) new NoteEditViewModel(todoNote, todoRepository);
        }
        throw new IllegalArgumentException();
    }
}