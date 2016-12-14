package com.example.praveensubramaniyam.silentapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class ProfileAddScreen extends AppCompatActivity {

    Dialog dialog;
    View dialogView;
    private DataBaseHelper db;
    private static final String TAG = "SilentApp";
    private String coordinates =  null;
    private TextView addressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        addressView = (TextView) findViewById(R.id.location);
        if(addressView == null)
            System.out.println("Address view null");
        else
            System.out.println("Address view not null");
    }

    protected void insert_profile (View view)
    {
        boolean isWifiEnabled = ((CheckBox) findViewById(R.id.isWifiEnabled)).isChecked();
        boolean isSilentEnabled = ((CheckBox) findViewById(R.id.isSilentEnabled)).isChecked();
        boolean isBluetoothEnabled = ((CheckBox) findViewById(R.id.isBluetoothEnabled)).isChecked();
        boolean isViberateEnabled = ((CheckBox) findViewById(R.id.isViberateEnabled)).isChecked();
        String profileName = ((TextView)findViewById(R.id.profileName)).getText().toString();
        String startTime = ((TextView)findViewById(R.id.starttime)).getText().toString();
        String endTime = ((TextView)findViewById(R.id.endtime)).getText().toString();

        db = new DataBaseHelper(getApplicationContext());
        Log.i(TAG,isWifiEnabled+":"+isSilentEnabled+":"+isBluetoothEnabled+":"+isViberateEnabled+":"+profileName);

        ContentValues values = new ContentValues();
        values.put(ProfileTable.C_ISBLUETOOTH, isBluetoothEnabled);
        values.put(ProfileTable.C_ISSILENT, isSilentEnabled);
        values.put(ProfileTable.C_ISVIBERATE,isViberateEnabled);
        values.put(ProfileTable.C_ISWIFI,isWifiEnabled);
        values.put(ProfileTable.C_StartTime, startTime);
        values.put(ProfileTable.C_EndTime,endTime);
        values.put(ProfileTable.C_PNAME,profileName);
        if(this.coordinates != null)
            values.put(ProfileTable.C_Cordinates,this.coordinates);
        db.insertProfileTable(values);

        Intent intent= new Intent(ProfileAddScreen.this,ProfileListActivity.class);
        startActivity(intent);
        finish();

    }

    protected void addAddress(View view)
    {
        Intent i = new Intent(this, MapsActivity.class);
        startActivityForResult(i, 1);
    }

    protected void addStartTime (View view)
    {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        final TextView start_time = (TextView) findViewById(R.id.starttime);

        TimePickerDialog timePickerDialog = new TimePickerDialog(ProfileAddScreen.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        start_time.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }


    protected void addEndTime (View view)
    {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        final TextView end_time = (TextView) findViewById(R.id.endtime);

        TimePickerDialog timePickerDialog = new TimePickerDialog(ProfileAddScreen.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        end_time.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    protected void chooseDayOfWeek (View view)
    {
        LayoutInflater inflater = this.getLayoutInflater();
        dialog = new Dialog(ProfileAddScreen.this);
        dialogView=inflater.inflate(R.layout.daysofweek, null) ;
        dialog.setContentView(dialogView);
        dialog.setTitle("Title...");
        dialog.show();
    }

    protected void selectDays (View view)
    {
        boolean sunday = ((CheckBox) dialogView.findViewById(R.id.sunday)).isChecked();
        boolean monday = ((CheckBox) dialogView.findViewById(R.id.monday)).isChecked();
        boolean tuesday = ((CheckBox) dialogView.findViewById(R.id.tuesday)).isChecked();
        boolean wednesday = ((CheckBox) dialogView.findViewById(R.id.wednesday)).isChecked();
        boolean thursday = ((CheckBox) dialogView.findViewById(R.id.thursday)).isChecked();
        boolean friday = ((CheckBox) dialogView.findViewById(R.id.friday)).isChecked();
        boolean saturday = ((CheckBox) dialogView.findViewById(R.id.saturday)).isChecked();

        String daysOfWeek = "";

        if(sunday)
            daysOfWeek += "SUN ";

        if(monday)
            daysOfWeek += "MON ";

        if(tuesday)
            daysOfWeek += "TUE ";

        if(wednesday)
            daysOfWeek += "WED ";

        if(thursday)
            daysOfWeek += "THURS ";

        if(friday)
            daysOfWeek += "FRI ";

        if(saturday)
            daysOfWeek += "SAT ";

        ((TextView)findViewById(R.id.chooseDayOfWeek)).setText(daysOfWeek);
        dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("Activity result called");
        if (requestCode == 1) {
            System.out.println("resquest code 1 "+Activity.RESULT_OK+":"+resultCode);
            if(resultCode == Activity.RESULT_OK){
                System.out.println("result ok");
                ArrayList <String> result=data.getStringArrayListExtra("result");
                for (String address: result)
                {
                    String[] separated = address.split("\n");
                    System.out.println("Address: "+ separated[0]);
                    addressView.setText(separated[0]);
                    this.coordinates = separated[1];

                    System.out.println("Coordinates: "+ separated[1]);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(ProfileAddScreen.this,ProfileListActivity.class);
        startActivity(intent);
        finish();

    }
}
