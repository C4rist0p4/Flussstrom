package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Flusstrom";

    DatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate");

        String user = ("CREATE TABLE IF NOT EXISTS Benutzer (User_id INTEGER PRIMARY KEY, idBenutzer TEXT, anlage TEXT)");
        String messages = ("CREATE TABLE IF NOT EXISTS Meldungen (datum TEXT, fk_meldungstyp TEXT, bemerkungMel TEXT)");
        // Execute script.
        db.execSQL(user);
        db.execSQL(messages);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Benutzer");
        db.execSQL("DROP TABLE IF EXISTS Meldungen");
        // Create tables again
        onCreate(db);
    }

    void addUser(HashMap data) {
        Log.i(TAG, "addUser" );

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("idBenutzer", Objects.requireNonNull(data.get("idBenutzer")).toString());
        values.put("anlage", Objects.requireNonNull(data.get("anlage")).toString());
        // Inserting Row
        db.insert("Benutzer", null, values);
        // Closing database connection
        db.close();
    }

    List<String> getUser() {
        Log.i(TAG, "getUser");

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Benutzer", new String[] { "idBenutzer",
                        "anlage" }, "User_id=1",null,
                null, null, null);

        assert cursor != null;
        cursor.moveToFirst();
        Log.i("cursor", cursor.toString());
        List<String> user = new ArrayList<>();
        user.add(cursor.getString(0));
        user.add(cursor.getString(1));

        return user;
    }

    void addMessages(String date, String title, String content) {
        Log.i(TAG, "addMessages" );

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("datum",date );
        values.put("fk_meldungstyp", title);
        values.put("bemerkungMel", content);

        db.insert("Meldungen", null, values);

        db.close();
    }

    List<Messages> getAllMessages() {
        Log.i(TAG, "getAllNotes ... " );

        List<Messages> messagesList = new ArrayList<>();

        String selectQuery = "SELECT * FROM Meldungen";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Messages messages = new Messages();
                messages.setMessagesDate(cursor.getString(0));
                messages.setMessagesTitle(cursor.getString(1));
                messages.setMessagesContent(cursor.getString(2));

                messagesList.add(messages);
            } while (cursor.moveToNext());
        }

        return messagesList;
    }

/*    public void deleteMessages(Messages messages) {
        Log.i(TAG, "deleteMessages");

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Meldungen, messages_ID = ?",
                new String[] { String.valueOf(messages.getId()) });
        db.close();
    }*/
}
