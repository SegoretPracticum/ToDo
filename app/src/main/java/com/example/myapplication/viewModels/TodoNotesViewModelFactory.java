package com.example.myapplication.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.repository.TodoRepository;

public class TodoNotesViewModelFactory implements ViewModelProvider.Factory {
    private final TodoRepository todoRepository;


    public TodoNotesViewModelFactory(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == TodoNotesViewModel.class) {
            return (T) new TodoNotesViewModel(todoRepository);
        }
        throw new IllegalArgumentException();
    }
}