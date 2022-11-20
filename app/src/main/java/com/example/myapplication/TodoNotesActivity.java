package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoNotesActivity extends AppCompatActivity {
    public static final String TODO_NOTE = "TODO_NOTE";
    public NotesAdapter notesAdapter;
    public ArrayList<TodoNotes> todoNotesList = new ArrayList<>();
    ActivityResultLauncher<Intent> noteResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getData() != null) {
            TodoNotes newTodo = result.getData().getParcelableExtra(TODO_NOTE);
            if (!todoNotesList.contains(newTodo)) {
                todoNotesList.add(newTodo);
                notesAdapter.notifyItemInserted(todoNotesList.size());
            } else {
                todoNotesList.set(todoNotesList.indexOf(newTodo), newTodo);
                notesAdapter.notifyItemChanged(todoNotesList.indexOf(newTodo));
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView notesView = findViewById(R.id.todo_notes_activity_res_view);
        Button addNote = findViewById(R.id.todo_notes_activity_btn_add_note);
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

