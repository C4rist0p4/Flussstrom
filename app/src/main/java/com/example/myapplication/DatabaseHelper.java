package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

        String user = ("CREATE TABLE IF NOT EXISTS Benutzer (User_id INTEGER PRIMARY KEY, idBenutzer TEXT)");
        String messages = ("CREATE TABLE IF NOT EXISTS Meldungen (datum TEXT, fk_meldungstyp TEXT, bemerkungMel TEXT)");
        String masterData  = ("CREATE TABLE IF NOT EXISTS Anlagen (idAnlagen TEXT, fk_betreiber TEXT, fk_hersteller TEXT," +
                "fk_adresse TEXT, installationsort TEXT, inbetriebnahme TEXT, anlagenname TEXT, seriennummer TEXT, bemerkung TEXT," +
                "aktiviert TEXT, kennung TEXT, ipaddress TEXT, tcpTriggerPort TEXT, emailenabled TEXT, leistung TEXT," +
                "anschlusswerte TEXT, datenanschluss TEXT, tiefgang TEXT, datenblatt TEXT, handbuch TEXT, " +
                "bemerkungtyp TEXT, bezeichnung TEXT, idAnlagentyp TEXT)");
        String systemIdData  = ("CREATE TABLE IF NOT EXISTS SystemId (Id INTEGER PRIMARY KEY, SystemId NUMBER)");
        // Execute script.
        db.execSQL(user);
        db.execSQL(messages);
        db.execSQL(masterData);
        db.execSQL(systemIdData);
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
        // Inserting Row
        db.insert("Benutzer", null, values);

        // Closing database connection
        db.close();
    }

    List<String> getUser() {
        Log.i(TAG, "getUser");

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Benutzer", new String[] { "idBenutzer"},
                "User_id=1",null,
                null, null, null);

        assert cursor != null;
        cursor.moveToFirst();
        Log.i("cursor", cursor.toString());
        List<String> user = new ArrayList<>();
        user.add(cursor.getString(0));
        db.close();
        return user;
    }

    void addSystemId(HashMap data) {
        Log.i(TAG, "addSystem" );

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            JSONObject jobject = new JSONObject(data.toString());
            JSONArray jarray = jobject.getJSONArray("anlage");

            for(int i=0; i < jarray.length(); i++) {
                JSONObject jo = new JSONObject(jarray.getString(i));
                values.put("SystemId", Objects.requireNonNull(jo.get("FK_Anlage").toString()));
                db.insert("SystemId", null, values);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Closing database connection
        db.close();
    }

    List<String> getSystemId() {
        Log.i(TAG, "getSystemId" );

        String selectQuery = "SELECT * FROM SystemId";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        assert cursor != null;

        List<String> SystemId = new ArrayList<>();

        int index = cursor.getColumnIndex("SystemId");
        if (cursor.moveToFirst()) {
            do {
                SystemId.add(cursor.getString(index));
            } while (cursor.moveToNext());
        }
        db.close();
        return SystemId;
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
        db.close();
        return messagesList;
    }

/*    public void deleteMessages(Messages messages) {
        Log.i(TAG, "deleteMessages");

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Meldungen, messages_ID = ?",
                new String[] { String.valueOf(messages.getId()) });
        db.close();
    }*/

    void addMasterData(HashMap data) {
        Log.i(TAG, "addMasterData" );

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        HashMap dataTyp = (HashMap) data.get("fk_anlagentyp");
        data.remove("fk_anlagentyp");

        Set entrySet = data.entrySet();
        for (Object o : entrySet) {
            Map.Entry me = (Map.Entry) o;
            values.put((String) me.getKey(), Objects.requireNonNull(me.getValue().toString()));
        }

        assert dataTyp != null;
        Set entryTyp = dataTyp.entrySet();
        for (Object o : entryTyp) {
            Map.Entry me = (Map.Entry) o;
            Object strVal = me.getValue();
            if (strVal != null) {
                values.put((String) me.getKey(), strVal.toString());
            }
        }
        // Inserting Row
        db.insert("Anlagen", null, values);
        // Closing database connection
        db.close();
    }

    HashMap getSystemDetails(String name) {
       Log.i(TAG, "getSystemDetails" );

       String selectQuery = "SELECT * FROM Anlagen WHERE anlagenname = '"+name+"'" ;
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

       assert cursor != null;
       String[] columnNames = cursor.getColumnNames();

        HashMap<String,String> temp = new HashMap<>();

        cursor.moveToFirst();
        for (String columnName : columnNames) {
            int index = cursor.getColumnIndex(columnName);
            String str = cursor.getString(index);

            temp.put(columnName, str);
        }

        db.close();
       return temp;
   }

    ArrayList<SystemItem> getSystemItem() {
       Log.i(TAG, "getSystemItem" );

       ArrayList<SystemItem> systemList = new ArrayList<>();

       String selectQuery = "SELECT * FROM Anlagen";
       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);

       if (cursor.moveToFirst()) {
           do {
               String name = cursor.getString(6);
               SystemItem systemItem = new SystemItem(name, "2", "2");
               systemList.add(systemItem);

           } while (cursor.moveToNext());
       }
        db.close();
        return systemList;
   }

   int countSystemes() {
       SQLiteDatabase db = this.getWritableDatabase();
       String count = "SELECT count(*) FROM Anlagen";

       Cursor mcursor = db.rawQuery(count, null);

       mcursor.moveToFirst();

       return mcursor.getInt(0);
   }
}
