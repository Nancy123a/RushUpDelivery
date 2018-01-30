package me.zeroandone.technology.rushupdelivery.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class NotificationSound {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Notification_Sound";

    public NotificationSound(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void saveNotificationsound(boolean isOn){
        editor.putBoolean("sound",isOn);
        editor.apply();
    }

    public boolean getNotificationsound(){
        return pref.getBoolean("sound",false);
    }


}
