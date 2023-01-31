package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TodoNotesActivity extends AppCompatActivity {

    private static final int SPAN_COUNT = 2;
    public static final String TODO_NOTE = "TODO_NOTE";
    public NotesAdapter notesAdapter;
    RecyclerView notesView;
    public TodoNotesViewModel viewModel;
    ActivityResultLauncher<Intent> noteResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getData() != null) {
            TodoNotes newTodo = result.getData().getParcelableExtra(TODO_NOTE);
            viewModel.noteEditActivityResultProcessing(newTodo);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesView = findViewById(R.id.todo_notes_activity_res_view);
        FloatingActionButton addNote = findViewById(R.id.todo_notes_activity_btn_add_note);
        initViewModel();
        joinNotesAdapter();
        addNote.setOnClickListener(view -> {
            viewModel.buttonClick();
            viewModel.clickReset();
        });
    }

    private void btnClickAction(Boolean btnClick) {
        if (btnClick) {
            Intent addNewNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            noteResult.launch(addNewNote);
        }
    }

    private void itemClickAction(TodoNotes todoNotes) {
        if (todoNotes != null) {
            Intent todoEditedNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            todoEditedNote.putExtra(TODO_NOTE, todoNotes);
            noteResult.launch(todoEditedNote);
        }
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(TodoNotesViewModel.class);
        viewModel.getAddTodoEvent().observe(this, this::btnClickAction);
        viewModel.getEditTodoEvent().observe(this, this::itemClickAction);
        viewModel.getTodoList().observe(this, todoList -> notesAdapter.refreshList(todoList));
    }

    private void joinNotesAdapter() {
        NotesAdapter.OnTodoClickListener onTodoListener = (todoNotes) -> {
            viewModel.todoItemClick(todoNotes);
            viewModel.clickReset();
        };
        notesAdapter = new NotesAdapter(onTodoListener);
        notesView.setLayoutManager(new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        notesView.setAdapter(notesAdapter);
    }
}