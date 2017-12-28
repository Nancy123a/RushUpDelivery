package me.zeroandone.technology.rushupdelivery.fcm;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMNotificationIntentService extends FirebaseMessagingService {
    public static final String LOG_TAG="FCMNotificationIntent";
    public FCMNotificationIntentService() {
        super();
    }


    @Override
    public void onMessageReceived(RemoteMessage message) {
        String msg = message.getData().get("message");
        String type = message.getData().get("type");
        sendNotification(msg,type);

    }

    public void sendNotification(String messafe,String type){

    }
}
