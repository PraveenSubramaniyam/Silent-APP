package com.example.praveensubramaniyam.silentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileScreenDisplay extends AppCompatActivity {

    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ProfileTableValues profileTableValues;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen_display);
        String s = getIntent().getStringExtra("ProfileName");
        db = new DataBaseHelper(getApplicationContext());
        profileTableValues = db.getProfileTableValues(ProfileTable.C_PNAME + " = '" +s +"'");

        if(profileTableValues.getProfileNames() != null) {
            for (String temp : profileTableValues.getProfileNames()) {
                ((TextView)findViewById(R.id.profileNameDisp)).setText(temp);
            }

            for (String temp : profileTableValues.getCoordinates()) {
                ((TextView)findViewById(R.id.locationDisp)).setText(temp);
            }


        }
    }

    public void onBackPressed() {
        super.onBackPressed();

        Intent intent= new Intent(ProfileScreenDisplay.this,ProfileListActivity.class);
        startActivity(intent);
        finish();

    }
}
