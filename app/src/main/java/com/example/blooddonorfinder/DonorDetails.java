package com.example.blooddonorfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DonorDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {
    RegisterDatabase registerDatabase;
    MainActivity mainActivity;
    CheckAuthentication checkAuthentication;

    String donorUserID = null;
    String date = null;

    ListView donationDateList;
    Button saveButton, addDonationDate;
    EditText nameField, phoneField, presentAddressField, permanentAddressField, donationDate, area;
    TextView birthDate, availableStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);

        registerDatabase = new RegisterDatabase(this);
        mainActivity = new MainActivity();
        checkAuthentication = new CheckAuthentication();

        saveButton =  findViewById(R.id.saveButton);
        nameField  = findViewById(R.id.nameEditText);
        phoneField = findViewById(R.id.editTextPhone);
        presentAddressField = findViewById(R.id.editTextAddress);
        permanentAddressField = findViewById(R.id.editTextPermanentAddress);
        donationDate = findViewById(R.id.editTextDonationDate);
        addDonationDate = findViewById(R.id.addLastDonationDate);
        addDonationDate.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        donationDateList = findViewById(R.id.totalDonationDateList);
        birthDate = findViewById(R.id.birthDateText);
        area = findViewById(R.id.editTextSpecificArea);
        availableStatus = findViewById(R.id.availableTextView);
        if(checkAuthentication.isLogin()) forLoginUser();
        getUserData();

    }

    public void forLoginUser(){
        saveButton.setVisibility(View.VISIBLE);
        addDonationDate.setVisibility(View.VISIBLE);
        phoneField.setFocusableInTouchMode(true);
        presentAddressField.setFocusableInTouchMode(true);
        permanentAddressField.setFocusableInTouchMode(true);
        nameField.setFocusableInTouchMode(true);
        area.setFocusableInTouchMode(true);
    }

    public void forOtherData(){
        saveButton.setVisibility(View.INVISIBLE);
        addDonationDate.setVisibility(View.INVISIBLE);
        phoneField.setFocusableInTouchMode(false);
        presentAddressField.setFocusableInTouchMode(false);
        permanentAddressField.setFocusableInTouchMode(false);
        nameField.setFocusableInTouchMode(false);
        area.setFocusableInTouchMode(false);
    }

    public void getUserData(){
        Intent intent = getIntent();
        int user_id = intent.getIntExtra(Login.userID, -1);
        if (user_id == -1) {
            user_id = intent.getIntExtra(ShowDonor.USER_ID,0);
            if (checkAuthentication.isLogin() && (checkAuthentication.getUserID() != user_id)){
                forOtherData();
                Toast.makeText(this, "Disable the edit option", Toast.LENGTH_LONG).show();
            }


        }


        Cursor cursor = registerDatabase.getDonorDetails(user_id);
        cursor.moveToFirst();

        nameField.setText(cursor.getString(cursor.getColumnIndex("Name")));
        nameField.append(" ("+ cursor.getString(cursor.getColumnIndex("blood_group")) + ")");
        phoneField.setText(cursor.getString(cursor.getColumnIndex("phone")));
        presentAddressField.setText(cursor.getString(cursor.getColumnIndex("present_address")));
        permanentAddressField.setText(cursor.getString(cursor.getColumnIndex("permanent_address")));
        birthDate.append(cursor.getString(cursor.getColumnIndex("dob")));
        area.setText(cursor.getString(cursor.getColumnIndex("area")));
        int status = cursor.getInt(cursor.getColumnIndex("is_available"));

        if (status == 0){
            availableStatus.setText(R.string.available_status);
            availableStatus.setTextColor(Color.RED);
        }
        else{
            availableStatus.setText(R.string.notavailable_Status);
            availableStatus.setTextColor(Color.GREEN);
        }

        donorUserID = cursor.getString(cursor.getColumnIndex("user_id"));

        refreshDate();

    }


    public void setNewDonationDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year + "-" +  (month+1) + "-" +dayOfMonth;
        donationDate.setText(date);
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        long rowNumber = registerDatabase.addDate(donorUserID, date);
        if (rowNumber>0)
            Toast.makeText(this, "Date added", Toast.LENGTH_LONG).show();
        else Toast.makeText(this, "Date added failed", Toast.LENGTH_LONG).show();
        refreshDate();
        checkAvailabilityDonor();

    }


    public void refreshDate(){
        int i =  0;
        Cursor date_cursor = registerDatabase.getDate(donorUserID);
        if (date_cursor.getCount() != 0){
            date_cursor.moveToFirst();

            String items[] = new String[date_cursor.getCount()];
            while (!date_cursor.isAfterLast()){
                if (date_cursor.getString(date_cursor.getColumnIndex("date"))!=null){
                    items[i] = date_cursor.getString(date_cursor.getColumnIndex("date"));
                    i = i+1;
                }
                date_cursor.moveToNext();
            }
            date_cursor.moveToLast();
            date = date_cursor.getString(date_cursor.getColumnIndex("date"));
            donationDate.setText(date_cursor.getString(date_cursor.getColumnIndex("date")));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, items);
            donationDateList.setAdapter(adapter);

        }

    }

    public void checkAvailabilityDonor(){
        try {
            Date date3 = new Date();
            Date date2;
            Date date1;

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
            date2 = dates.parse(date);
            String d = dates.format(date3);
            date1 = dates.parse(d);
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            String dayDifference = Long.toString(differenceDates);
            int day = Integer.parseInt(dayDifference);
//                Toast.makeText(this, dayDifference, Toast.LENGTH_LONG).show();
//                System.out.print(date1 + " "  +  date2);

            if (day > 120){
                registerDatabase.makeAvailableDonor(donorUserID);
                availableStatus.setText(R.string.available_status);
                availableStatus.setTextColor(Color.GREEN);
            }
            else{
                registerDatabase.makeUnavailableDonor(donorUserID);
                availableStatus.setText(R.string.notavailable_Status);
                availableStatus.setTextColor(Color.RED);
            }

        }catch (Exception e){}

    }

    public void saveUserDate(View view) {
        String name = nameField.getText().toString();
        String phone = phoneField.getText().toString();
        String presentAddress = presentAddressField.getText().toString();
        String permanentAddress = permanentAddressField.getText().toString();
        String specificArea = area.getText().toString();
        registerDatabase.updateUserData(name, presentAddress, permanentAddress,
                specificArea, phone, Integer.parseInt(donorUserID));

    }
}