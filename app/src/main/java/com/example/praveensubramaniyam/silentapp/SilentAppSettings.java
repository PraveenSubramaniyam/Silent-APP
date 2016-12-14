
package com.example.praveensubramaniyam.silentapp;

import android.content.SharedPreferences;

public class SilentAppSettings {

    SharedPreferences mSettings;

    public SilentAppSettings(SharedPreferences settings) {
        mSettings = settings;
    }

    /**
     *
     * @param running
     */
    public void saveServiceRunning(boolean running) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", running);
        editor.commit();
    }

    public void clearServiceRunning() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean("service_running", false);
        editor.commit();
    }

    public boolean isServiceRunning() {
        return mSettings.getBoolean("service_running", false);
    }
}
