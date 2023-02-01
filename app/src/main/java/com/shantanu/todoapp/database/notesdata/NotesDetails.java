package com.shantanu.todoapp.database.notesdata;

public class NotesDetails {
    private int notes_id, user_id;
    private String notes_title, notes_description;

    public NotesDetails() {
    }

    public int getNotes_id() {
        return notes_id;
    }

    public void setNotes_id(int notes_id) {
        this.notes_id = notes_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNotes_title() {
        return notes_title;
    }

    public void setNotes_title(String notes_title) {
        this.notes_title = notes_title;
    }

    public String getNotes_description() {
        return notes_description;
    }

    public void setNotes_description(String notes_description) {
        this.notes_description = notes_description;
    }
}
