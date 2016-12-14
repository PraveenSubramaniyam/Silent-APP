package com.example.praveensubramaniyam.silentapp;

import java.util.ArrayList;

/**
 * Created by PraveenSubramaniyam on 12/3/2016.
 */

public class ProfileTableValues {
    private ArrayList <String> profileNames;
    private ArrayList <String> startTime;
    private ArrayList <String> endTime;
    private ArrayList <Integer> repeatValues;
    private ArrayList <Boolean> isWifiVales;
    private ArrayList <Boolean> isViberateVales;
    private ArrayList <Boolean> isSilentVales;
    private ArrayList <Boolean> isBluetoothVales;
    private ArrayList <String> coordinates;

    public ArrayList<String> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<String> coordinates) {
        this.coordinates = coordinates;
    }

    public ProfileTableValues() {
        profileNames = null;
        startTime = null;
        endTime = null;
        repeatValues = null;
        isBluetoothVales = null;
        isSilentVales = null;
        isWifiVales = null;
        isViberateVales = null;
        coordinates = null;
    }


    public void addProfileNames(String profileName)
    {
        if (profileNames == null) {
            profileNames = new ArrayList<String>();
        }
        profileNames.add(profileName);
    }

    public void addCoordinates(String coordinate)
    {
        if (coordinates == null) {
            coordinates = new ArrayList<String>();
        }
        coordinates.add(coordinate);
    }

    public void addStartTime(String startT)
    {
        if (startTime == null) {
            startTime = new ArrayList<String>();
        }
        startTime.add(startT);
    }

    public void addEndTime(String endT)
    {
        if (endTime == null) {
            endTime = new ArrayList<String>();
        }
        endTime.add(endT);
    }

    public void addrepeatValues(Integer val)
    {
        if (repeatValues == null) {
            repeatValues = new ArrayList<Integer>();
        }
        repeatValues.add(val);
    }

    public void addisBluetoothVales(Boolean bluetooth)
    {
        if (isBluetoothVales == null) {
            isBluetoothVales = new ArrayList<Boolean>();
        }
        isBluetoothVales.add(bluetooth);
    }

    public void addisSilentVales(Boolean silent)
    {
        if (isSilentVales == null) {
            isSilentVales = new ArrayList<Boolean>();
        }
        isSilentVales.add(silent);
    }

    public void addisWifiVales(Boolean wifi)
    {
        if (isWifiVales == null) {
            isWifiVales = new ArrayList<Boolean>();
        }
        isWifiVales.add(wifi);
    }

    public void addisViberateVales(Boolean viberate)
    {
        if (isViberateVales == null) {
            isViberateVales = new ArrayList<Boolean>();
        }
        isViberateVales.add(viberate);
    }



    public ArrayList<Boolean> getIsSilentVales() {
        return isSilentVales;
    }

    public void setIsSilentVales(ArrayList<Boolean> isSilentVales) {
        this.isSilentVales = isSilentVales;
    }

    public ArrayList<Boolean> getIsViberateVales() {
        return isViberateVales;
    }

    public void setIsViberateVales(ArrayList<Boolean> isViberateVales) {
        this.isViberateVales = isViberateVales;
    }

    public ArrayList<Boolean> getIsWifiVales() {
        return isWifiVales;
    }

    public void setIsWifiVales(ArrayList<Boolean> isWifiVales) {
        this.isWifiVales = isWifiVales;
    }

    public ArrayList<Integer> getRepeatValues() {
        return repeatValues;
    }

    public void setRepeatValues(ArrayList<Integer> repeatValues) {
        this.repeatValues = repeatValues;
    }

    public ArrayList<String> getEndTime() {
        return endTime;
    }

    public void setEndTime(ArrayList<String> endTime) {
        this.endTime = endTime;
    }

    public ArrayList<String> getStartTime() {
        return startTime;
    }

    public void setStartTime(ArrayList<String> startTime) {
        this.startTime = startTime;
    }

    public ArrayList<String> getProfileNames() {
        return profileNames;
    }

    public void setProfileNames(ArrayList<String> profileNames) {
        this.profileNames = profileNames;
    }


    public ArrayList<Boolean> getIsBluetoothVales() {
        return isBluetoothVales;
    }

    public void setIsBluetoothVales(ArrayList<Boolean> isBluetoothVales) {
        this.isBluetoothVales = isBluetoothVales;
    }



}
