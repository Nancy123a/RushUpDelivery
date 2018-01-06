package me.zeroandone.technology.rushupdelivery.utils;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class InternalStorage {

    public InternalStorage() {
    }

    public static void writeFCMToken(Context context, String key, String fcm_Token) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        fos.write(fcm_Token.getBytes());
        fos.close();
    }

    public static String readFCMToken(Context context,String key) throws IOException, ClassNotFoundException{
        FileInputStream fin = context.openFileInput(key);
        int c;
        String fcm_token="";
        while( (c = fin.read()) != -1){
            fcm_token = fcm_token + Character.toString((char)c);
        }

        fin.close();

        return fcm_token;

    }

    public static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    public static Object readObject(Context context, String key) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

}
