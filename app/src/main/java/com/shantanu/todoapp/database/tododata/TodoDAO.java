package com.shantanu.todoapp.database.tododata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.shantanu.todoapp.database.notesdata.NotesConstants;
import com.shantanu.todoapp.database.notesdata.NotesDetails;

import java.util.ArrayList;

public class TodoDAO {
    private SQLiteOpenHelper helper = null;

    public TodoDAO(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    /* insert new todo */
    public boolean insertNewTODO(TodoDetails todoDetails){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TodoDBConstants.COL_TODO_TITLE, todoDetails.getTodo_title());
        cv.put(TodoDBConstants.COL_TODO_USER_ID, todoDetails.getUser_id());

        long row_id = db.insert(TodoDBConstants.TABLE_NAME, null, cv);
        return row_id > 0;
    }

    /* get particular users all todo */
    public ArrayList<TodoDetails> getAllTodoOfParticularUser(int user_id) {
        ArrayList<TodoDetails> todo_list = new ArrayList<TodoDetails>();
        TodoDetails details = null;

        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "select * from " + TodoDBConstants.TABLE_NAME + " where " + TodoDBConstants.COL_TODO_USER_ID + "=" + user_id;
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()) {
            do {
                details = new TodoDetails();
                details.setTodo_id(cursor.getInt(0));
                details.setTodo_title(cursor.getString(1));
                details.setUser_id(cursor.getInt(2));

                todo_list.add(details);
            } while (cursor.moveToNext());
        }

        return todo_list;
    }

    /* delete todo by id */
    public boolean deleteTodo(int todo_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where_clause = TodoDBConstants.COL_TODO_ID + "=" + todo_id;
        int row_id = db.delete(TodoDBConstants.TABLE_NAME, where_clause, null);
        return row_id > 0;
    }

    /* update todo by id */
    public boolean updateTodo(TodoDetails details , int todo_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TodoDBConstants.COL_TODO_TITLE, details.getTodo_title());
        cv.put(TodoDBConstants.COL_TODO_USER_ID, details.getUser_id());

        int row_id = db.update(TodoDBConstants.TABLE_NAME,cv,TodoDBConstants.COL_TODO_ID + "=?", new String[]{String.valueOf(todo_id)});
        return row_id > 0;
    }

    /* delete all todo of particular user */
    public boolean deleteAllTODOOfParticularUser(int user_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where_clause = TodoDBConstants.COL_TODO_USER_ID + "=" + user_id;
        int row_id = db.delete(TodoDBConstants.TABLE_NAME, where_clause, null);
        return row_id > 0;
    }
}
