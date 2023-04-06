package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TodoNotesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final ConnectChecker connectChecker;

    public TodoNotesViewModelFactory(ConnectChecker connectChecker) {
        this.connectChecker = connectChecker;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == TodoNotesViewModel.class) {
           return (T) new TodoNotesViewModel(connectChecker);
        }
        return null;
    }
}