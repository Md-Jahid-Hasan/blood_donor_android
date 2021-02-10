package com.example.blooddonorfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ShowDonor extends AppCompatActivity {
    //String items[] = new String [] {"jahid", "mahin", "fdasf", "sdfg"};
    ListView donorList;

    RegisterDatabase registerDatabase;

    public static final String USER_ID = "Donor user id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_donor);

        registerDatabase = new RegisterDatabase(this);

        donorList = findViewById(R.id.availableDonorList);
        showDonor();


    }

    public void showDonor(){
        Intent intent = getIntent();
        String bdGroup = intent.getStringExtra(MainActivity.BLOOD);
        String district = intent.getStringExtra(MainActivity.DIST);
        String specificArea = intent.getStringExtra(MainActivity.AREA);

        Cursor donor = registerDatabase.searchDonor(bdGroup, district, null, specificArea);
        int i =0;
        if (donor.getCount() != 0){
            donor.moveToFirst();

            String items[] = new String[donor.getCount()];
            String user_id[] = new String[donor.getCount()];
            while (!donor.isAfterLast()){
                if (donor.getString(donor.getColumnIndex("Name"))!=null){
                    items[i] = donor.getString(donor.getColumnIndex("Name"));
                    user_id[i] = donor.getString(donor.getColumnIndex("user_id"));
                    i = i+1;
                }
                donor.moveToNext();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, items);
            donorList.setAdapter(adapter);

            donorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent detailsIntent = new Intent(ShowDonor.this, DonorDetails.class);
                    detailsIntent.putExtra(USER_ID, Integer.parseInt(user_id[position]));
                    startActivity(detailsIntent);
                }
            });

        }

    }

}