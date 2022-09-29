package com.example.myapplication;
import static com.example.myapplication.TodoNotesActivity.NEW_NOTE;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class NoteEditActivity extends AppCompatActivity {

    private EditText enterNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        enterNote = (EditText) findViewById(R.id.enterNote);
        enterNote.setText(getIntent().getStringExtra(NEW_NOTE));
        View.OnClickListener onClickListener = view -> {
    Intent intent = new Intent();
    Editable text = enterNote.getText();
    intent.putExtra(getString(R.string.note), text.toString());
    setResult(RESULT_OK, intent);
    finish();
    };
        enterNote.setOnClickListener(onClickListener);
   }
 }