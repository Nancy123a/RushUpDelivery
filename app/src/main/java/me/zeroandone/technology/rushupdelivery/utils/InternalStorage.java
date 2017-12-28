package me.zeroandone.technology.rushupdelivery.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class InternalStorage {

    public InternalStorage() {
    }

    public static void writeFCMToken(Context context, String key, String fcm_Token) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        fos.write(fcm_Token.getBytes());
        fos.close();
    }

    public static String raedFCMToken(Context context,String key) throws IOException, ClassNotFoundException{
        FileInputStream fin = context.openFileInput(key);
        int c;
        String fcm_token="";
        while( (c = fin.read()) != -1){
            fcm_token = fcm_token + Character.toString((char)c);
        }

        fin.close();

        return fcm_token;

    }


}
