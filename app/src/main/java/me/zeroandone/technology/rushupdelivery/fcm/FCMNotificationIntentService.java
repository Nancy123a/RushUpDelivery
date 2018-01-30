package me.zeroandone.technology.rushupdelivery.fcm;


import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.zeroandone.technology.rushupdelivery.MainActivity;
import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.objects.PushType;
import me.zeroandone.technology.rushupdelivery.utils.Application;
import me.zeroandone.technology.rushupdelivery.utils.NotificationIDs;
import me.zeroandone.technology.rushupdelivery.utils.NotificationSound;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

public class FCMNotificationIntentService extends FirebaseMessagingService {
    public static final String LOG_TAG="FCMNotificationIntent";
    NotificationIDs notificationIDs;
    NotificationSound notificationSound;
    public FCMNotificationIntentService() {
        super();
    }


    @Override
    public void onMessageReceived(RemoteMessage message) {
        String msg = message.getData().get("message");
        String type = message.getData().get("type");
        PushType pushType = PushType.valueOf(type);
        sendNotification(msg,pushType);

    }

    public void sendNotification(String message,PushType type){
      if(message!=null && !message.equalsIgnoreCase("")){
          Log.d("HeroJongi","onNotification "+type+"  "+message);

          notificationIDs=new NotificationIDs(FCMNotificationIntentService.this);
          notificationSound=new NotificationSound(FCMNotificationIntentService.this);

          Intent intent =  new Intent(this, MainActivity.class);
          Gson gson = new Gson();
          NotificationCompat.Builder builder = null;
          Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
          PendingIntent pIntent=null;
          int Notificationid=new Random().nextInt();

          switch (type){
              case delivery_new:
                  // timer and work on notification
                  DeliveryRequest deliveryRequest=gson.fromJson(message, DeliveryRequest.class);
                  if(deliveryRequest!=null) {
                      notificationIDs.addValuetoArrayList(getMap("driver_request",Notificationid));
                      intent.putExtra("delivery_update",deliveryRequest);
                      pIntent = PendingIntent.getActivity(this, new Random().nextInt(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                      builder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.logo).setContentTitle(getResources().getString(R.string.app_name))
                              .setContentText(getResources().getString(R.string.new_delivery)+" " + deliveryRequest.getPickupName()).setAutoCancel(true).setColor(getResources().getColor(R.color.colorPrimary))
                              .setContentIntent(pIntent);
                      if(notificationSound.getNotificationsound()){
                          builder.setSound(alarmSound);
                      }
                      if (Application.getInstance() != null && Application.getInstance().getRushUpDeliverySettings() != null) {
                          Application.getInstance().getRushUpDeliverySettings().onNotificationRecieved(PushType.delivery_update, deliveryRequest);
                      }
                      else{
                          Handler handler = new Handler(Looper.getMainLooper());
                          handler.postDelayed(new Runnable() {
                              public void run() {
                                  // cancel the notification
                                  Log.d("HeroJongi","onTimeRun");
                                Utils.RemoveNotification(FCMNotificationIntentService.this,"delivery_update");
                              }
                          }, 60*1000 );
                      }
                  }
                  break;
          }
          if (builder != null) {
              NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
              manager.notify(Notificationid, builder.build());
          }

      }
    }

    public Map<String, String> getMap(String pushtype, int id) {
        Map<String, String> myMap = new HashMap<String, String>();
        myMap.put("key", pushtype);
        myMap.put("value", String.valueOf(id));
        return myMap;
    }
}
