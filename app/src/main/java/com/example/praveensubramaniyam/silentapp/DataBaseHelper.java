package com.example.praveensubramaniyam.silentapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Praveen Subramaniyam on 9/27/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SilentAPP";

    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "Pedometer";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * onCreate: Called during the creation of the database
     * @param database
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i(TAG,"DBhealper Oncreate Called");
        database.execSQL(ProfileTable.CREATE_ENTRIES);
    }



    /**
     * onUpgrade: Method is called during an upgrade of the database
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(DataBaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        //database.execSQL("DROP TABLE IF EXISTS MyEmployees");
        onCreate(database);
    }

    /**
     * getDateTime function is used to return the current date in string format
     * @return
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Function is used to insert values into the ProfileTable
     * @param values
     * @return
     */
    public long insertProfileTable(ContentValues values)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // insert row
        long id = db.insert(ProfileTable.TABLE_NAME, null, values);
        db.close();
        return id;
    }


    /**
     * Function is used return the rows in the profile table which has the
     * @return
     */
    public ProfileTableValues getCoordinates()
    {
        Log.i(TAG,"Calling get corrdinates a row");
        ProfileTableValues profileTableValues = null;
        SQLiteDatabase myDB = this.getReadableDatabase();
        String[] projection = {
                ProfileTable.C_ISWIFI,
                ProfileTable.C_ISBLUETOOTH,
                ProfileTable.C_ISVIBERATE,
                ProfileTable.C_ISSILENT,
                ProfileTable.C_PNAME,
                ProfileTable.C_Cordinates
        };

        Cursor c = myDB.query(
                ProfileTable.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                ProfileTable.C_Cordinates + " IS NOT NULL",                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        int Column1 = c.getColumnIndex(ProfileTable.C_PNAME);
        int Column2 = c.getColumnIndex(ProfileTable.C_ISSILENT);
        int Column3 = c.getColumnIndex(ProfileTable.C_ISWIFI);
        int Column4 = c.getColumnIndex(ProfileTable.C_ISBLUETOOTH);
        int Column5 = c.getColumnIndex(ProfileTable.C_ISVIBERATE);
        int Column6 = c.getColumnIndex(ProfileTable.C_Cordinates);

        try {

            if (c != null && c.moveToFirst()) {
                // Loop through all Results
                profileTableValues = new ProfileTableValues();
                do {
                    Log.i(TAG,"Adding a row");
                    profileTableValues.addProfileNames(c.getString(Column1));
                    profileTableValues.addisSilentVales((c.getInt(Column2) != 0));
                    profileTableValues.addisWifiVales((c.getInt(Column3) != 0));
                    profileTableValues.addisBluetoothVales((c.getInt(Column4) != 0));
                    profileTableValues.addisViberateVales((c.getInt(Column5) != 0));
                    profileTableValues.addCoordinates(c.getString(Column6));
                } while (c.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.e("exception",e.toString());
        }
        finally {
            if(myDB != null)
                myDB.close();
        }
        return profileTableValues;
    }


    /**
     * Function to update the ProfileTable when the profile id is given
     * @param values
     * @param id
     */
    public void updateProfileTable(ContentValues values,long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(ProfileTable.TABLE_NAME, values, ProfileTable.C_PID + " = " + id, null);
        db.close();
    }


    /**
     * Function to return all the rows of the ProfileTable
     * @param whereClause
     * @return
     */
    public ProfileTableValues getProfileTableValues(String whereClause)
    {
        ProfileTableValues profileTableValues = null;
        SQLiteDatabase myDB = this.getReadableDatabase();
        String[] projection = {
                ProfileTable.C_ISWIFI,
                ProfileTable.C_ISBLUETOOTH,
                ProfileTable.C_ISVIBERATE,
                ProfileTable.C_ISSILENT,
                ProfileTable.C_PNAME,
                ProfileTable.C_StartTime,
                ProfileTable.C_EndTime,
                ProfileTable.C_Cordinates,
                ProfileTable.C_PID
        };

        Cursor c = myDB.query(
                ProfileTable.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        int Column1 = c.getColumnIndex(ProfileTable.C_PNAME);
        int Column2 = c.getColumnIndex(ProfileTable.C_ISSILENT);
        int Column3 = c.getColumnIndex(ProfileTable.C_ISWIFI);
        int Column4 = c.getColumnIndex(ProfileTable.C_ISBLUETOOTH);
        int Column5 = c.getColumnIndex(ProfileTable.C_ISVIBERATE);
        int Column6 = c.getColumnIndex(ProfileTable.C_StartTime);
        int Column7 = c.getColumnIndex(ProfileTable.C_EndTime);
        int Column8 = c.getColumnIndex(ProfileTable.C_PID);

        try {
            c.moveToFirst();
            if (c != null) {
                // Loop through all Results
                profileTableValues = new ProfileTableValues();
                do {
                    profileTableValues.addProfileNames(c.getString(Column1));
                    profileTableValues.addisSilentVales((c.getInt(Column2) != 0));
                    profileTableValues.addisWifiVales((c.getInt(Column3) != 0));
                    profileTableValues.addisBluetoothVales((c.getInt(Column4) != 0));
                    profileTableValues.addisViberateVales((c.getInt(Column5) != 0));
                    profileTableValues.addStartTime(c.getString(Column6));
                    profileTableValues.addEndTime(c.getString(Column7));
                    profileTableValues.addProfileId(c.getInt(Column8));
                } while (c.moveToNext());
            }
        }
        catch (Exception e)
        {
            Log.e("exception",e.toString());
        }
        finally {
            if(myDB != null)
                myDB.close();
        }
        return profileTableValues;
    }

    public boolean deleteProfile(int id)
    {
        Boolean retVal;
        SQLiteDatabase myDB = this.getWritableDatabase();
        retVal = myDB.delete(ProfileTable.TABLE_NAME, ProfileTable.C_PID+ "=" + String.valueOf(id), null) >0 ;
        myDB.close();
        return retVal;
    }
}