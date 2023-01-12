package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TodoNotesActivity extends AppCompatActivity {

    private static final int SPAN_COUNT = 2;
    public static final String TODO_NOTE = "TODO_NOTE";
    public NotesAdapter notesAdapter;
    public ArrayList<TodoNotes> todoNotesList = new ArrayList<>();
    public TodoNotesViewModel viewModel;

    ActivityResultLauncher<Intent> noteResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getData() != null) {
            TodoNotes newTodo = result.getData().getParcelableExtra(TODO_NOTE);
            viewModel.onClickResultProcessing(newTodo);

        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView notesView = findViewById(R.id.todo_notes_activity_res_view);
        FloatingActionButton addNote = findViewById(R.id.todo_notes_activity_btn_add_note);
        viewModel = new ViewModelProvider(this).get(TodoNotesViewModel.class);

        NotesAdapter.OnTodoClickListener onTodoListener = (position) -> {
            Intent todoEditedNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            todoEditedNote.putExtra(TODO_NOTE, todoNotesList.get(position));
            noteResult.launch(todoEditedNote);
        };

        notesAdapter = new NotesAdapter(todoNotesList, onTodoListener);
        notesView.setLayoutManager(new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        notesView.setAdapter(notesAdapter);
        Observer<ArrayList<TodoNotes>> todoListObserver = todoList -> {
            notesAdapter.refreshList(todoList);
            todoNotesList = todoList;
        };
        viewModel.getTodoList().observe(this, todoListObserver);
        addNote.setOnClickListener(view -> {
            Intent addNewNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            noteResult.launch(addNewNote);
        });
    }
}