package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    final TextView note;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        note = itemView.findViewById(R.id.notes_view_text_view);
    }

    public void bindViewHolder(TodoNotes todo) {
        note.setText(todo.getNoteText());
    }
}
