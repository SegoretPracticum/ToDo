package com.example.myapplication;

import static com.example.myapplication.TodoNotesActivity.NEW_NOTE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NoteEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        EditText enterNote = findViewById(R.id.note_edit_activity_edit_text_enter_note);
        String newNote = getIntent().getStringExtra(NEW_NOTE);
        enterNote.setText(newNote);

        enterNote.setOnClickListener(view -> {
            TodoNotes newTodo = new TodoNotes(enterNote.getText().toString());
            Intent intent = new Intent();
            intent.putExtra(NEW_NOTE, newTodo);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
