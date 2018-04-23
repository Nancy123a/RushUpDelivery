package me.zeroandone.technology.rushupdelivery.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveI {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "SaveI";

    public SaveI(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveII(int i){
        editor.putInt("i",i);
        editor.apply();
    }

    public int getII(){
        return pref.getInt("i",0);
    }
}
