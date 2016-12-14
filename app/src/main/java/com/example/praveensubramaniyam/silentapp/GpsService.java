
package com.example.praveensubramaniyam.silentapp;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GpsService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
	private static final String TAG = "SilentAPP";

    private double prevLatitude;
    private double prevLongitude;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private DataBaseHelper db;
    private SharedPreferences mSettings;
    private SilentAppSettings mSilentAppSettings;

    private void handleNewLocation(Location location) {
        ProfileTableValues profileTableValues;
        int i = 0;

        db = new DataBaseHelper(getApplicationContext());
        profileTableValues = db.getCoordinates();
        if(profileTableValues != null)
        {
            if(profileTableValues.getCoordinates() != null) {
                for (String temp : profileTableValues.getCoordinates()) {
                    String[] separated = temp.split(" ");
                    Location loc2 = new Location("");
                    loc2.setLatitude(Double.parseDouble(separated[0]));
                    loc2.setLongitude(Double.parseDouble(separated[1]));

                    float distanceInMeters = location.distanceTo(loc2);
                    if(distanceInMeters < 100)
                    {
                        Log.i(TAG, "A location reached Activating Profile");
                        sendNotification(profileTableValues.getProfileNames().get(i));
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (!mBluetoothAdapter.isEnabled()) {
                            mBluetoothAdapter.enable();
                        }
                    }
                    i++;
                }
            }
        }
    }

    public void sendNotification(String profileName)
    {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.silenticon)
                        .setContentTitle("Activating Profile")
                        .setContentText("Profile "+profileName +" is activated");

        Intent notificationIntent = new Intent(this, ProfileListActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            // Blank for a moment...
            Log.i(TAG,"Location Null");
           // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            //handleNewLocation(location);

        };

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this, "onLocation Changed" , Toast.LENGTH_LONG).show();
        Log.i(TAG, "On Location Changed Called");

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if(latitude != prevLatitude || longitude != prevLongitude )
        {
            prevLatitude = latitude;
            prevLongitude = longitude;

            if (mCallback != null) {
                mCallback.locationChanged();
            }
            Log.i(TAG,"Change in Location");
            //Toast.makeText(this, "Change in Location" , Toast.LENGTH_LONG).show();
            handleNewLocation(location);
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "[STEP SERVICE] onCreate");
        super.onCreate();

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mSilentAppSettings = new SilentAppSettings(mSettings);
        mSilentAppSettings.saveServiceRunning(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(20 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(5 * 1000); // 1 second, in milliseconds
        mGoogleApiClient.connect();
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
        Log.i(TAG, "[SERVICE] onStart");
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "[SERVICE] onDestroy");
        super.onDestroy();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        mSilentAppSettings.clearServiceRunning();
    }

    public class StepBinder extends Binder {
        GpsService getService() {
            return GpsService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "[SERVICE] onBind");
        return mBinder;
    }

    private final IBinder mBinder = new StepBinder();

    public interface ICallback {
        public void locationChanged();
    }
    
    private ICallback mCallback;
    public void registerCallback(ICallback cb) {
        mCallback = cb;
    }
    public void unregisterCallback() {
        mCallback = null;
    }
    public void resetValues() {
        return;
    }
}

