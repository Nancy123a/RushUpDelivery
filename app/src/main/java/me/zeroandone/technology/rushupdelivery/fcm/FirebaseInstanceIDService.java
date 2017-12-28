package me.zeroandone.technology.rushupdelivery.fcm;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import me.zeroandone.technology.rushupdelivery.utils.InternalStorage;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    String token;
    @Override
    public void onTokenRefresh() {
        token = FirebaseInstanceId.getInstance().getToken();
        try {
            InternalStorage.writeFCMToken(this,"FCMToken",token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
