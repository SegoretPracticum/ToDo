package com.example.myapplication;

import android.annotation.SuppressLint;
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
    public RecyclerView notesView;
    public static final String NEW_NOTE = "NEW_NOTE";
    private final ArrayList<TodoNotes> todoNotes= new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesView = findViewById(R.id.resView);
        Button addNote = findViewById(R.id.addNote);
        NotesAdapter notesAdapter = new NotesAdapter(todoNotes);
        notesView.setLayoutManager(new LinearLayoutManager(this));
        notesView.setAdapter(notesAdapter);

         ActivityResultLauncher<Intent> noteResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent intent = result.getData();
            TodoNotes newTodo = intent.getParcelableExtra(NEW_NOTE);
            todoNotes.add(newTodo);
            notesAdapter.notifyDataSetChanged();
        });

                addNote.setOnClickListener(view -> {
                    Intent addNewNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
                    noteResult.launch(addNewNote);
                });
    }


    }
