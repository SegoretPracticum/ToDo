package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class TodoNotes implements Parcelable {
    private final String noteText;
    private final int index;

    public TodoNotes(String noteText, int index) {
        this.noteText = noteText;
        this.index = index;
    }

    protected TodoNotes(Parcel in) {
        noteText = in.readString();
        index = in.readInt();
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
        parcel.writeInt(index);
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoNotes)) return false;
        TodoNotes todoNotes = (TodoNotes) o;
        return getIndex() == todoNotes.getIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIndex());
    }
}
