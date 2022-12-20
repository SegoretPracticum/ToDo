package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView note;

    public ViewHolder(@NonNull View itemView, NotesAdapter.OnTodoClickListener onTodoClickListener) {
        super(itemView);
        note = itemView.findViewById(R.id.item_notes_view_text_view);
        MaterialCardView materialCardView = itemView.findViewById(R.id.card_todo);
        materialCardView.setOnClickListener(view ->
                onTodoClickListener.onTodoClick(getAdapterPosition()));
    }

    public void bindViewHolder(TodoNotes todoNote) {
        note.setText(todoNote.getNoteText());
    }
}