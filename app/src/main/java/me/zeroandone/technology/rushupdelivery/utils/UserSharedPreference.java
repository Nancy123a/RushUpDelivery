package me.zeroandone.technology.rushupdelivery.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class UserSharedPreference {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Driver_Pickup_Distance";

    public UserSharedPreference(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveUserName(String username){
        editor.putString("username",username);
        editor.apply();
    }

    public String getUserName(){
        return pref.getString("username","");
    }
}
