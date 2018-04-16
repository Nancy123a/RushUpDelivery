package me.zeroandone.technology.rushupdelivery.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class isPickUp {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Driver_Pickup_Distance";

    public isPickUp(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveisPickup(boolean isPickup){
        editor.putBoolean("ispickup",isPickup);
        editor.apply();
    }

    public boolean getisPickUp(){
        return pref.getBoolean("ispickup",true);
    }

    public void clearSharedPreference(){
        if(pref!=null) {
            pref.edit().remove("ispickup").apply();
        }
    }
}
