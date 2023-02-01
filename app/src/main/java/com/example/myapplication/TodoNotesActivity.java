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
    private NotesAdapter notesAdapter;
    private RecyclerView notesView;
    private TodoNotesViewModel viewModel;

    private final ActivityResultLauncher<Intent> noteResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getData() != null) {
            TodoNotes newTodo = result.getData().getParcelableExtra(TODO_NOTE);
            viewModel.onEventReceived(newTodo);
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
        addNote.setOnClickListener(view -> viewModel.buttonClicked());
    }

    private void onButtonClicked(Boolean btnClick) {
        if (btnClick) {
            Intent addNewNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            noteResult.launch(addNewNote);
            viewModel.clickReset();
        }
    }

    private void onItemClicked(TodoNotes todoNotes) {
        if (todoNotes != null) {
            Intent todoEditedNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            todoEditedNote.putExtra(TODO_NOTE, todoNotes);
            noteResult.launch(todoEditedNote);
            viewModel.clickReset();
        }
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(TodoNotesViewModel.class);
        viewModel.getAddTodoEvent().observe(this, this::onButtonClicked);
        viewModel.getEditTodoEvent().observe(this, this::onItemClicked);
        viewModel.getTodoList().observe(this, todoList -> notesAdapter.refreshList(todoList));
    }

    private void joinNotesAdapter() {
        NotesAdapter.OnTodoClickListener onTodoListener = (todoNotes) -> viewModel.todoItemClicked(todoNotes);
        notesAdapter = new NotesAdapter(onTodoListener);
        notesView.setLayoutManager(new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        notesView.setAdapter(notesAdapter);
    }
}