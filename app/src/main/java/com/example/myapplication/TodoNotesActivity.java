package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TodoNotesActivity extends AppCompatActivity {
    private static final int NO_POSITION = -1;
    public static final String TODO_NOTE = "TODO_NOTE";
    public NotesAdapter notesAdapter;
    public ArrayList<TodoNotes> todoNotesList = new ArrayList<>();
    ActivityResultLauncher<Intent> noteResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getData() != null) {
            TodoNotes newTodo = result.getData().getParcelableExtra(TODO_NOTE);
            int index = todoNotesList.indexOf(newTodo);
            if (index == NO_POSITION) {
                todoNotesList.add(newTodo);
                notesAdapter.notifyItemInserted(todoNotesList.size());
            } else {
                todoNotesList.set(index, newTodo);
                notesAdapter.notifyItemChanged(index);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView notesView = findViewById(R.id.todo_notes_activity_res_view);
        FloatingActionButton addNote = findViewById(R.id.todo_notes_activity_btn_add_note);
        NotesAdapter.OnTodoClickListener onTodoListener = (position) -> {
            Intent todoEditedNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            todoEditedNote.putExtra(TODO_NOTE, todoNotesList.get(position));
            noteResult.launch(todoEditedNote);
        };

        notesAdapter = new NotesAdapter(todoNotesList, onTodoListener);
        notesView.setLayoutManager(new LinearLayoutManager(this));
        notesView.setAdapter(notesAdapter);

        addNote.setOnClickListener(view -> {
            Intent addNewNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            noteResult.launch(addNewNote);
        });
    }
}

