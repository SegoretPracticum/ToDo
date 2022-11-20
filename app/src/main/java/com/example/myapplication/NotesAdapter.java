package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<ViewHolder> {

    interface OnTodoClickListener {
        void OnTodoClick(int position);
    }

    private final List<TodoNotes> notesMassive;
    private final OnTodoClickListener onTodoClickListener;

    NotesAdapter(ArrayList<TodoNotes> notesMassive, OnTodoClickListener onTodoClickListener) {
        this.notesMassive = notesMassive;
        this.onTodoClickListener = onTodoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notesview, parent, false);
        return new ViewHolder(view, this.onTodoClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoNotes todoNotes = notesMassive.get(position);
        holder.bindViewHolder(todoNotes);
    }

    @Override
    public int getItemCount() {
        return notesMassive.size();
    }
}