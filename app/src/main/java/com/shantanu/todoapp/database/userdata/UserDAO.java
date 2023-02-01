package com.shantanu.todoapp.database.userdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.shantanu.todoapp.database.notesdata.NotesConstants;

public class UserDAO {
    private SQLiteOpenHelper helper = null;

    public UserDAO(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    /* Insert new user data into userdetails table */
    public boolean insertNewUser(UserDetails details){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(UserDBConstants.COL_USER_NAME, details.getUser_name());
        cv.put(UserDBConstants.COL_USER_EMAIL, details.getUser_email());
        cv.put(UserDBConstants.COL_USER_PASSWORD, details.getUser_password());

        long row_id = db.insert(UserDBConstants.TABLE_NAME, null, cv);
        return row_id > 0;
    }

    /* login existing user */
    public UserDetails loginUser(UserDetails details) {
        UserDetails det = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "select * from " + UserDBConstants.TABLE_NAME + " where " + UserDBConstants.COL_USER_EMAIL + "=? and " + UserDBConstants.COL_USER_PASSWORD + "=?;";

        Cursor cursor = db.rawQuery(query, new String[]{details.getUser_email(), details.getUser_password()});

        if(cursor.moveToFirst()) {
            do {
                det = new UserDetails();
                det.setUser_id(cursor.getInt(0));
                det.setUser_name(cursor.getString(1));
                det.setUser_email(cursor.getString(2));
                det.setUser_password(cursor.getString(3));
            }while (cursor.moveToNext());
        }

        cursor.close();

        return det;
    }

    /* get user info from user id */
    public UserDetails getUserInfoFromUserID(int id) {
        UserDetails det = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "select * from " + UserDBConstants.TABLE_NAME + " where " + UserDBConstants.COL_USER_ID + "=" + id;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                det = new UserDetails();
                det.setUser_id(cursor.getInt(0));
                det.setUser_name(cursor.getString(1));
                det.setUser_email(cursor.getString(2));
                det.setUser_password(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return det;
    }

    /* delete user forever */
    public boolean deleteUserByID(int user_id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where_clause = UserDBConstants.COL_USER_ID + "=" + user_id;
        int row_id = db.delete(UserDBConstants.TABLE_NAME, where_clause, null);
        return row_id > 0;
    }
}
