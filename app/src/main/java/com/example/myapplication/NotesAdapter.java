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

    private final List<TodoNotes> notesMassive;

    NotesAdapter(ArrayList<TodoNotes> notesMassive) {
        this.notesMassive = notesMassive;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notesview, parent, false);
        return new ViewHolder(view);
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
