package com.kmd.uog.logbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kmd.uog.logbook.database.Contact;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="ContactDatabase.db";
    private static final String TABLE_CONTACT ="tblContact";

    public static final String CONTACT_ID = "id";
    public static final String IMAGE_FILE_PATH= "imageFilePath";
    public static final String NAME = "name";
    public static final String DATE = "date";
    public static final String EMAIL = "email";

    private SQLiteDatabase database;

    private static final String CREATE_CONTACT_TABLE = String.format(
            "CREATE TABLE IF NOT EXISTS %s (" +
                    " %s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s TEXT," +
                    " %s TEXT)" // Add imageFilePath column definition
            , TABLE_CONTACT, CONTACT_ID, NAME, DATE, EMAIL, IMAGE_FILE_PATH);


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        database =getWritableDatabase();
        if(database !=null) database.execSQL( "PRAGMA encoding ='UTF-8'" );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long save(String name, String date, String email, String imageFilePath){
        long result =0;
        ContentValues rowValues =new ContentValues();
        rowValues.put(IMAGE_FILE_PATH, imageFilePath);
        rowValues.put(NAME, name);
        rowValues.put(DATE, date);
        rowValues.put(EMAIL, email);
        result = database.insertOrThrow(TABLE_CONTACT, null, rowValues);
        return result;
    }

    public List<Contact> search(String keyword) {
        Cursor cursor = null;
        String query = "SELECT * FROM " + TABLE_CONTACT +
                " WHERE " + NAME + " LIKE '%" + keyword + "%'";

        List<Contact> results = new ArrayList<>();
        cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            String email = cursor.getString(3);
            String imageFilePath = cursor.getString(4); // Retrieve the image file path
            cursor.moveToNext();

            Contact contact = new Contact(id, name, date, email);
            contact.setImageFilePath(imageFilePath); // Set the image file path
            results.add(contact);
        }
        cursor.close();
        return results;
    }


    public long delete(int id)
    {
        long result=0;
        String where= "id=?";
        String values[]= {String.valueOf(id)};
        result= database.delete(TABLE_CONTACT, where, values);
        return result;
    }

    public long update(int id, String name, String date, String email, String imageFilePath) {
        long result = 0;
        ContentValues rowValues = new ContentValues();
        rowValues.put(NAME, name);
        rowValues.put(DATE, date);
        rowValues.put(EMAIL, email);
        rowValues.put(IMAGE_FILE_PATH, imageFilePath); // Update the image file path
        String where = "id=?";
        String values[] = {id + ""};
        result = database.update(TABLE_CONTACT, rowValues, where, values);
        return result;
    }
}
