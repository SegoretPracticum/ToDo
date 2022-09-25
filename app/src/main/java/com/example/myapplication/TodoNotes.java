package com.example.myapplication;

public class TodoNotes {
    private String noteText;

    public TodoNotes(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
}
