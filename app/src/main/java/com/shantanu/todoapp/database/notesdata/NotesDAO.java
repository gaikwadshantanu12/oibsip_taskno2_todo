package com.shantanu.todoapp.database.notesdata;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class NotesDAO {
    private SQLiteOpenHelper helper = null;

    public NotesDAO(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    /* insert new notes */
    public boolean insertNewNotes(NotesDetails details){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NotesConstants.COL_NOTES_TITLE, details.getNotes_title());
        cv.put(NotesConstants.COL_NOTES_DESCRIPTION, details.getNotes_description());
        cv.put(NotesConstants.COL_NOTES_USER_ID, details.getUser_id());

        long row_id = db.insert(NotesConstants.TABLE_NAME, null, cv);
        return row_id > 0;
    }

    /* get particular users all notes */
    public ArrayList<NotesDetails> getAllNotesOfParticularUser(int user_id) {
        ArrayList<NotesDetails> notes_list = new ArrayList<NotesDetails>();
        NotesDetails details = null;

        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "select * from " + NotesConstants.TABLE_NAME + " where " + NotesConstants.COL_NOTES_USER_ID + "=" + user_id;
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()) {
            do {
                details = new NotesDetails();
                details.setNotes_id(cursor.getInt(0));
                details.setNotes_title(cursor.getString(1));
                details.setNotes_description(cursor.getString(2));
                details.setUser_id(cursor.getInt(3));

                notes_list.add(details);
            } while (cursor.moveToNext());
        }

        return notes_list;
    }

    /* delete notes */
    public boolean deleteNotesByID(int notes_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where_clause = NotesConstants.COL_NOTES_ID + "=" + notes_id;
        int row_id = db.delete(NotesConstants.TABLE_NAME, where_clause, null);
        return row_id > 0;
    }

    /* update notes */
    public boolean updateNotes(NotesDetails details ,int notes_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NotesConstants.COL_NOTES_TITLE, details.getNotes_title());
        cv.put(NotesConstants.COL_NOTES_DESCRIPTION, details.getNotes_description());
        cv.put(NotesConstants.COL_NOTES_USER_ID, details.getUser_id());

        int row_id = db.update(NotesConstants.TABLE_NAME,cv,NotesConstants.COL_NOTES_ID + "=?", new String[]{String.valueOf(notes_id)});
        return row_id > 0;
    }

    /* delete all notes of particular user */
    public boolean deleteAllNotesOfParticularUser(int user_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where_clause = NotesConstants.COL_NOTES_USER_ID + "=" + user_id;
        int row_id = db.delete(NotesConstants.TABLE_NAME, where_clause, null);
        return row_id > 0;
    }
}
