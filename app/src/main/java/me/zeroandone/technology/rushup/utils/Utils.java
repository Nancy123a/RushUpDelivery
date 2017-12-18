package me.zeroandone.technology.rushup.utils;


import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class Utils {
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

}
