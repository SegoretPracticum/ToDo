package com.example.myapplication;

import static com.example.myapplication.TodoNotesActivity.TODO_NOTE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

public class NoteEditActivity extends AppCompatActivity {
    private EditText enterNote;
    private Toolbar toolbar;
    private ActionMenuItemView toolbarBtn;
    private ProgressBar progressBar;
    private NoteEditViewModel viewModel;

    private final Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            viewModel.onBtnToolbarClicked(enterNote.getText().toString());
            return true;
        }
    };

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //        do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //        do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            viewModel.onTextNoteChanged(editable.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        enterNote = findViewById(R.id.note_edit_activity_edit_text_enter_note);
        toolbar = findViewById(R.id.note_edit_activity_toolbar);
        toolbarBtn = toolbar.findViewById(R.id.menu_toolbar_button_send_note);
        progressBar = findViewById(R.id.note_edit_activity_progressBar);
        setToolbarTitle();
        viewModelInit();
        enterNote.addTextChangedListener(textWatcher);
        toolbar.setNavigationOnClickListener(item -> viewModel.navigationClicked());
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    private void sendTodoNote(TodoNotes todoNotes) {
        Intent intent = new Intent();
        intent.putExtra(TODO_NOTE, todoNotes);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void showToast(Boolean needToShowError) {
        if (needToShowError) {
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
        ConnectCheck connectChecker = new ConnectChecker(getApplicationContext());
        viewModel = new ViewModelProvider(this,
                new NoteEditModelFactory(getIntent().getParcelableExtra(TODO_NOTE),
                        connectChecker)).get(NoteEditViewModel.class);
        viewModel.getTodoText().observe(this, todoText -> {
            if (!todoText.equals(enterNote.getText().toString())) {
                enterNote.setText(todoText);
            }
        });
        viewModel.getToolbarNavigationEvent().observe(this, onNavigation -> finish());
        viewModel.getSendTodo().observe(this, this::sendTodoNote);
        viewModel.getEmptyTodoInput().observe(this, this::showToast);
        viewModel.getErrorWorkingWithServer().observe(this, errorOfInternet -> {
            if (errorOfInternet) {
                Toast.makeText(getApplicationContext(), R.string.failed_server_toast,
                        Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getInternetConnectionError().observe(this, internetConnectionError -> {
            if (internetConnectionError) {
                Toast.makeText(getApplicationContext(), R.string.internet_connection_toast,
                        Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getSendTodoProcessing().observe(this, sendTodoProcessingGoes -> {
            if (sendTodoProcessingGoes) {
                progressBar.setVisibility(View.VISIBLE);
                toolbarBtn.setVisibility(View.GONE);
                enterNote.setEnabled(false);
            } else {
                progressBar.setVisibility(View.GONE);
                toolbarBtn.setVisibility(View.VISIBLE);
                enterNote.setEnabled(true);
            }
        });
    }
}