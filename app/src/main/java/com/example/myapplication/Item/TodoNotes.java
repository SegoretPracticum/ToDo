package com.example.myapplication.Item;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class TodoNotes implements Parcelable {
    private String noteText;
    private String idTodo;

    public void setTodoId(String idTodo) {
        this.idTodo = idTodo;
    }

    public String getTodoId() {
        return idTodo;
    }

    public TodoNotes(String noteText, String idTodo) {
        this.noteText = noteText;
        this.idTodo = idTodo;
    }

    protected TodoNotes(Parcel in) {
        noteText = in.readString();
        idTodo = in.readString();
    }

    public static final Creator<TodoNotes> CREATOR = new Creator<TodoNotes>() {
        @Override
        public TodoNotes createFromParcel(Parcel in) {
            return new TodoNotes(in);
        }

        @Override
        public TodoNotes[] newArray(int size) {
            return new TodoNotes[size];
        }
    };

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteText() {
        return noteText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(noteText);
        parcel.writeString(idTodo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoNotes)) return false;
        TodoNotes todoNotes = (TodoNotes) o;
        return Objects.equals(idTodo, todoNotes.idTodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTodo);
    }
}