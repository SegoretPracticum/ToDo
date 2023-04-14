package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class TodoNotesActivity extends AppCompatActivity {

    private static final int SPAN_COUNT = 2;
    public static final String TODO_NOTE = "TODO_NOTE";
    private NotesAdapter notesAdapter;
    private RecyclerView notesView;
    private FloatingActionButton addNote;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TodoNotesViewModel viewModel;
    private ConstraintLayout constraintLayout;

    private final ActivityResultLauncher<Intent> noteResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getData() != null) {
            TodoNotes newTodo = result.getData().getParcelableExtra(TODO_NOTE);
            viewModel.onResultReceived(newTodo);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.todo_notes_activity_constraint_layout);
        notesView = findViewById(R.id.todo_notes_activity_res_view);
        swipeRefreshLayout = findViewById(R.id.activity_todo_notes_list_swipe_refresh_layout);
        addNote = findViewById(R.id.todo_notes_activity_btn_add_note);
        initViewModel();
        joinNotesAdapter();
        addNote.setOnClickListener(view -> viewModel.buttonClicked());
        swipeRefreshLayout.setOnRefreshListener(() -> viewModel.getNotesList());
    }

    private void onButtonClicked(Boolean btnClick) {
        if (btnClick) {
            Intent addNewNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            noteResult.launch(addNewNote);
            viewModel.clickReset();
        }
    }

    private void onItemClicked(TodoNotes todoNotes) {
        if (todoNotes != null) {
            Intent todoEditedNote = new Intent(TodoNotesActivity.this, NoteEditActivity.class);
            todoEditedNote.putExtra(TODO_NOTE, todoNotes);
            noteResult.launch(todoEditedNote);
            viewModel.clickReset();
        }
    }

    private void initViewModel() {
        ConnectCheck connectChecker = new ConnectChecker(getApplicationContext());
        AppIdentification appIdentification = new AppIdentifier(getApplicationContext());
        viewModel = new ViewModelProvider(this,
                new TodoNotesViewModelFactory(connectChecker, appIdentification)).get(TodoNotesViewModel.class);
        viewModel.getAddTodoEvent().observe(this, this::onButtonClicked);
        viewModel.getEditTodoEvent().observe(this, this::onItemClicked);
        viewModel.getTodoList().observe(this, todoList -> notesAdapter.refreshList(todoList));
        viewModel.getInternetConnectionError().observe(this, internetConnectionError -> {
            if (internetConnectionError) {
                Snackbar snackbar = Snackbar.make(constraintLayout, R.string.internet_connection_snackbar,
                        Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        viewModel.getErrorWorkingWithServer().observe(this, errorOfInternet -> {
            if (errorOfInternet) {
                Snackbar snackbar = Snackbar.make(constraintLayout, R.string.failed_server_snackbar,
                        Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
        viewModel.getRefreshTodoList().observe(this, startRefresh -> {
            if (startRefresh) {
                swipeRefreshLayout.setRefreshing(true);
                addNote.setVisibility(View.GONE);
                notesView.setEnabled(false);
            } else {
                addNote.setVisibility(View.VISIBLE);
                notesView.setEnabled(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void joinNotesAdapter() {
        NotesAdapter.OnTodoClickListener onTodoListener = (todoNotes) -> viewModel.todoItemClicked(todoNotes);
        notesAdapter = new NotesAdapter(onTodoListener);
        notesView.setLayoutManager(new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL));
        notesView.setAdapter(notesAdapter);
    }
}