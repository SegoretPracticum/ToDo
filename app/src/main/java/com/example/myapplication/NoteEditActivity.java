package com.example.myapplication;

import static com.example.myapplication.TodoNotesActivity.NEW_NOTE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class NoteEditActivity extends AppCompatActivity implements View.OnClickListener {

    EditText enterNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        enterNote = (EditText) findViewById(R.id.enterNote);
        enterNote.setText(getIntent().getStringExtra(NEW_NOTE));
        enterNote.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        Editable text = enterNote.getText();
        intent.putExtra("note", text.toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}