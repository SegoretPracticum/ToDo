package com.example.myapplication;

import static com.example.myapplication.TodoNotesActivity.TODO_NOTE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.lifecycle.ViewModelProvider;

public class NoteEditActivity extends AppCompatActivity {
    private EditText enterNote;
    private Toolbar toolbar;
    private NoteEditViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        enterNote = findViewById(R.id.note_edit_activity_edit_text_enter_note);
        toolbar = findViewById(R.id.note_edit_activity_toolbar);
        setToolbarTitle();
        viewModelInit();
        textChangeWatch();
        toolbar.setNavigationOnClickListener(item -> viewModel.navigationClicked());
        toolbarItemClicked();
    }

    private void sendTodoNote(TodoNotes todoNotes) {
        if (todoNotes != null) {
            Intent intent = new Intent();
            todoNotes.setNoteText(enterNote.getText().toString());
            intent.putExtra(TODO_NOTE, todoNotes);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void onNavigationClicked(Boolean onNavigation) {
        if (onNavigation) {
            finish();
        }
    }

    private void textChangeWatch() {
        enterNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                viewModel.onTextNoteChanged(editable.toString());
            }
        });
    }

    private void showToast(Boolean emptyString) {
        if (emptyString) {
            Toast.makeText(getApplicationContext(), R.string.note_edit_activity_edit_text_toast, Toast.LENGTH_SHORT).show();
            viewModel.clickReset();
        }
    }

    private void setToolbarTitle() {
        if (getIntent().getParcelableExtra(TODO_NOTE) != null) {
            toolbar.setTitle(R.string.note_edit_activity_toolbar_title_edit_todo);
        } else {
            toolbar.setTitle(R.string.note_edit_activity_toolbar_title_add_todo);
        }
    }

    private void viewModelInit() {
        viewModel = new ViewModelProvider(this).get(NoteEditViewModel.class);
        viewModel.setNewTodo(getIntent().getParcelableExtra(TODO_NOTE));
        viewModel.getTodoText().observe(this, todoText -> {
            if (!todoText.equals(enterNote.getText().toString())) {
                enterNote.setText(todoText);
            }
        });
        viewModel.getBackToMainActivityEvent().observe(this, this::onNavigationClicked);
        viewModel.getSendTodo().observe(this, this::sendTodoNote);
        viewModel.getEmptyTodoInput().observe(this, this::showToast);
    }

    private void toolbarItemClicked() {
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener -> {
            viewModel.onBtnToolbarClicked(enterNote.getText().toString());
            return true;
        });
    }
}