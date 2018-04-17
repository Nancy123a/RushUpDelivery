package me.zeroandone.technology.rushupdelivery.utils;


import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.Registration;
import me.zeroandone.technology.rushupdelivery.interfaces.PickerInterface;
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
                if(dialog!=null && dialog.isShowing()) {
                    try {
                        dialog.dismiss();
                    }
                    catch (Exception exception){
                        Log.d("HeroJongi"," exception "+exception.getMessage());
                    }
                }
            }
        }, 3000);
    }

    /**
     * Method to verify google play services on the device
     * */
    public static boolean checkPlayServices(Context context) {
        if(context!=null) {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {

                }
                return false;
            }
        }
        return true;
    }

    public static Dialog showDriverDialog(final Context context, final DeliveryRequest deliveryRequest, final RushUpDeliverySettings rushUpDeliverySettings, final DriverStatusSharedPreference driverStatusSharedPreference){
        Dialog dialog=null;
        if(context!=null) {
            if (rushUpDeliverySettings != null && deliveryRequest != null && deliveryRequest.getPickupLocation() != null && deliveryRequest.getPickupLocation().getName() != null
                    && !deliveryRequest.getPickupLocation().getName().equalsIgnoreCase("") && !deliveryRequest.getDropoffLocation().getName().equalsIgnoreCase("")
                    && deliveryRequest.getPickupName() != null && deliveryRequest.getDropoffName() != null) {

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
                TextView dropoff_address = (TextView) dialog.findViewById(R.id.dropoff_address);
                TextView pickup_address = (TextView) dialog.findViewById(R.id.pickup_address);
                TextView acceptdelivery = (TextView) dialog.findViewById(R.id.accept_delivery);
                TextView decline_delivery = (TextView) dialog.findViewById(R.id.decline_delivery);
                final TextView time = (TextView) dialog.findViewById(R.id.time);
                String Dropoff_Address = deliveryRequest.getDropoffName() + " \n " + deliveryRequest.getDropoffLocation().getName().replaceAll("[\\t\\n\\r]+", " ");
                String PickUp_Address = deliveryRequest.getPickupName() + " \n " + deliveryRequest.getPickupLocation().getName().replaceAll("[\\t\\n\\r]+", " ");
                dropoff_address.setText(PickUp_Address);
                pickup_address.setText(Dropoff_Address);

                new CountDownTimer(60 * 1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        String ms = (millisUntilFinished / 1000) + " sec";
                        time.setText(ms);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                    }

                }.start();

                acceptdelivery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity=(Activity) context;
                        if(finalDialog.isShowing() &&  !activity.isFinishing()) {
                            try {
                                finalDialog.dismiss();
                                rushUpDeliverySettings.PlotPins(deliveryRequest);
                                rushUpDeliverySettings.SaveActiveDelivery(deliveryRequest);
                                rushUpDeliverySettings.showBottomMenu(deliveryRequest);
                                rushUpDeliverySettings.FillUpBottomMenu(deliveryRequest, true);
                                // save state to assign
                                AppHelper.assignDeliveryToDriver(deliveryRequest);
                                // change state of user to occupied
                                AppHelper.UpdateStatusofDriver(DriverStatus.occupied);

                                driverStatusSharedPreference.saveStatus("occupied");
                            }
                            catch (Exception e){
                                Log.d("HeroJongi"," exception "+e.getMessage());
                            }
                        }
                    }
                });

                decline_delivery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity=(Activity) context;
                        if(finalDialog.isShowing() &&  !activity.isFinishing()) {
                            try {
                                finalDialog.dismiss();
                                rushUpDeliverySettings.showOptions();
                            }
                            catch (Exception exception){
                                Log.d("HeroJongi"," exception "+exception.getMessage());
                            }
                        }
                    }
                });

            }
        }
         return dialog;
    }

    public static void DeclareHandler(final Context context, final Dialog dialog, final Object object){
        final NotificationIDs notificationIDs=new NotificationIDs(context);
        Handler handler=new Handler();
        if(context!=null) {
            Utils.setHandler(handler);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Log.d("HeroJongi", "onTime");
                    if (object instanceof DeliveryRequest) {
                        Activity activity=(Activity) context;
                        if(dialog!=null && dialog.isShowing() &&  !activity.isFinishing()) {
                            try {
                                Log.d("HeroJongi", "on Reciever Side");
                                dialog.dismiss();
                                RemoveNotification(context, "driver_request");
                            }
                            catch (Exception exception){
                                Log.d("HeroJongi"," exception "+exception.getMessage());
                            }
                        }
                    }
                }
            };
            handler.postDelayed(runnable, 60 * 1000);
            Utils.setRunnable(runnable);
        }
    }


    public static void RemoveNotification(Context context,String pushtype){
        if(context!=null) {
            NotificationIDs notificationIDs = new NotificationIDs(context);
            List<Map<String, String>> map = notificationIDs.getValuesFromSharedPreference(pushtype);
            for (Map<String, String> map1 : map) {
                Log.d("HeroJongi", "Val " + map1.get("value") + map1.get("key"));
            }
            if (map.size() > 0) {
                cancelNotification(context, Integer.valueOf(map.get(0).get("value")));
                notificationIDs.DeleteFromNotificationIds(map.get(0).get("value"));
            }
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

    public static void startImagePicker(final Context context, final PickerInterface pickerDialog) {
        if (pickerDialog != null && context!=null) {
            final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.image_picker_dialog);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            Button gallery = (Button) dialog.findViewById(R.id.gallery);
            Button camera = (Button) dialog.findViewById(R.id.cameradialog);
            ImageView close = (ImageView) dialog.findViewById(R.id.close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dialog.isShowing()) {
                        try {
                            //File file = new File(context.getCacheDir(), String.valueOf(System.currentTimeMillis()) + ".jpg");
                            ContentValues values = new ContentValues ();
                            values.put (MediaStore.Images.Media.IS_PRIVATE, 1);
                            values.put (MediaStore.Images.Media.TITLE, "RushUp Picture");
                            values.put (MediaStore.Images.Media.DESCRIPTION, "User Picture for Rushup");

                            Uri picUri = context.getContentResolver ().insert (MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra (MediaStore.EXTRA_OUTPUT, picUri);
                            pickerDialog.OnCameraClicked(intent,picUri);
                            dialog.dismiss();
                        }
                        catch (Exception exception){
                            Log.d("HeroJongi"," exception "+exception.getMessage());
                        }
                    }

                }
            });
            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickerDialog.OnGalleryClicked(intent);
                    if(dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        }

    }

    public static void writeBitmaptoInternalStorage(Context context,byte[] byteArray){
        if(context!=null && byteArray!=null) {
            try{
            Log.d("HeroJongi"," we are saving bitmap in internal storage");
                InternalStorage.writeBitmap(context, byteArray, "driverPicture");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void BitmapOperation(Context context,Bitmap bitmap){
        if(context!=null && bitmap!=null) {
            byte[] byteArray = getByteArray(bitmap);
            writeBitmaptoInternalStorage(context, byteArray);
        }
    }

    public static byte[] getByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();

    }

    public static byte[] readBytes(Uri uri, Context context) throws IOException {
        // this dynamically extends to take the bytes you read
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void UpdateDialog(final Context context, final String attribute, final String attributeName, final RushUpDeliverySettings settings, String att_value) {
      if(context!=null){
          final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
          dialog.setContentView(R.layout.update_attribute_dialog);
          WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
          lp.copyFrom(dialog.getWindow().getAttributes());
          lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
          lp.gravity = Gravity.CENTER;
          dialog.getWindow().setAttributes(lp);
          dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
          dialog.show();
          final TextView attribute_name = (TextView) dialog.findViewById(R.id.attribute_name);
          final EditText attribute_value = (EditText) dialog.findViewById(R.id.attribute_value);
          if (att_value != null) {
              if (att_value.equalsIgnoreCase(context.getResources().getString(R.string.verification_code))) {
                  attribute_value.setHint(att_value);
              } else {
                  attribute_value.setText(att_value);
              }
          }
          Button update = (Button) dialog.findViewById(R.id.update);
          attribute_name.setText(attributeName);
          Button cancel = (Button) dialog.findViewById(R.id.cancel);
          if (att_value != null) {
              if (att_value.equalsIgnoreCase(context.getResources().getString(R.string.verification_code))) {
                  update.setText(context.getResources().getString(R.string.done));
              } else {
                  update.setText(context.getResources().getString(R.string.update));
              }
          }
          if (attribute.equalsIgnoreCase("email")) {
              if (attributeName.equalsIgnoreCase(context.getResources().getString(R.string.verify_email))) {
                  attribute_value.setInputType(InputType.TYPE_CLASS_NUMBER);
                  settings.openVerifyPhoneEmail(dialog,"email");
              } else {
                  attribute_value.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
              }
          }
          if (attribute.equalsIgnoreCase("phone_number")) {
              if (attributeName.equalsIgnoreCase(context.getResources().getString(R.string.verify_phone))) {
                  attribute_value.setInputType(InputType.TYPE_CLASS_NUMBER);
                  settings.openVerifyPhoneEmail(dialog,"phone_number");
              } else {
                  attribute_value.setInputType(InputType.TYPE_CLASS_PHONE);
              }
          }
          cancel.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  dialog.dismiss();
              }
          });
          update.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(settings!=null){
                      String value = attribute_value.getText().toString();
                      if (attribute.equalsIgnoreCase("email")) {
                          if (attributeName.equalsIgnoreCase(context.getResources().getString(R.string.verify_email))) {

                              if (value == null || value.equalsIgnoreCase("")) {
                                  attribute_value.setError(context.getResources().getString(R.string.error_code));
                              } else {
                                  settings.Verify_Email_Password("email", value, dialog);
                              }
                          } else {
                              if (value == null || value.equalsIgnoreCase("")) {
                                  attribute_value.setError(context.getResources().getString(R.string.email_error));
                              } else if (!Utils.isEmailValid(value)) {
                                  attribute_value.setError(context.getResources().getString(R.string.email_not_valid));
                              } else {
                                  attribute_value.setError(null);
                                  settings.UpdateAttribute(attribute, value, dialog);
                              }
                          }
                      }
                      else if (attribute.equalsIgnoreCase("phone_number")) {
                          if (attributeName.equalsIgnoreCase(context.getResources().getString(R.string.verify_phone))) {
                              if (value == null || value.equalsIgnoreCase("")) {
                                  attribute_value.setError(context.getResources().getString(R.string.error_code));
                              } else {
                                  settings.Verify_Email_Password("phone_number", value, dialog);
                              }
                          } else {
                              if (value == null || value.equalsIgnoreCase("")) {
                                  attribute_value.setError(context.getResources().getString(R.string.phone_error));

                              } else if (!Utils.isValidMobileNumber(value)) {
                                  attribute_value.setError(context.getResources().getString(R.string.phone_valid));

                              } else {
                                  attribute_value.setError(null);
                                  settings.UpdateAttribute(attribute, value, dialog);
                              }
                          }
                      }

                  }
              }
          });
      }
    }
    public static boolean isValidMobileNumber(String phone) {
        String expression = "^[+][0-9]{7,18}$";

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();

    }

    public static void ChangePasswordDialog(final Context context, final RushUpDeliverySettings settings) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_password);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        final EditText old_password = (EditText) dialog.findViewById(R.id.old_password);
        final EditText new_password = (EditText) dialog.findViewById(R.id.new_password);
        Button update = (Button) dialog.findViewById(R.id.update);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OLdPassword = old_password.getText().toString();
                String NewPassword = new_password.getText().toString();
                if (checkPassword(context, old_password, new_password, OLdPassword, NewPassword)) {
                    settings.ChangePassword(NewPassword, OLdPassword, dialog);
                }
            }
        });
    }

    private static boolean checkPassword(Context context, EditText oldpass, EditText newpass, String oldPass, String newPass) {
        if(context!=null) {
            if (oldPass == null || oldPass.equalsIgnoreCase("")) {
                oldpass.setError(context.getResources().getString(R.string.password_error));
                return false;
            } else if (!Utils.isPasswordValid(oldPass)) {
                oldpass.setError(context.getResources().getString(R.string.password_valid));
                return false;
            } else {
                oldpass.setError(null);
            }
            if (newPass == null || newPass.equalsIgnoreCase("")) {
                newpass.setError(context.getResources().getString(R.string.password_error));
                return false;
            } else if (!Utils.isPasswordValid(newPass)) {
                newpass.setError(context.getResources().getString(R.string.password_valid));
                return false;
            } else {
                newpass.setError(null);
            }
        }
        return true;
    }

    public static void rotateImage(Context context,Uri uri,Bitmap bitmap,PickerInterface pickerInterface,int type){
        if(context!=null && bitmap!=null && pickerInterface!=null && uri!=null){
            float rotation=getExifAngle(context,uri);
            Log.d("HeroJongi"," Reach here for image "+rotation);
            Matrix matrix = new Matrix();
            if(rotation==90f){
                matrix.postRotate(90);
            }
            else if(rotation==180f){
                matrix.postRotate(180);
            }
            else if(rotation==270f){
                matrix.postRotate(270);
            }
            else{
                matrix.postRotate(0);
            }
            Bitmap _bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            pickerInterface.rotateimage(_bitmap,uri,type);
        }
    }

    public static float getExifAngle(Context context, Uri uri) {
        try {
            ExifInterface exifInterface = getExifInterface(context, uri);
            if(exifInterface == null) {
                return -1f;
            }

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90f;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180f;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270f;
                case ExifInterface.ORIENTATION_NORMAL:
                    return 0f;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    return -1f;
                default:
                    return -1f;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1f;
        }
    }

    @Nullable
    public static ExifInterface getExifInterface(Context context, Uri uri) {
        try {
            String path = uri.toString();
            if (path.startsWith("file://")) {
                return new ExifInterface(path);
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (path.startsWith("content://")) {
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    return new ExifInterface(inputStream);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
