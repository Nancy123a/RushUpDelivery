package me.zeroandone.technology.rushupdelivery.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    public static void setFontTypeOpenSansSemibold(Context context, TextView text) {
        if (context != null && text != null) {
            Typeface type = Typeface.createFromAsset(context.getAssets(), "OpenSans-Semibold.ttf");
            text.setTypeface(type);
        }
    }

    public static void setFontTypeOpenSansLight(Context context, TextView text) {
        if (context != null && text != null) {
            Typeface type = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
            text.setTypeface(type);
        }
    }

    public static void setFontTypeOpenSans(Context context, TextView text) {
        if (context != null && text != null) {
            Typeface type = Typeface.createFromAsset(context.getAssets(), "OpenSans.ttf");
            text.setTypeface(type);
        }
    }


    public static void setFontTypeOpenSansBold(Context context, TextView text) {
        if (context != null && text != null) {
            Typeface type = Typeface.createFromAsset(context.getAssets(), "OpenSans-Semibold.ttf");
            text.setTypeface(type);
        }
    }

    public static boolean isPasswordValid(String password) {
        String expression = "((?=.*\\d)(?=.*[A-Z])(?=.*\\W).{8,30})";

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static void dismissDialog(final Dialog dialog) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(dialog!=null) {
                    dialog.dismiss();
                }
            }
        }, 3000);
    }

    /**
     * Method to verify google play services on the device
     * */
    public static boolean checkPlayServices(Context context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode,(Activity)context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

            }
            return false;
        }
        return true;
    }


}
