package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView note;

    public ViewHolder(@NonNull View itemView, NotesAdapter.OnTodoClickListener onTodoClickListener) {
        super(itemView);
        note = itemView.findViewById(R.id.notes_view_text_view);
        itemView.setOnClickListener(view ->
                onTodoClickListener.onTodoClick(getAdapterPosition()));
    }

    public void bindViewHolder(TodoNotes todoNote) {
        note.setText(todoNote.getNoteText());
    }
}