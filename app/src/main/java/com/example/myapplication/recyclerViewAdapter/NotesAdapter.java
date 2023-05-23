package com.example.myapplication.recyclerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Item.TodoNotes;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<ViewHolder> {

    public interface OnTodoClickListener {
        void onTodoClick(TodoNotes todoNotes);

    }

    private List<TodoNotes> notesMassive = new ArrayList<>();
    private final OnTodoClickListener onTodoClickListener;

    public NotesAdapter(OnTodoClickListener onTodoClickListener) {
        this.onTodoClickListener = onTodoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_notesview, parent, false);
        return new ViewHolder(view, this.onTodoClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoNotes todoNotes = notesMassive.get(position);
        holder.bindViewHolder(todoNotes);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshList(List<TodoNotes> list) {
        notesMassive = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notesMassive.size();
    }
}