package me.zeroandone.technology.rushupdelivery.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FirstTimeOpen {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "My_Location";

    public FirstTimeOpen(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void isSignin(boolean isLogin){
        editor.putBoolean("login",isLogin);
        editor.apply();
    }

    public boolean getLogin(){
        return pref.getBoolean("login",false);
    }

}
