package me.zeroandone.technology.rushupdelivery.fcm;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Random;

import me.zeroandone.technology.rushupdelivery.MainActivity;
import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.objects.PushType;
import me.zeroandone.technology.rushupdelivery.utils.Application;

public class FCMNotificationIntentService extends FirebaseMessagingService {
    public static final String LOG_TAG="FCMNotificationIntent";
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

          Intent intent =  new Intent(this, MainActivity.class);
          Gson gson = new Gson();
          NotificationCompat.Builder builder = null;
          Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
          PendingIntent pIntent=null;

          switch (type){
              case delivery_new:
                  DeliveryRequest deliveryRequest=gson.fromJson(message, DeliveryRequest.class);
                  if (Application.getInstance()!=null && Application.getInstance().getRushUpDeliverySettings()!=null) {
                      Application.getInstance().getRushUpDeliverySettings().onNotificationRecieved(PushType.delivery_update,deliveryRequest);
                  } else {
                          intent.putExtra("delivery_update",deliveryRequest);
                          pIntent = PendingIntent.getActivity(this, new Random().nextInt(), intent, PendingIntent.FLAG_ONE_SHOT);
                          builder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.logo).setContentTitle(getResources().getString(R.string.app_name))
                                  .setContentText("You have new delivery request from: " + deliveryRequest.getPickupName()).setAutoCancel(true).setColor(getResources().getColor(R.color.colorPrimary))
                                  .setSound(alarmSound).setContentIntent(pIntent);

                  }
                  break;
          }
          if (builder != null) {
              NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
              manager.notify(new Random().nextInt(), builder.build());
          }

      }
    }
}
