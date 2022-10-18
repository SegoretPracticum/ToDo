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
    public static final String NEW_NOTE = "NEW_NOTE";
    private final ArrayList<TodoNotes> todoNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView notesView = findViewById(R.id.todo_notes_activity_res_view);
        Button addNote = findViewById(R.id.todo_notes_activity_btn_add_note);
        NotesAdapter notesAdapter = new NotesAdapter(todoNotes);
        notesView.setLayoutManager(new LinearLayoutManager(this));
        notesView.setAdapter(notesAdapter);

        ActivityResultLauncher<Intent> noteResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getData() != null) {
                TodoNotes newTodo = result.getData().getParcelableExtra(NEW_NOTE);
                todoNotes.add(newTodo);
                notesAdapter.notifyItemInserted(todoNotes.size());
            }
        });

        addNote.setOnClickListener(view -> {
            Intent addNewNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            noteResult.launch(addNewNote);
        });
    }
}