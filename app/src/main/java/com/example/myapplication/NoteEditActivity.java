package com.example.myapplication;


import static com.example.myapplication.TodoNotesActivity.TODO_NOTE;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.UUID;

public class NoteEditActivity extends AppCompatActivity {
    private EditText enterNote;
    private TodoNotes finalNote;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar_button_send_note) {
            Intent intent = new Intent();
            if (enterNote.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(), R.string.note_edit_activity_edit_text_toast, Toast.LENGTH_SHORT).show();
            } else {
                finalNote.setNoteText(enterNote.getText().toString());
                intent.putExtra(TODO_NOTE, finalNote);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = findViewById(R.id.note_edit_activity_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(item -> finish());
        enterNote = findViewById(R.id.note_edit_activity_edit_text_enter_note);
        TodoNotes note = getIntent().getParcelableExtra(TODO_NOTE);
        if (note != null) {
            enterNote.setText(note.getNoteText());
        } else {
            note = new TodoNotes("", UUID.randomUUID().toString());
        }
        finalNote = note;
    }
}