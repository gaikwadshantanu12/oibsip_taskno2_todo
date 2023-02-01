package com.shantanu.todoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import com.shantanu.todoapp.database.notesdata.NotesConstants;
import com.shantanu.todoapp.database.tododata.TodoDBConstants;
import com.shantanu.todoapp.database.userdata.UserDBConstants;

public class TodoDBHelper extends SQLiteOpenHelper {

    public TodoDBHelper(@Nullable Context context) {
        super(context, "todo_app.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_user_table_query = "create table " + UserDBConstants.TABLE_NAME + "(" + UserDBConstants.COL_USER_ID + " integer primary key autoincrement, " + UserDBConstants.COL_USER_NAME + " text not null, " + UserDBConstants.COL_USER_EMAIL + " text not null, " + UserDBConstants.COL_USER_PASSWORD + " text not null)";
        db.execSQL(create_user_table_query);
        String create_todo_table_query = "create table " + TodoDBConstants.TABLE_NAME + "(" + TodoDBConstants.COL_TODO_ID + " integer primary key autoincrement, " + TodoDBConstants.COL_TODO_TITLE + " text not null, " + TodoDBConstants.COL_TODO_USER_ID + " integer not null)";
        db.execSQL(create_todo_table_query);
        String create_notes_table_query = "create table " + NotesConstants.TABLE_NAME + "(" + NotesConstants.COL_NOTES_ID + " integer primary key autoincrement, " + NotesConstants.COL_NOTES_TITLE + " text not null, " + NotesConstants.COL_NOTES_DESCRIPTION + " text not null, " + NotesConstants.COL_NOTES_USER_ID + " integer not null)";
        db.execSQL(create_notes_table_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
