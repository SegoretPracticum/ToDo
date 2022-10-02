package com.example.myapplication;
import static com.example.myapplication.TodoNotesActivity.NEW_NOTE;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;

import android.widget.EditText;

public class NoteEditActivity extends AppCompatActivity {
    private Editable noteToRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        EditText enterNote = findViewById(R.id.enterNote);
        String newNote = getIntent().getStringExtra(NEW_NOTE);
        enterNote.setText(newNote);
        noteToRv = enterNote.getText();

        enterNote.setOnClickListener(view -> {
            TodoNotes newTodo = new TodoNotes(noteToRv.toString());
            Intent intent = new Intent(NoteEditActivity.this, TodoNotesActivity.class);
            intent.putExtra(NEW_NOTE, newTodo);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}




