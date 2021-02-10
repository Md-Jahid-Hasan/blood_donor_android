package com.example.blooddonorfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    Spinner bloodGroup;
    EditText organisationEditText, districtEditText, areaEditText;
    Button loginButton;

    public static final String BLOOD = "This is blood Group";
    public static final String DIST = "This is district";
    public static final String AREA = "This is specific are";

    RegisterDatabase registerDatabase;
    CheckAuthentication checkAuthentication = new CheckAuthentication();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerDatabase = new RegisterDatabase(this);

        bloodGroup = findViewById(R.id.bloodGroupID);
        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this,
                R.array.bloodType, android.R.layout.simple_spinner_item);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroup.setAdapter(myAdapter);

        organisationEditText = findViewById(R.id.organisationID);
        districtEditText = findViewById(R.id.districtNameID);
        areaEditText = findViewById(R.id.specificAreaID);
        loginButton = findViewById(R.id.loginLogout);

        if(checkAuthentication.isLogin())
            loginButton.setText(R.string.logout);
        else
            loginButton.setText(R.string.login);

    }

    public void searchDonor(View view) {
        String bdGroup = bloodGroup.getSelectedItem().toString();
        String district = districtEditText.getText().toString();
        String organisation = organisationEditText.getText().toString();
        String specificArea = areaEditText.getText().toString();



        Intent showIntent = new Intent(this, ShowDonor.class);
        showIntent.putExtra(BLOOD, bdGroup);
        showIntent.putExtra(DIST, district);
        showIntent.putExtra(AREA, specificArea);
        startActivity(showIntent);
    }

    public void loginButtonPressed(View view) {
        if(checkAuthentication.isLogin()){
            checkAuthentication.makeLogOut();
            loginButton.setText(R.string.login);
        }
        else {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }

    public void registeButtonPressed(View view) {
        checkAuthentication.makeLogOut();
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }
}