package com.example.myapplication.activities;

import static com.example.myapplication.activities.TodoNotesActivity.NO_ERROR;
import static com.example.myapplication.activities.TodoNotesActivity.TODO_NOTE;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.Item.TodoNotes;
import com.example.myapplication.R;
import com.example.myapplication.data.DBHelperManager;
import com.example.myapplication.data.TodoListDBHelper;
import com.example.myapplication.data.TodoNotesDBHelper;
import com.example.myapplication.interfaces.ConnectCheck;
import com.example.myapplication.interfaces.TodoNotesAPI;
import com.example.myapplication.interfaces.TodoNotesDAO;
import com.example.myapplication.network.ConnectChecker;
import com.example.myapplication.network.HttpConnect;
import com.example.myapplication.repository.TodoRepository;
import com.example.myapplication.viewModels.NoteEditModelFactory;
import com.example.myapplication.viewModels.NoteEditViewModel;
import com.google.android.material.snackbar.Snackbar;

public class NoteEditActivity extends AppCompatActivity {
    private EditText enterNote;
    private Toolbar toolbar;
    private ActionMenuItemView toolbarBtn;
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;
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
        constraintLayout = findViewById(R.id.note_edit_activity_constraint_layout);
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

    private void setToolbarTitle() {
        if (getIntent().getParcelableExtra(TODO_NOTE) != null) {
            toolbar.setTitle(R.string.note_edit_activity_toolbar_title_edit_todo);
        } else {
            toolbar.setTitle(R.string.note_edit_activity_toolbar_title_add_todo);
        }
    }

    private void viewModelInit() {
        TodoNotesDBHelper todoNotesDBHelper = new TodoNotesDBHelper(getApplicationContext());
        TodoListDBHelper todoListDBHelper = new TodoListDBHelper(getApplicationContext());
        ConnectCheck connectChecker = new ConnectChecker(getApplicationContext());
        TodoNotesDAO todoNotesDAO = new DBHelperManager(todoNotesDBHelper, todoListDBHelper);
        TodoNotesAPI todoNotesAPI = new HttpConnect();
        TodoRepository todoRepository = TodoRepository.getInstance(todoNotesDAO, connectChecker,todoNotesAPI);
        viewModel = new ViewModelProvider(this,
                new NoteEditModelFactory(getIntent().getParcelableExtra(TODO_NOTE),
                        todoRepository)).get(NoteEditViewModel.class);
        viewModel.getTodoText().observe(this, todoText -> {
            if (!todoText.equals(enterNote.getText().toString())) {
                enterNote.setText(todoText);
            }
        });
        viewModel.getToolbarNavigationEvent().observe(this, onNavigation -> finish());
        viewModel.getSendTodo().observe(this, this::sendTodoNote);
        viewModel.getError().observe(this, error -> {
            if (!error.equals(NO_ERROR)) {
                Snackbar snackbar = Snackbar.make(constraintLayout,error,
                        Snackbar.LENGTH_LONG);
                snackbar.show();
                viewModel.resetConnectionErrors();
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