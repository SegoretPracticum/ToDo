package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<TodoNotes> notesMassive;
    NotesAdapter (Context context,List<TodoNotes> notesMassive) {
        this.notesMassive = notesMassive;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notesview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    TodoNotes todoNotes = notesMassive.get(position);
    holder.note.setText(todoNotes.getNoteText());
    }

    @Override
    public int getItemCount() {
        return notesMassive.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView note;
        public ViewHolder(View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.noteView);
        }
    }
}
