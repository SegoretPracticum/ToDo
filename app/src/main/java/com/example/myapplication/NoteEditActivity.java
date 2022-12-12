package com.example.myapplication;


import static com.example.myapplication.TodoNotesActivity.TODO_NOTE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.UUID;

public class NoteEditActivity extends AppCompatActivity {
    private TodoNotes note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = findViewById(R.id.note_edit_activity_toolbar);
        toolbar.setNavigationOnClickListener(item -> finish());
        EditText enterNote = findViewById(R.id.note_edit_activity_edit_text_enter_note);
        note = getIntent().getParcelableExtra(TODO_NOTE);
        processingIntent(enterNote, toolbar);
        TodoNotes finalNote = note;
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener -> {
            sendTodoNote(finalNote, enterNote);
            return true;
        });
    }

    private void sendTodoNote(TodoNotes todoNotes, EditText editText) {
        Intent intent = new Intent();
        if (editText.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.note_edit_activity_edit_text_toast, Toast.LENGTH_SHORT).show();
        } else {
            todoNotes.setNoteText(editText.getText().toString());
            intent.putExtra(TODO_NOTE, todoNotes);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void processingIntent(EditText editText, Toolbar toolbar) {
        if (note != null) {
            toolbar.setTitle(R.string.note_edit_activity_toolbar_title_edit_todo);
            editText.setText(note.getNoteText());
        } else {
            toolbar.setTitle(R.string.note_edit_activity_toolbar_title_add_todo);
            note = new TodoNotes("", UUID.randomUUID().toString());
        }
    }
}