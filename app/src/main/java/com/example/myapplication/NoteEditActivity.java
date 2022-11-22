package com.example.myapplication;


import static com.example.myapplication.TodoNotesActivity.TODO_NOTE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class NoteEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        EditText enterNote = findViewById(R.id.note_edit_activity_edit_text_enter_note);
        TodoNotes note = getIntent().getParcelableExtra(TODO_NOTE);
        if (note != null) {
            enterNote.setText(note.getNoteText());
        } else {
            note = new TodoNotes("", UUID.randomUUID().toString());
        }

        TodoNotes finalNote = note;
        enterNote.setOnClickListener(view -> {
            Intent intent = new Intent();
            if (enterNote.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), R.string.note_edit_activity_edit_text_toast, Toast.LENGTH_SHORT).show();
            } else {
                finalNote.setNoteText(enterNote.getText().toString());
                intent.putExtra(TODO_NOTE, finalNote);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}