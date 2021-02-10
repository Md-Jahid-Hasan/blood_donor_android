package com.example.blooddonorfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class RegisterDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "blood_donor.db";
    private static final String TABLE_NAME = "user";
    private static final String USER_ID = "user_id";
    private static final String NAME = "Name";
    private static final String EMAIL = "Email";
    private static final String PHONE_NUMBER = "phone";
    private static final String PRESENT_ADDRESS = "present_address";
    private static final String PERMANENT_ADDRESS = "permanent_address";
    private static final String PASSWORD = "password";
    private static final String BLOOD_GROUP = "blood_group";
    private static final String USER_ID1 = "user_id";
    private static final String DATE = "date";
    private static final String ROW_NUMBER = "row_number";
    private static final String TABLE_NAME1 = "blood_donation";
    private static final String SPECIFIC_AREA = "area";
    private static final String DATE_OF_BIRTH = "dob";
    private static final String AVAILABLE = "is_available";

    private Context context;


    public RegisterDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 4);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+ TABLE_NAME + "( "+ USER_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ NAME + " " +
                "TEXT NOT NULL, "+ EMAIL + " TEXT NOT NULL UNIQUE, "+ PHONE_NUMBER + " TEXT NOT NULL " +
                "UNIQUE, "  + PRESENT_ADDRESS + " TEXT NOT NULL, " + PERMANENT_ADDRESS + " TEXT NOT NULL, " +
                PASSWORD  + " TEXT NOT NULL, "+ BLOOD_GROUP +" TEXT NOT NULL, "+ DATE_OF_BIRTH +" TEXT NOT NULL, " +
                SPECIFIC_AREA + " TEXT NOT NULL, "+ AVAILABLE +" INTEGER NOT NULL);";

        String query1 = "CREATE TABLE " + TABLE_NAME1 + " ( " + ROW_NUMBER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_ID1 + " INTEGER NOT NULL, " + DATE + " TEXT NOT NULL, FOREIGN KEY (user_id) " +
                "REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE);";

        try {
            db.execSQL(query);
            db.execSQL(query1);
            Toast.makeText(context, "Table successfully created",  Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(context, "Something Wrong Happend",  Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String query1 = "DROP TABLE IF EXISTS " + TABLE_NAME1;
        try {
            db.execSQL(query);
            db.execSQL(query1);
            Toast.makeText(context, "onUpgrade Work", Toast.LENGTH_LONG).show();
            onCreate(db);
        }catch (Exception e){
            Toast.makeText(context, "Something wrong happend on onUpgrade", Toast.LENGTH_LONG).show();
        }

    }

    public long registerUser(String name, String email, String phone, String bloodGroup,
                             String presentAddress, String permanentAddress, String password,
                             String dob, String area, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(EMAIL, email);
        contentValues.put(PHONE_NUMBER, phone);
        contentValues.put(PRESENT_ADDRESS, presentAddress);
        contentValues.put(PERMANENT_ADDRESS, permanentAddress);
        contentValues.put(PASSWORD, password);
        contentValues.put(BLOOD_GROUP, bloodGroup);
        contentValues.put(DATE_OF_BIRTH, dob);
        contentValues.put(SPECIFIC_AREA, area);
        contentValues.put(AVAILABLE, status);

        long rowNumber = db.insert(TABLE_NAME, null, contentValues);
        return rowNumber;
    }

    public Cursor checkLogin(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE email = '"+ email+ "' AND password = '" +
                password + "';";
        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.getCount() > 0) return true;
//        else return false;
        return cursor;
    }

    public boolean checkUser(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE email = '"+ email+ "';";
        cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    public Cursor getLoginUserData(String email,  String password){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE email = '"+ email+ "' AND password = '" +
                password + "';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

    }
    public long addDate(String user_id, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID1, user_id);
        contentValues.put(DATE, date);
        long rowNumber = db.insert(TABLE_NAME1, null, contentValues);
        return rowNumber;

    }

    public Cursor getDate(String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME1 + " WHERE user_id = "+ user_id + ";";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void makeAvailableDonor(String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+ TABLE_NAME + " SET " + AVAILABLE + " = 1 WHERE "+ USER_ID + " = "+
                user_id + ";";
        db.execSQL(query);
    }

    public void makeUnavailableDonor(String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+ TABLE_NAME + " SET " + AVAILABLE + " = 1 WHERE "+ USER_ID + " = "+
                user_id + ";";

        db.execSQL(query);
    }

    public Cursor searchDonor(String bdGroup, String district, String organisation,
                            String specificArea){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + BLOOD_GROUP + " = '"+ bdGroup +
                "' AND "+ PRESENT_ADDRESS + " = '" + district + "' AND " + SPECIFIC_AREA +
                " LIKE '%" + specificArea + "%' AND "+ AVAILABLE + "= 1;";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor getDonorDetails(int user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = "+user_id+";";
        Cursor cursor = db.rawQuery(query, null);
        Toast.makeText(context, cursor.getCount()+" ", Toast.LENGTH_LONG).show();
        return cursor;
    }

    public void updateUserData(String name, String presentAddress, String permanentAddress,
                               String area, String phone, int user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + NAME + " = '" + name + "', " +
                PRESENT_ADDRESS + " = '" + presentAddress + "', " + PERMANENT_ADDRESS + " = '"+
                permanentAddress + "', " + PHONE_NUMBER + " = '" + phone + "', " + SPECIFIC_AREA +
                " = '" + area + "' WHERE " + USER_ID + " = " + user_id + ";";
        db.execSQL(query);
        Toast.makeText(context, "Your Data Update Successfully", Toast.LENGTH_LONG).show();
    }
}
