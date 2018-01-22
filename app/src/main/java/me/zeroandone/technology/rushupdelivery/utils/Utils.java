package me.zeroandone.technology.rushupdelivery.utils;


import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.interfaces.RushUpDeliverySettings;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryStatus;
import me.zeroandone.technology.rushupdelivery.objects.DriverStatus;

public class Utils {
    public static final int MY_SOCKET_TIMEOUT_MS = 5000;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final String DIRECTION_API = "https://maps.googleapis.com/maps/api/directions/json?origin=";

    public static Handler handlers;
    public static Runnable runnables;


    public Handler getHandler() {
        return handlers;
    }

    public static void setHandler(Handler handler) {
        handlers = handler;
    }

    public Runnable getRunnable() {
        return runnables;
    }

    public static void setRunnable(Runnable runnable) {
        runnables = runnable;
    }

    public static void cancelHandler(){
        if(handlers!=null && runnables!=null){
            Log.d("HeroJongi","Cancel handler");
            handlers.removeCallbacks(runnables);
        }
    }

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

    public static Dialog showDriverDialog(Context context, final DeliveryRequest deliveryRequest, final RushUpDeliverySettings rushUpDeliverySettings, final DriverStatusSharedPreference driverStatusSharedPreference){
        Dialog dialog=null;
         if(rushUpDeliverySettings!=null && deliveryRequest!=null && deliveryRequest.getPickupLocation()!=null && deliveryRequest.getPickupLocation().getName()!=null
                 && !deliveryRequest.getPickupLocation().getName().equalsIgnoreCase("") && !deliveryRequest.getDropoffLocation().getName().equalsIgnoreCase("")
                 && deliveryRequest.getPickupName()!=null && deliveryRequest.getDropoffName()!=null){

             dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
             dialog.setContentView(R.layout.notification);
             WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
             lp.copyFrom(dialog.getWindow().getAttributes());
             lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
             lp.gravity = Gravity.CENTER;
             dialog.getWindow().setAttributes(lp);
             dialog.show();
             final Dialog finalDialog = dialog;
             TextView dropoff_address=(TextView) dialog.findViewById(R.id.dropoff_address);
             TextView pickup_address=(TextView) dialog.findViewById(R.id.pickup_address);
             TextView acceptdelivery=(TextView) dialog.findViewById(R.id.accept_delivery);
             TextView decline_delivery=(TextView)dialog.findViewById(R.id.decline_delivery);
             final TextView time=(TextView) dialog.findViewById(R.id.time);
             String Dropoff_Address=deliveryRequest.getDropoffName()+" \n "+deliveryRequest.getDropoffLocation().getName().replaceAll("[\\t\\n\\r]+", " ");
             String PickUp_Address=deliveryRequest.getPickupName()+" \n "+deliveryRequest.getPickupLocation().getName().replaceAll("[\\t\\n\\r]+", " ");
             dropoff_address.setText(PickUp_Address);
             pickup_address.setText(Dropoff_Address);

             new CountDownTimer(60*1000, 1000) {

                 public void onTick(long millisUntilFinished) {
                     String ms=(millisUntilFinished / 1000)+" sec";
                     time.setText(ms);
                     //here you can have your logic to set text to edittext
                 }

                 public void onFinish() {
                 }

             }.start();

             acceptdelivery.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                 finalDialog.dismiss();
                 rushUpDeliverySettings.PlotPins(deliveryRequest);
                 rushUpDeliverySettings.SaveActiveDelivery(deliveryRequest);
                 rushUpDeliverySettings.showBottomMenu(deliveryRequest);
                 rushUpDeliverySettings. FillUpBottomMenu(deliveryRequest,true);
                 // save state to assign
                 AppHelper.assignDeliveryToDriver(deliveryRequest);
                 // change state of user to occupied
                 AppHelper.UpdateStatusofDriver(DriverStatus.occupied);

                 driverStatusSharedPreference.saveStatus("occupied");

                 }
             });

             decline_delivery.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     finalDialog.dismiss();
                     rushUpDeliverySettings.showOptions();
                 }
             });

         }
         return dialog;
    }

    public static void DeclareHandler(final Context context, final Dialog dialog, final Object object){
        final NotificationIDs notificationIDs=new NotificationIDs(context);
        Handler handler=new Handler();
        Utils.setHandler(handler);
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                Log.d("HeroJongi","onTime");
                if(object instanceof DeliveryRequest) {
                    if (dialog != null) {
                        Log.d("HeroJongi", "on Reciever Side");
                        dialog.dismiss();
                        RemoveNotification(context, "driver_request");
                    }
                }
            }
        };
        handler.postDelayed(runnable,60*1000);
        Utils.setRunnable(runnable);
    }


    public static void RemoveNotification(Context context,String pushtype){
        NotificationIDs notificationIDs=new NotificationIDs(context);
        List<Map<String,String>> map=notificationIDs.getValuesFromSharedPreference(pushtype);
        for(Map<String,String>map1:map){
            Log.d("HeroJongi","Val "+map1.get("value")+map1.get("key"));
        }
        if(map.size()>0) {
            cancelNotification(context, Integer.valueOf(map.get(0).get("value")));
            notificationIDs.DeleteFromNotificationIds(map.get(0).get("value"));
        }
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }

    public static String getUrl(Context context, String originLat, String originLon, String destinationLat, String destinationLon) {
        String API_KEY = context.getResources().getString(R.string.google_maps_key);
        return DIRECTION_API + originLat + "," + originLon + "&destination=" + destinationLat + "," + destinationLon + "&key=" + API_KEY;
    }

}
