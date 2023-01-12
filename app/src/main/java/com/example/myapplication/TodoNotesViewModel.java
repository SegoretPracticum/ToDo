package com.example.myapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class TodoNotesViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<TodoNotes>> todoList = new MutableLiveData<>();
    private final ArrayList<TodoNotes> todoNotesList = new ArrayList<>();
    private static final int NO_POSITION = -1;
    private NotesAdapter notesAdapter;

    public MutableLiveData<ArrayList<TodoNotes>> getTodoList() {
        return todoList;
    }

    public void onClickResultProcessing(TodoNotes todoNotes) {

        int index = todoNotesList.indexOf(todoNotes);
        if (index == NO_POSITION) {
            todoNotesList.add(todoNotes);
            notesAdapter.notifyItemInserted(todoNotesList.size());

        } else {
            todoNotesList.set(index, todoNotes);
            notesAdapter.notifyItemChanged(index);
        }
        todoList.setValue(todoNotesList);
    }

    public void setNotesAdapter(NotesAdapter notesAdapter) {
        this.notesAdapter = notesAdapter;
    }
}
