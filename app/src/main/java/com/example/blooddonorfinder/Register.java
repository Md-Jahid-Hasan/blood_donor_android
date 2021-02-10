package com.example.blooddonorfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class Register extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    RegisterDatabase  registerDatabase;

    EditText nameEditText, emailEditText, phoneEditText, presentAdEditText,
            permanentAdEditText, password1EditText, password2EditText, birthDate, areaEditText;
    Spinner bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerDatabase = new RegisterDatabase(this);
        SQLiteDatabase db = registerDatabase.getWritableDatabase();
        bloodGroup = findViewById(R.id.bloodGroupID);
        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this,
                R.array.bloodType, android.R.layout.simple_spinner_item);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroup.setAdapter(myAdapter);

        nameEditText = findViewById(R.id.nameField);
        emailEditText = findViewById(R.id.registerEmailAddress);
        phoneEditText = findViewById(R.id.editTextPhone);
        presentAdEditText = findViewById(R.id.editTextPresentAddress);
        permanentAdEditText = findViewById(R.id.editTextPermanentAddress);
        password1EditText = findViewById(R.id.editTextPassword);
        password2EditText = findViewById(R.id.editTextPassword2);
        birthDate = findViewById(R.id.dobEditText);
        areaEditText = findViewById(R.id.editTextArea);
    }

    public void doRegister(View view) {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String presentAddress = presentAdEditText.getText().toString();
        String permanentAddress = permanentAdEditText.getText().toString();
        String pass1 = password1EditText.getText().toString();
        String pass2 = password2EditText.getText().toString();
        String bloodGP =  bloodGroup.getSelectedItem().toString();
        String date = birthDate.getText().toString();
        String area = areaEditText.getText().toString();

        if (pass1.equals(pass2)){
            long rowNumber = registerDatabase.registerUser(name, email, phone, bloodGP, presentAddress,
                    permanentAddress, pass1, date, area, 0);
            if (rowNumber>0){
                Toast.makeText(this, "User successfully created",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "User create failed.Try again.",Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this, "Password Don't Match", Toast.LENGTH_LONG).show();
        }

    }

    public void showDatePickerOption(View view) {
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
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        birthDate.setText(date);
    }
}