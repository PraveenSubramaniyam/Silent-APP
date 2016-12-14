package com.example.praveensubramaniyam.silentapp;

/**
 * Created by PraveenSubramaniyam on 12/3/2016.
 */

public final class ProfileTable {
    private static final String TEXT_TYPE = " TEXT ";
    private static final String INTEGER_TYPE = " INTEGER ";
    private static final String COMMA_SEP = ",";

    public static final String TABLE_NAME = "PROFILE";
    public static final String C_PID = "ProfileId";
    public static final String C_PNAME = "ProfileName";
    public static final String C_ISVIBERATE = "IsViberate";
    public static final String C_ISSILENT = "IsSilent";
    public static final String C_ISWIFI = "IsWifi";
    public static final String C_ISBLUETOOTH = "IsBluetooth";
    public static final String C_Cordinates = "cordinates";
    public static final String C_StartTime = "startTime";
    public static final String C_EndTime = "endTime";
    public static final String C_REPEAT = "Repeat";

    public static final String CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    C_PID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    C_PNAME + TEXT_TYPE + COMMA_SEP +
                    C_ISSILENT + INTEGER_TYPE + COMMA_SEP+
                    C_ISVIBERATE + INTEGER_TYPE + COMMA_SEP+
                    C_ISBLUETOOTH + INTEGER_TYPE + COMMA_SEP +
                    C_Cordinates + TEXT_TYPE + "DEFAULT NULL "+COMMA_SEP +
                    C_StartTime + TEXT_TYPE + COMMA_SEP +
                    C_EndTime + TEXT_TYPE + COMMA_SEP+
                    C_REPEAT + INTEGER_TYPE + COMMA_SEP+
                    C_ISWIFI + INTEGER_TYPE + " )";
}
