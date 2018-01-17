package me.zeroandone.technology.rushupdelivery.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class DriverStatusSharedPreference {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Driver_Pickup_Distance";

    public DriverStatusSharedPreference(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveStatus(String status){
        editor.putString("driver",status);
        editor.apply();
    }

    public String getStatus(){
        return pref.getString("driver","");
    }

}
