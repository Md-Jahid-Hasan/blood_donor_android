//package com.example.blooddonorfinder;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//
//public class BloodDonationDatabase extends SQLiteOpenHelper {
//    private static final String DATABASE_NAME = "blood_donor.db";
//    private static final String TABLE_NAME1 = "blood_donation";
//    private static final String USER_ID1 = "user_id";
//    private static final String DATE = "date";
//    private static final String ROW_NUMBER = "row_number";
//    private Context context;
//
//
//    public BloodDonationDatabase(@Nullable Context context) {
//        super(context, DATABASE_NAME, null, 2);
//        this.context = context;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String query = "CREATE TABLE " + TABLE_NAME + " ( " + ROW_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                USER_ID + " INTEGER NOT NULL, " + DATE + " TEXT NOT NULL, FOREIGN KEY (user_id) " +
//                "REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE);";
//
//        try {
//            db.execSQL(query);
//            Toast.makeText(context, "Blood Donation Table create", Toast.LENGTH_LONG).show();
//        }
//        catch (Exception e){
//            Toast.makeText(context, "Blood Donation Table creation failed", Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
//        try {
//            db.execSQL(query);
//            Toast.makeText(context, "onUpgrade Work", Toast.LENGTH_LONG).show();
//            onCreate(db);
//        }catch (Exception e){
//            Toast.makeText(context, "Something wrong happend on onUpgrade", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public long addDate(String user_id, String date){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(USER_ID1, user_id);
//        contentValues.put(DATE, date);
//        long rowNumber = db.insert(TABLE_NAME1, null, contentValues);
//        return rowNumber;
//
//    }
//
//    public void getUserDonationDate(String user_id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "SELECT *  FROM " + TABLE_NAME  + " WHERE " + USER_ID + " = " + user_id + " ;";
//        Cursor cursor = db.rawQuery(query, null);
//    }
//
//
//}
