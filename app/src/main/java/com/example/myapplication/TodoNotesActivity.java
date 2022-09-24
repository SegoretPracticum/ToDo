package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TodoNotesActivity extends AppCompatActivity {
    TextView note;
    public static final String NEW_NOTE = "NEW_NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addNote = findViewById(R.id.addNote);
        note = findViewById(R.id.note);
        ActivityResultLauncher<Intent> noteResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Intent intent = result.getData();
            assert intent != null;
            String accessMessage = intent.getStringExtra(NEW_NOTE);
            addNote.setText(accessMessage);
        });

        note.setOnClickListener(view -> {
            Intent noteEdit = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            noteEdit.putExtra(NEW_NOTE,note.getText().toString());
            noteResult.launch(noteEdit);
        });

        addNote.setOnClickListener(view -> {
                    Intent addNewNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
                    noteResult.launch(addNewNote);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        String note1 = data.getStringExtra("note");
        note.setText(note1);
    }
}