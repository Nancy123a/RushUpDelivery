package me.zeroandone.technology.rushupdelivery;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import me.zeroandone.technology.rushupdelivery.objects.DriverCode;
import me.zeroandone.technology.rushupdelivery.utils.AppHelper;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

public class Registration extends AppCompatActivity implements View.OnClickListener, PickerInterface {
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 93;
    EditText username, password, phone, email, number1, number2, number3, number4;
    TextView submit,insertcode;
    TextView already_have_account;
    String Username, Password, Nb1, Nb2, Nb3, Nb4, Email, Phone,Code;
    CircleImageView pickImage;
    PickerInterface pickerInterface = this;
    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            AppHelper.federateWithProvider();
            if (!signUpConfirmationState) {
                Intent intent=new Intent(Registration.this,Confirm.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", Username);
                bundle.putString("password",Password);
                bundle.putString("code",Code);
                bundle.putString("phone",Phone);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void onFailure(Exception exception) {
            Log.d("SPECIAL", "Error " + exception.getMessage());
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        number1 = (EditText) findViewById(R.id.number1);
        number2 = (EditText) findViewById(R.id.number2);
        number3 = (EditText) findViewById(R.id.number3);
        number4 = (EditText) findViewById(R.id.number4);
        submit = (TextView) findViewById(R.id.submit);
        insertcode=(TextView) findViewById(R.id.insertcode);
        already_have_account=(TextView) findViewById(R.id.already_have_account);
        pickImage = (CircleImageView) findViewById(R.id.camera);
        AppHelper.initializeCognitoPool(Registration.this);
        submit.setOnClickListener(this);
        pickImage.setOnClickListener(this);
        already_have_account.setOnClickListener(this);


    }

    public boolean isFormFull() {
        Username = username.getText().toString();
        Password = password.getText().toString();
        Nb1 = number1.getText().toString();
        Nb2 = number2.getText().toString();
        Nb3 = number3.getText().toString();
        Nb4 = number4.getText().toString();
        Email = email.getText().toString();
        Phone = phone.getText().toString();
        if (Email == null || Email.equalsIgnoreCase("")) {
            email.setError(getResources().getString(R.string.email_error));
            return false;
        } else if (!Utils.isEmailValid(Email)) {
            email.setError(getResources().getString(R.string.email_not_valid));
            return false;
        } else {
            email.setError(null);
        }
        if (Username == null || Username.equalsIgnoreCase("")) {
            username.setError(getResources().getString(R.string.username_error));
            return false;
        } else {
            username.setError(null);
        }
        if (Password == null || Password.equalsIgnoreCase("")) {
            password.setError(getResources().getString(R.string.password_error));
            return false;
        } else if (!Utils.isPasswordValid(Password)) {
            password.setError(getResources().getString(R.string.password_valid));
            return false;
        } else {
            password.setError(null);
        }
        if (Phone == null || Phone.equalsIgnoreCase("")) {
            phone.setError(getResources().getString(R.string.phone_error));
            return false;
        } else if (!Utils.isValidMobileNumber(Phone)) {
            phone.setError(getResources().getString(R.string.phone_valid));
            return false;
        } else {
            phone.setError(null);
        }
        if (Nb1 == null || Nb1.equalsIgnoreCase("")) {
            number1.setError(getResources().getString(R.string.first_number));
            return false;
        } else {
            number1.setError(null);
        }
        if (Nb2 == null || Nb2.equalsIgnoreCase("")) {
            number2.setError(getResources().getString(R.string.second_number));
            return false;
        } else {
            number2.setError(null);
        }
        if (Nb3 == null || Nb3.equalsIgnoreCase("")) {
            number3.setError(getResources().getString(R.string.third_number));
            return false;
        } else {
            number3.setError(null);
        }
        if (Nb4 == null || Nb4.equalsIgnoreCase("")) {
            number4.setError(getResources().getString(R.string.fourth_number));
            return false;
        } else {
            number4.setError(null);
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.already_have_account:
                Intent intent=new Intent(this,UserLogin.class);
                startActivity(intent);
                break;
            case R.id.submit:
                if (isFormFull()) {
                    Log.d("SPECIAL", " Username " + isFormFull());
                    insertcode.setText(getResources().getString(R.string.insertcode));
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Code = Nb1 + Nb2 + Nb3 + Nb4;
                                DriverCode driverCode = new DriverCode(Username, Code);
                                DriverCode _driverCode = AppHelper.getRushUpClientUnauthenticated().driverCodeCheckcodePost(driverCode);
                                if (_driverCode.getCode() != null) {
                                    if (_driverCode.getCode().equalsIgnoreCase(Code)) {
                                        CognitoUserAttributes userAttributes = new CognitoUserAttributes();
                                        userAttributes.addAttribute("name", Username);
                                        userAttributes.addAttribute("email", Email);
                                        userAttributes.addAttribute("phone_number", Phone);
                                        userAttributes.addAttribute("custom:type","driver");
                                        if (AppHelper.getPool() != null) {
                                            AppHelper.getPool().getCognitoUserPool().signUpInBackground(Username, Password, userAttributes, null, signUpHandler);
                                        }

                                    } else {
                                        runOnUiThread(new Thread(new Runnable() {
                                            public void run() {
                                                // enter correct code
                                                insertcode.setText(getResources().getString(R.string.please_insert_correct_code));
                                            }
                                        }));
                                    }
                                }
                            } catch (final Exception ex) {
                                Log.d("SPECIAL", ex.getMessage());
                                runOnUiThread(new Thread(new Runnable() {
                                    public void run() {
                                        if (ex.getMessage().contains("Enter your correct user name")) {
                                            username.setError(getResources().getString(R.string.username_wrong));
                                        } else if (ex.getMessage().contains("User already exists")) {
                                            username.setError(getResources().getString(R.string.username_already_exist));
                                        } else if (ex.getMessage().contains("PreSignUp failed with error Email already exist")) {
                                            email.setError(getResources().getString(R.string.email_already_exist));
                                        } else if (ex.getMessage().contains("PreSignUp failed with error Phone already exist")) {
                                            phone.setError(getResources().getString(R.string.phonenumber_already_exist));
                                        }

                                    }

                                }
                                ));
                            }
                        }
                    }).start();

                }
                break;
            case R.id.camera:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Utils.startImagePicker(this, pickerInterface);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
                }
                break;
        }
    }

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
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap bitmap = (Bitmap) extras.get("data");

                        //camera reverse image
                        if (bitmap != null) {
                            Utils.BitmapOperation(this, bitmap);
                            pickImage.setImageBitmap(bitmap);
                        }
                    }
                }
            } else if (requestCode == 2) {
                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        try {
                            byte[] byteArray = Utils.readBytes(selectedImage, this);
                            Utils.writeBitmaptoInternalStorage(this, byteArray);
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
