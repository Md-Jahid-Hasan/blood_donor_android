package com.example.blooddonorfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Trace;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    RegisterDatabase registerDatabase;
    CheckAuthentication checkAuthentication;

    public static final String userEmail = "Login user email";
    public static final String userID = "Login user id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.editTextEmailAddress);
        passwordEditText = findViewById(R.id.editPassword);
        registerDatabase = new RegisterDatabase(this);
        SQLiteDatabase db = registerDatabase.getWritableDatabase();
        checkAuthentication = new CheckAuthentication();


    }

    public void registeButtonPressed(View view) {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    public void homeButtonPressed(View view) {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

    public void makeLoginUser(View view) {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean userStatus = registerDatabase.checkUser(email);
        if (userStatus){
            Cursor cursor = registerDatabase.checkLogin(email, password);
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                Toast.makeText(this, "Successfully Login",  Toast.LENGTH_LONG).show();
                checkAuthentication.makeLogin();

                int user = cursor.getInt(cursor.getColumnIndex("user_id"));
                checkAuthentication.setUserID(user);
                Intent intent = new Intent(Login.this, DonorDetails.class);
                intent.putExtra(userID,user);
                //intent.putExtra(userEmail,email);
                startActivity(intent);
            }
            else Toast.makeText(this, "Email and Password dosen't match",  Toast.LENGTH_LONG).show();
        }

        else Toast.makeText(this, "User dosen't exist",  Toast.LENGTH_LONG).show();

    }
}