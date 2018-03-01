package me.zeroandone.technology.rushupdelivery;


import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zeroandone.technology.rushupdelivery.interfaces.PickerInterface;
import me.zeroandone.technology.rushupdelivery.model.Driver;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryPickUpDropoff;
import me.zeroandone.technology.rushupdelivery.utils.AppHelper;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

import static java.security.AccessController.getContext;

public class Registration extends AppCompatActivity implements View.OnClickListener,PickerInterface{
    EditText username,password,number1,number2,number3,number4;
    TextView submit;
    String Username,Password,Nb1,Nb2,Nb3,Nb4;
    CircleImageView pickImage;
    PickerInterface pickerInterface=this;
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 93;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        number1 = (EditText) findViewById(R.id.number1);
        number2 = (EditText) findViewById(R.id.number2);
        number3 = (EditText) findViewById(R.id.number3);
        number4 = (EditText) findViewById(R.id.number4);
        submit = (TextView) findViewById(R.id.submit);
        pickImage = (CircleImageView) findViewById(R.id.camera);
        AppHelper.initializeCognitoPool(Registration.this);
        submit.setOnClickListener(this);
        pickImage.setOnClickListener(this);


    }


    public boolean isFormFull(){
        Username = username.getText().toString();
        Password = password.getText().toString();
        Nb1 = number1.getText().toString();
        Nb2 = number2.getText().toString();
        Nb3 = number3.getText().toString();
        Nb4 = number4.getText().toString();
        if(Username==null || Username.equalsIgnoreCase("")){
            username.setError(getResources().getString(R.string.username_error));
            return false;
        }
        else{
            username.setError(null);
        }
        if(Password==null || Password.equalsIgnoreCase("")){
            password.setError(getResources().getString(R.string.password_error));
            return false;
        }
        else if(!Utils.isPasswordValid(Password)){
            password.setError(getResources().getString(R.string.password_valid));
            return false;
        }
        else{
            password.setError(null);
        }
        if(Nb1==null || Nb1.equalsIgnoreCase("")){
            number1.setError(getResources().getString(R.string.first_number));
            return false;
        }
        else{
            number1.setError(null);
        }
        if(Nb2==null || Nb2.equalsIgnoreCase("")){
            number2.setError(getResources().getString(R.string.second_number));
            return false;
        }
        else{
            number2.setError(null);
        }
        if(Nb3==null || Nb3.equalsIgnoreCase("")){
            number3.setError(getResources().getString(R.string.third_number));
            return false;
        }
        else{
            number3.setError(null);
        }
        if(Nb4==null || Nb4.equalsIgnoreCase("")){
            number4.setError(getResources().getString(R.string.fourth_number));
            return false;
        }
        else{
            number4.setError(null);
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                Log.d("HeroJongi"," Username "+isFormFull());
                if(isFormFull()){
                    //check number if true register register
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Driver driver = new Driver();
                                driver.setUsername(Username);
                                Driver _driver= AppHelper.getRushUpClient().driverCodeUsernamePost(driver);
                                Log.d("HeroJongi","Success "+_driver.toString());
                            }catch (Exception ex) {
                                Log.e("HeroJongi","Error"+ex.getMessage());
                            }
                        }
                    }).start();


                                //  AppHelper.getPool().getCognitoUserPool().signUpInBackground(Username, Password, null, null, signUpHandler);



                }
                break;
            case R.id.camera:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Utils.startImagePicker(this,pickerInterface);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
                }
                break;
        }
    }


    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            AppHelper.federateWithProvider();
            if (!signUpConfirmationState) {
                // User is not confirmed
                //   Utils.confirmSignUp(getActivity(), Username, Password, getDialog(), Phone,"person");
            }
        }

        @Override
        public void onFailure(Exception exception) {
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            if (requestCode == EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                    dialogBuilder.setTitle(getResources().getString(R.string.permission_failed));
                    dialogBuilder.setMessage(getResources().getString(R.string.deny_camera));
                    dialogBuilder.setNegativeButton(getString(android.R.string.ok), null);
                    dialogBuilder.show();
                } else {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Utils.startImagePicker(this, pickerInterface);
                    }
                }
            }
    }

    @Override
    public void OnCameraClicked(Intent intent) {
        startActivityForResult(intent, 1);
    }

    @Override
    public void OnGalleryClicked(Intent intent) {
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                if(data!=null) {
                    Bundle extras = data.getExtras();
                    if(extras!=null) {
                        Bitmap bitmap = (Bitmap) extras.get("data");

                        //camera reverse image
                        if (bitmap != null) {
                            Utils.BitmapOperation(this,bitmap);
                            pickImage.setImageBitmap(bitmap);
                        }
                    }
                }
            }
            else if (requestCode == 2) {
                if(data.getData()!=null) {
                    Uri selectedImage = data.getData();
                    if(selectedImage!=null) {
                        try {
                            byte[]  byteArray = Utils.readBytes(selectedImage,this);
                            Utils.writeBitmaptoInternalStorage(this,byteArray);
                            Picasso.with(getApplicationContext()).load(selectedImage).into(pickImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


        }
    }


}
