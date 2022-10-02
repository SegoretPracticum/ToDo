package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class TodoNotes implements Parcelable {
    private String noteText;

    public TodoNotes(String noteText) {
        this.noteText = noteText;
    }

    protected TodoNotes(Parcel in) {
        noteText = in.readString();
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

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(noteText);
    }
}
