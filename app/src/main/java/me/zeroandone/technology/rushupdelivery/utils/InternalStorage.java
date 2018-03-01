package me.zeroandone.technology.rushupdelivery.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public static void writeBitmap(Context context, byte[] bitmap,String key) throws IOException {
        if(context!=null && bitmap!=null) {
            FileOutputStream out = context.openFileOutput(key, Context.MODE_PRIVATE);
            out.write(bitmap);
            out.close();
            out.flush();

        }
    }

    public static Bitmap readBitmap(Context context,String key) throws IOException{
        Bitmap bitmap=null;
        if(context!=null) {
            FileInputStream fis = context.openFileInput(key);
            bitmap = BitmapFactory.decodeStream(fis);
        }
        return bitmap;
    }


    public static File returnFile(Context context, String key){
        File f = new File(context.getCacheDir(), key);
        try {
            f.createNewFile();
            Bitmap bitmap = readBitmap(context,key);
            if(bitmap!=null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }


}
