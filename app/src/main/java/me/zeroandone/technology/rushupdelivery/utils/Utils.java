package me.zeroandone.technology.rushupdelivery.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.interfaces.RushUpDeliverySettings;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;

public class Utils {
    public static final int MY_SOCKET_TIMEOUT_MS = 5000;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final String DIRECTION_API = "https://maps.googleapis.com/maps/api/directions/json?origin=";

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

    public static void showDriverDialog(Context context, final DeliveryRequest deliveryRequest, final RushUpDeliverySettings rushUpDeliverySettings){
         if(rushUpDeliverySettings!=null && deliveryRequest!=null){
             final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
             dialog.setContentView(R.layout.notification);
             WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
             lp.copyFrom(dialog.getWindow().getAttributes());
             lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
             lp.gravity = Gravity.CENTER;
             dialog.getWindow().setAttributes(lp);
             dialog.show();

             TextView dropoff_address=(TextView) dialog.findViewById(R.id.dropoff_address);
             TextView pickup_address=(TextView) dialog.findViewById(R.id.pickup_address);
             TextView acceptdelivery=(TextView) dialog.findViewById(R.id.accept_delivery);
             TextView decline_delivery=(TextView)dialog.findViewById(R.id.decline_delivery);
             String Dropoff_Address=deliveryRequest.getDropoffName()+" \n "+deliveryRequest.getDropoffLocation().getName().replaceAll("[\\t\\n\\r]+", " ");
             String PickUp_Address=deliveryRequest.getPickupName()+" \n "+deliveryRequest.getPickupLocation().getName().replaceAll("[\\t\\n\\r]+", " ");
             dropoff_address.setText(Dropoff_Address);
             pickup_address.setText(PickUp_Address);
             acceptdelivery.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                 dialog.dismiss();
                 rushUpDeliverySettings.PlotPins(deliveryRequest);
                 rushUpDeliverySettings.SaveActiveDelivery(deliveryRequest);
                 // save state to assign
                 AppHelper.assignDeliveryToDriver(deliveryRequest);
                 }
             });

         }
    }

    public static String getUrl(Context context, String originLat, String originLon, String destinationLat, String destinationLon) {
        String API_KEY = context.getResources().getString(R.string.google_maps_key);
        return DIRECTION_API + originLat + "," + originLon + "&destination=" + destinationLat + "," + destinationLon + "&key=" + API_KEY;
    }

}
