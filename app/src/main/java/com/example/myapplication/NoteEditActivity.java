package com.example.myapplication;

import static com.example.myapplication.TodoNotesActivity.INDEX_COUNT;
import static com.example.myapplication.TodoNotesActivity.TODO_NOTE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NoteEditActivity extends AppCompatActivity {
    private final static int DEF_VAL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        EditText enterNote = findViewById(R.id.note_edit_activity_edit_text_enter_note);
        String note = getIntent().getStringExtra(TODO_NOTE);
        int ind = getIntent().getIntExtra(INDEX_COUNT, DEF_VAL);
        enterNote.setText(note);

        enterNote.setOnClickListener(view -> {
            Intent intent = new Intent();
            TodoNotes newTodo = new TodoNotes(enterNote.getText().toString(), ind);
            intent.putExtra(TODO_NOTE, newTodo);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
