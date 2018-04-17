package me.zeroandone.technology.rushupdelivery.interfaces;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

public interface PickerInterface {

    public void OnCameraClicked(Intent intent, Uri uri);

    public void OnGalleryClicked(Intent intent);

    public void rotateimage(Bitmap bitmap, Uri uri, int type);

}
