package me.zeroandone.technology.rushupdelivery;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.SignInStateChangeListener;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.google.android.gms.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wunderlist.slidinglayer.SlidingLayer;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zeroandone.technology.rushupdelivery.adapter.BalanceAdapter;
import me.zeroandone.technology.rushupdelivery.adapter.HistoryAdapter;
import me.zeroandone.technology.rushupdelivery.adapter.SettingsAdapter;
import me.zeroandone.technology.rushupdelivery.dialogs.MapDialog;
import me.zeroandone.technology.rushupdelivery.interfaces.LatLngInterpolator;
import me.zeroandone.technology.rushupdelivery.interfaces.RushUpDeliverySettings;
import me.zeroandone.technology.rushupdelivery.model.Driver;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryStatus;
import me.zeroandone.technology.rushupdelivery.objects.DriverBalance;
import me.zeroandone.technology.rushupdelivery.objects.DriverDeliveryHistory;
import me.zeroandone.technology.rushupdelivery.objects.DriverStatus;
import me.zeroandone.technology.rushupdelivery.objects.PushType;
import me.zeroandone.technology.rushupdelivery.objects.Settings;
import me.zeroandone.technology.rushupdelivery.objects.date_History;
import me.zeroandone.technology.rushupdelivery.utils.AppHelper;
import me.zeroandone.technology.rushupdelivery.utils.Application;
import me.zeroandone.technology.rushupdelivery.utils.DrawPolylineVolley;
import me.zeroandone.technology.rushupdelivery.utils.DriverPickupVolley;
import me.zeroandone.technology.rushupdelivery.utils.DriverStatusSharedPreference;
import me.zeroandone.technology.rushupdelivery.utils.InternalStorage;
import me.zeroandone.technology.rushupdelivery.utils.SaveI;
import me.zeroandone.technology.rushupdelivery.utils.UserSharedPreference;
import me.zeroandone.technology.rushupdelivery.utils.Utils;
import me.zeroandone.technology.rushupdelivery.utils.isPickUp;


public class InsideApp extends AppCompatActivity implements RushUpDeliverySettings, SignInStateChangeListener,OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private static final int PERMISSION_PHONE_CALL = 200;
    RelativeLayout homelayout, settings_menu, history_menu, balance_menu;
    TextView settings, history, balance, user_name, insert_code, package_pickup,no_history_found,score,no_balance_found;
    RatingBar ratingBar;
    ImageView settings_close, history_close, balance_close, user_image;
    EditText insert_code_edittext;
    Button stimulate;
    RecyclerView historyRecycleView;
    RecyclerView balanceRecyclerView;
    RecyclerView settings_recycler_view;
    CognitoUserDetails details;
    String Username, Phonenumber;
    LinearLayout options;
    isPickUp isPickUp;
    LatLngInterpolator latLngInterpolator;
    ValueAnimator valueAnimator;
    private float v;
    private double lat, lng;
    LatLng startPosition,endPosition;
    ImageView call;
    Dataset dataset_settings;
    SettingsAdapter adapter;
    IdentityManager identityManager;
    DriverStatusSharedPreference driverStatusSharedPreference;
    public static final int Access_Location = 70;
    LocationListener locationListener = this;
    Marker Driver;
    FrameLayout frameLayout;
    DrawPolylineVolley drawPolyline;
    DriverPickupVolley driverPickupVolley;
    Marker PickupMarker, DropOffMarker;
    DeliveryRequest deliveryRequest;
    RushUpDeliverySettings rushUpDeliverySettings = this;
    SlidingLayer bottomMenu,upperMenu;
    Button off, on;
    TextView pickupname, pickupaddress, Boy_photoname;
    CircleImageView boy_photo;
    List<LatLng> latLngs=new ArrayList<>();
    String phoneNumber;
    HistoryAdapter historyAdapter;
    BalanceAdapter balanceAdapter;
    ProgressBar historyProgressBar,balanceProgressBar;
    UserSharedPreference userSharedPreference;
    boolean drawPoly=false;
    final int[] i = {0};
    SaveI saveI;
    boolean isautomatic=false;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 5000; // 5 sec


    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_app);

        Application.getInstance().setRushUpDeliverySettings(rushUpDeliverySettings);
        drawPolyline = new DrawPolylineVolley(this,this);
        driverPickupVolley=new DriverPickupVolley(this,this);
        saveI=new SaveI(this);
        deliveryRequest = new DeliveryRequest();
        isPickUp = new isPickUp(this);
        identityManager = identityManager.getDefaultIdentityManager();
        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        settings = (TextView) findViewById(R.id.settings);
        homelayout = (RelativeLayout) findViewById(R.id.homelayout);
        settings_menu = (RelativeLayout) findViewById(R.id.settings_menu);
        history = (TextView) findViewById(R.id.history);
        balance = (TextView) findViewById(R.id.balance);
        stimulate=(Button) findViewById(R.id.stimulate);
        history_menu = (RelativeLayout) findViewById(R.id.history_menu);
        settings_close = (ImageView) findViewById(R.id.settings_close);
        history_close = (ImageView) findViewById(R.id.history_close);
        balance_close = (ImageView) findViewById(R.id.balance_close);
        user_image = (ImageView) findViewById(R.id.userimage);
        user_name = (TextView) findViewById(R.id.username);
        balance_menu = (RelativeLayout) findViewById(R.id.balance_menu);
        historyRecycleView = (RecyclerView) findViewById(R.id.historyRecycleView);
        balanceRecyclerView = (RecyclerView) findViewById(R.id.balanceRecycleView);
        settings_recycler_view = (RecyclerView) findViewById(R.id.settings_recycle_view);
        bottomMenu = (SlidingLayer) findViewById(R.id.slidingLayer2);
        upperMenu=(SlidingLayer) findViewById(R.id.slidingLayer1);
        options = (LinearLayout) findViewById(R.id.relative);
        frameLayout = (FrameLayout) findViewById(R.id.maplayout);
        off = (Button) findViewById(R.id.off);
        on = (Button) findViewById(R.id.on);
        pickupaddress = (TextView) findViewById(R.id.addresspickup);
        pickupname = (TextView) findViewById(R.id.pickupname);
        boy_photo = (CircleImageView) findViewById(R.id.boy_photo);
        Boy_photoname = (TextView) findViewById(R.id.driver_xxphoto);
        insert_code = (TextView) findViewById(R.id.insertcode);
        insert_code_edittext = (EditText) findViewById(R.id.edittext_insertcode);
        package_pickup = (TextView) findViewById(R.id.package_pickup);
        call = (ImageView) findViewById(R.id.call);
        historyProgressBar=(ProgressBar) findViewById(R.id.historyProgressBar);
        no_history_found=(TextView) findViewById(R.id.no_history_found);
        score=(TextView) findViewById(R.id.score);
        ratingBar=(RatingBar) findViewById(R.id.ratingbar);
        balanceProgressBar=(ProgressBar) findViewById(R.id.balanceProgressBar);
        no_balance_found=(TextView) findViewById(R.id.no_balance_found);
        driverStatusSharedPreference = new DriverStatusSharedPreference(this);
        userSharedPreference=new UserSharedPreference(this);



        if (driverStatusSharedPreference.getStatus() != null && !driverStatusSharedPreference.getStatus().equalsIgnoreCase("")) {
            if (driverStatusSharedPreference.getStatus().equalsIgnoreCase("on") || driverStatusSharedPreference.getStatus().equalsIgnoreCase("occupied")) {
                activeButton(on);
                inactiveButton(off);
            } else {
                activeButton(off);
                inactiveButton(on);
            }
        } else {
            activeButton(off);
            inactiveButton(on);
        }


        if (AppHelper.getPool() != null && AppHelper.getPool().getCognitoUserPool().getCurrentUser() != null) {
            AppHelper.getPool().getCognitoUserPool().getCurrentUser().getDetailsInBackground(detailsHandler);
        }

        AWSConfiguration awsConfig = new AWSConfiguration(this);
        CognitoSyncManager manager = new CognitoSyncManager(this, IdentityManager.getDefaultIdentityManager().getUnderlyingProvider(), awsConfig);
        dataset_settings = manager.openOrCreateDataset("Settings");

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        latLngInterpolator = new LatLngInterpolator.LinearFixed();
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(2000); // duration 5 second
        valueAnimator.setInterpolator(new LinearInterpolator());

        settings.setOnClickListener(this);
        history.setOnClickListener(this);
        balance.setOnClickListener(this);
        settings_close.setOnClickListener(this);
        history_close.setOnClickListener(this);
        balance_close.setOnClickListener(this);
        off.setOnClickListener(this);
        on.setOnClickListener(this);
        insert_code.setOnClickListener(this);
        call.setOnClickListener(this);
        stimulate.setOnClickListener(this);

        if (Utils.checkPlayServices(InsideApp.this)) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        insert_code_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    Log.d("HeroJongi", "editText" + insert_code_edittext.getText().toString());
                    if (!insert_code_edittext.getText().toString().equalsIgnoreCase("")) {
                        // if its pickup
                        if (isPickUp.getisPickUp()) {
                            Log.d("HeroJongi", "ItsPickup");
                            AppHelper.CheckCode(deliveryRequest, insert_code_edittext.getText().toString(), DeliveryStatus.with_delivery, true, rushUpDeliverySettings);
                        }
                        else {
                            Log.d("HeroJongi", "ItsDropOffup");
                            AppHelper.CheckCode(deliveryRequest, insert_code_edittext.getText().toString(), DeliveryStatus.delivered, false, rushUpDeliverySettings);
                        }
                    }
                    else {
                        // hide kwyboard
                        Log.d("HeroJongi", "editText is empty " + insert_code_edittext.getText().toString());

                        insert_code_edittext.setError(getResources().getString(R.string.please_insert_code));
                    }
                }

                return false;
            }
        });



    }

    @Override
    public void setRatingofDriver() {
        if(identityManager.getCachedUserID()!=null && !identityManager.getCachedUserID().equalsIgnoreCase("")){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Log.d("HeroJongi"," Success get driver rating ");
                        final Driver driver= AppHelper.getRushUpClient().driverDriverIdGet(identityManager.getCachedUserID());
                        runOnUiThread(new Thread(new Runnable() {
                            public void run() {
                                if(driver!=null && driver.getScore()!=null) {
                                    setRatingBar(driver.getScore());
                                }
                            }
                        }));
                    }catch (Exception ex) {
                        Log.e("HeroJongi","Fail in getting driver rating "+ex.getMessage());
                    }
                }
            }).start();
        }
    }

    @Override
    public void onBalanceHistoryRowClicked(DeliveryRequest deliveryRequest,boolean isHistory) {
        if(deliveryRequest!=null){
            upperMenu.closeLayer(true);
            if(isHistory){
                history_menu.setVisibility(View.GONE);
            }
            else{
                balance_menu.setVisibility(View.GONE);
            }
            homelayout.setVisibility(View.VISIBLE);
            FragmentManager fm = getSupportFragmentManager();
            MapDialog mapDialog= MapDialog.newInstance("Some Title");
            mapDialog.setStyle(android.app.DialogFragment.STYLE_NO_TITLE, R.style.MyTheme);
            mapDialog.setDelivery(deliveryRequest);
            mapDialog.show(fm, "map_dialog");
        }
    }

    @Override
    public void clearHistory() {
        java.util.Date currentTime = Calendar.getInstance().getTime();
        String date=String.valueOf(currentTime.getTime()).substring(0,10);
        dataset_settings.put("date",date);
        dataset_settings.synchronizeOnConnectivity(new Dataset.SyncCallback() {
            public void onSuccess(Dataset dataset, List list) {
                Log.d("HeroJongi", "Success on  save date");
            }

            public boolean onConflict(Dataset dataset, List list) {
                Log.d("HeroJongi", "On Conflict ");
                return false;

            }

            public boolean onDatasetDeleted(Dataset dataset, String list) {
                Log.d("HeroJongi", "On DatasetDeleted ");
                return true;
            }

            public boolean onDatasetsMerged(Dataset dataset, List list) {
                Log.d("HeroJongi", "OnDataSet Merged");
                return true;
            }

            public void onFailure(DataStorageException exception) {
                Log.d("HeroJongi", "On DataSet Fail " + exception.getMessage());
            }
        });

    }

    @Override
    public void deleteFile() {
      InternalStorage.deleteFile(this,"driverPicture");

    }

    @Override
    public void afterDriverPickFinish(List<LatLng> LatLongs) {
        APICallToDrawPolyline();
        latLngs.addAll(LatLongs);
    }

    @Override
    public void onPickupDriverFinish(List<LatLng> LatLongs) {
        latLngs.addAll(LatLongs);
    }

    @Override
    public void setUserImage(File file, boolean isPickUp,DeliveryRequest deliveryRequest) {
      if(isPickUp){
          if (deliveryRequest.getPickupName() != null && !deliveryRequest.getPickupName().equalsIgnoreCase("")) {
              pickupname.setText(deliveryRequest.getPickupName());
              if(file!=null && file.length()>0){
                  boy_photo.setVisibility(View.VISIBLE);
                  Boy_photoname.setVisibility(View.GONE);
                  Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                  boy_photo.setImageBitmap(myBitmap);
              }
              else {
                  boy_photo.setVisibility(View.GONE);
                  Boy_photoname.setVisibility(View.VISIBLE);
                  char firstletter = Character.toUpperCase(deliveryRequest.getPickupName().charAt(0));
                  Boy_photoname.setText(String.valueOf(firstletter));
              }
          }

      }
      else{
          if (deliveryRequest.getDropoffName() != null && !deliveryRequest.getDropoffName().equalsIgnoreCase("")) {
              pickupname.setText(deliveryRequest.getDropoffName());
              if(file!=null && file.length()>0){
                  boy_photo.setVisibility(View.VISIBLE);
                  Boy_photoname.setVisibility(View.GONE);
                  Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                  boy_photo.setImageBitmap(myBitmap);
              }
              else {
                  boy_photo.setVisibility(View.GONE);
                  Boy_photoname.setVisibility(View.VISIBLE);
                  char firstletter = Character.toUpperCase(deliveryRequest.getPickupName().charAt(0));
                  Boy_photoname.setText(String.valueOf(firstletter));
              }
          }
      }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }
    GetDetailsHandler detailsHandler = new GetDetailsHandler() {
        public void onSuccess(CognitoUserDetails cognitoUserDetails) {
            Log.d("HeroJongi", " Here details handler");
            details = cognitoUserDetails;
            Username = AppHelper.getPool().getCognitoUserPool().getCurrentUser().getUserId();
            Phonenumber = cognitoUserDetails.getAttributes().getAttributes().get("phone_number");
            ReadSettings();
            SharedPreferences settingss = getSharedPreferences("showpopup", 0);
            SharedPreferences.Editor editor = settingss.edit();

           if(userSharedPreference.getUserName()==null || userSharedPreference.getUserName().equalsIgnoreCase("") || !userSharedPreference.getUserName().equalsIgnoreCase(Username)){
                if(identityManager.isUserSignedIn()){
                    AppHelper.SaveDriverFCMTokenToCloud(InsideApp.this,Username,rushUpDeliverySettings);
                    Log.d("SOLS"," User is posting token");
                    userSharedPreference.saveUserName(Username);
                    File file = InternalStorage.returnFile(InsideApp.this, "driverPicture");
                    Log.d("SOLS", "first time upload " + file+" "+file.length());
                    if (file.length() != 0) {
                        AppHelper.uploadDownloadPicture(true, InsideApp.this, file, identityManager, rushUpDeliverySettings);
                    } else {
                        AppHelper.uploadDownloadPicture(false, InsideApp.this, null, identityManager, rushUpDeliverySettings);
                    }
                    setRatingofDriver();
                }
            }
            else{
                Log.d("HeroJongi"," WE are here ");
                AppHelper.uploadDownloadPicture(false,InsideApp.this,null,identityManager,rushUpDeliverySettings);
                setRatingofDriver();
            }


            setAdapter();
            IntentsListener();
            if (Username.length() > 0) {
                String CapUsername = Username.substring(0, 1).toUpperCase() + Username.substring(1);
                user_name.setText(CapUsername);
            }

        }
        @Override
        public void onFailure(Exception exception) {

        }
    };

    private void ReadSettings() {
        try {
            Object data = InternalStorage.readObject(this, "ActiveDelivery");
            if (data instanceof DeliveryRequest) {
                DeliveryRequest deliveryRequest = (DeliveryRequest) data;
                this.deliveryRequest = deliveryRequest;
                // plot the pins
                if (deliveryRequest != null) {
                    Log.d("HeroJongi ", "Read settings ");
                    if (deliveryRequest.getPickupLocation() != null && deliveryRequest.getDropoffLocation() != null) {
                        options.setVisibility(View.GONE);
                        PlotPins(deliveryRequest);
                        showBottomMenu(deliveryRequest);
                        if (isPickUp.getisPickUp()) {
                            FillUpBottomMenu(deliveryRequest, true);
                        } else {
                            FillUpBottomMenu(deliveryRequest, false);
                        }
                    }


                }

                if(saveI.getII()!=0){
                    i[0]=saveI.getII();
                }
            }
        } catch (IOException e) {
            Log.d("HeroJongi", "error" + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.d("HeroJongi", "error" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setRatingBar(String _score) {
        float __score=Float.parseFloat(_score);
        ratingBar.setRating(__score);
        double _double_rating=Double.parseDouble(_score);
        score.setText(new DecimalFormat("##.##").format(_double_rating));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

    }

    @Override
    public void onBackPressed() {
        //make some alert for the user
        return;
    }

    public void makePhoneCall(String Phonenumber) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Log.d("Call", " its permission is granted");
            this.phoneNumber = Phonenumber;
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Phonenumber));
            startActivity(intent);
        } else {
            Log.d("Call", " its permission is not granted");
            ActivityCompat.requestPermissions(InsideApp.this, new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSION_PHONE_CALL);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stimulate:
                Log.d("HeroJongi"," stimulate");
                isautomatic=true;
                    final Handler DriverHandler = new Handler();
                    final Runnable DriverRunnable = new Runnable() {
                        @Override
                        public void run() {
                         if(i[0]<latLngs.size()-1) {
                             final LatLng latLngprevious = latLngs.get(i[0]);
                             LatLng latLngcurrent = latLngs.get(i[0] + 1);

                             final Location prev_location = new Location("driver_previous");
                             Location current_location = new Location("driver_current");

                             prev_location.setLongitude(latLngprevious.longitude);
                             prev_location.setLatitude(latLngprevious.latitude);

                             AppHelper.sendDriverLocation(prev_location);

                             if (i[0] == 0) {
                                 current_location = prev_location;
                             } else {
                                 if (latLngcurrent != null) {
                                     current_location.setLatitude(latLngcurrent.latitude);
                                     current_location.setLongitude(latLngcurrent.longitude);
                                 }
                             }
                             if(latLngprevious!=null) {
                                 if(Driver!=null) {
                                     Driver.setPosition(latLngprevious);
                                 }
                             }
                             if (latLngcurrent != null) {
                                 setBearing(prev_location, current_location);
                             }

                             i[0]++;
                         }
                         else{
                                 DriverHandler.removeCallbacks(this);
                                 isautomatic=false;

                         }

                           DriverHandler.postDelayed(this, 3500); // 8 seconds
                        }
                    };
                    DriverHandler.postDelayed(DriverRunnable, 100);
                break;
            case R.id.call:
                if (deliveryRequest != null) {
                    if (isPickUp.getisPickUp()) {
                        if (deliveryRequest.getFrom() != null && !deliveryRequest.getFrom().equalsIgnoreCase("")) {
                            makePhoneCall(deliveryRequest.getFrom());
                        }
                    } else {
                        if (deliveryRequest.getTo() != null && !deliveryRequest.getTo().equalsIgnoreCase("")) {
                            makePhoneCall(deliveryRequest.getTo());
                        }
                    }
                }
                break;
            case R.id.insertcode:
                insert_code.setVisibility(View.GONE);
                insert_code_edittext.setVisibility(View.VISIBLE);
                break;
            case R.id.on:
                AppHelper.UpdateStatusofDriver(DriverStatus.on);
                driverStatusSharedPreference.saveStatus("on");
                activeButton(on);
                inactiveButton(off);
                break;
            case R.id.off:
                AppHelper.UpdateStatusofDriver(DriverStatus.off);
                driverStatusSharedPreference.saveStatus("off");
                activeButton(off);
                inactiveButton(on);
                break;
            case R.id.settings:
                homelayout.setVisibility(View.GONE);
                settings_menu.setVisibility(View.VISIBLE);
                break;
            case R.id.history:
                homelayout.setVisibility(View.GONE);
                history_menu.setVisibility(View.VISIBLE);
                // api call
                getHistory();
                break;
            case R.id.settings_close:
                homelayout.setVisibility(View.VISIBLE);
                settings_menu.setVisibility(View.GONE);
                break;
            case R.id.history_close:
                homelayout.setVisibility(View.VISIBLE);
                history_menu.setVisibility(View.GONE);
                break;
            case R.id.balance_close:
                homelayout.setVisibility(View.VISIBLE);
                balance_menu.setVisibility(View.GONE);
                break;
            case R.id.balance:
                homelayout.setVisibility(View.GONE);
                balance_menu.setVisibility(View.VISIBLE);
                // api call
                getBalance();
                break;
        }
    }

    public void getHistory(){
        if(historyAdapter==null) {
            historyProgressBar.setVisibility(View.VISIBLE);
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    date_History date_history=null;
                    if (dataset_settings != null) {
                        if (dataset_settings.get("date") != null) {
                            String date = dataset_settings.get("date");
                            date_history = new date_History(Long.parseLong(date));
                        } else {
                            date_history = new date_History(0L);
                        }
                        final DriverDeliveryHistory driverDeliveryHistory = AppHelper.getRushUpClient().deliveryHistoryDriverPost(date_history);
                        Log.d("HeroJongi", "Delivery Success " + driverDeliveryHistory.getDriver_history().size());
                        if (driverDeliveryHistory.getDriver_history() != null) {
                            runOnUiThread(new Thread(new Runnable() {
                                public void run() {
                                    historyProgressBar.setVisibility(View.GONE);
                                    if (driverDeliveryHistory.getDriver_history().size() > 0) {
                                        if (historyAdapter == null) {
                                            historyRecycleView.setVisibility(View.VISIBLE);
                                            no_history_found.setVisibility(View.GONE);
                                            historyAdapter = new HistoryAdapter(driverDeliveryHistory.getDriver_history(), InsideApp.this, rushUpDeliverySettings);
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InsideApp.this, OrientationHelper.VERTICAL, false);
                                            historyRecycleView.setLayoutManager(linearLayoutManager);
                                            historyRecycleView.setItemAnimator(new DefaultItemAnimator());
                                            historyRecycleView.setAdapter(historyAdapter);
                                        } else {
                                            historyAdapter.RefreshItems(driverDeliveryHistory.getDriver_history());
                                        }
                                    } else {
                                        Log.d("HeroJongi", " no history found ");
                                        historyRecycleView.setVisibility(View.GONE);
                                        no_history_found.setVisibility(View.VISIBLE);
                                    }
                                }
                            }));
                        }
                    }

                }catch (Exception ex) {
                    Log.e("HeroJongi","Fail status update",ex);
                }
            }
        }).start();
    }

    public void getBalance(){
        if(balanceAdapter==null) {
            balanceProgressBar.setVisibility(View.VISIBLE);
        }
        new Thread(new Runnable() {
            public void run() {
                try {
                    final DriverBalance driverbalance=AppHelper.getRushUpClient().driverBalanceGet();
                    Log.d("HeroJongi","Delivery Success "+driverbalance.getDriver_balance().size());
                    if(driverbalance!=null && driverbalance.getDriver_balance()!=null){
                        runOnUiThread(new Thread(new Runnable() {
                            public void run() {
                               balanceProgressBar.setVisibility(View.GONE);
                                if(driverbalance.getDriver_balance().size()>0) {
                                    if (balanceAdapter == null) {
                                        List<DeliveryRequest> Balance=driverbalance.getDriver_balance();
                                        no_balance_found.setVisibility(View.GONE);
                                        Collections.sort(Balance, new Comparator<DeliveryRequest>() {
                                            @Override
                                            public int compare(DeliveryRequest o1, DeliveryRequest o2) {
                                                Date dateD=new Date(o2.getDeliveryDate()*1000);
                                                Date dateY=new Date(o1.getDeliveryDate()*1000);
                                                return  dateD.compareTo(dateY);
                                            }
                                        });
                                        balanceAdapter = new BalanceAdapter(Balance, InsideApp.this, rushUpDeliverySettings);
                                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InsideApp.this, OrientationHelper.VERTICAL, false);
                                        balanceRecyclerView.setLayoutManager(linearLayoutManager);
                                        balanceRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                        balanceRecyclerView.setAdapter(balanceAdapter);
                                    } else {
                                        balanceAdapter.RefreshItems(driverbalance.getDriver_balance());
                                    }
                                }
                                else{
                                   no_balance_found.setVisibility(View.VISIBLE);
                                }
                            }
                        }));
                    }

                }catch (Exception ex) {
                    Log.e("HeroJongi","Fail status update",ex);
                }
            }
        }).start();
    }

    public void activeButton(Button button) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.width = (int) convertDpToPixel(100, this);
        params.height = (int) convertDpToPixel(100, this);
        button.setLayoutParams(params);
    }

    public void inactiveButton(Button button) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.width = (int) convertDpToPixel(50, this);
        params.height = (int) convertDpToPixel(50, this);
        button.setLayoutParams(params);
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public void setAdapter() {
        List<Settings> settings_list = new ArrayList<>();
        settings_list.add(new Settings(getResources().getString(R.string.myaccount), null, Settings.HEADER));
        settings_list.add(new Settings(null, getResources().getString(R.string.mobile), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.email), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.password), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.notification), Settings.Notification));
        settings_list.add(new Settings(getResources().getString(R.string.more_info), null, Settings.HEADER));
        settings_list.add(new Settings(null, getResources().getString(R.string.support), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.privacy_policy), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.terms), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.license), Settings.OPTION));
        settings_list.add(new Settings(getResources().getString(R.string.account_actions), null, Settings.HEADER));
        if (details != null && details.getAttributes().getAttributes().get("email_verified").equalsIgnoreCase("false")) {
            settings_list.add(new Settings(null, getResources().getString(R.string.verify_email), Settings.OPTION));
        }
        if (details != null && details.getAttributes().getAttributes().get("phone_number_verified").equalsIgnoreCase("false")) {
            settings_list.add(new Settings(null, getResources().getString(R.string.verify_phone), Settings.OPTION));
        }
        settings_list.add(new Settings(null, getResources().getString(R.string.clear_history), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.logout), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.licenses), Settings.OPTION));
        adapter = new SettingsAdapter(this, settings_list,this,details);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        settings_recycler_view.setLayoutManager(linearLayoutManager);
        settings_recycler_view.setItemAnimator(new DefaultItemAnimator());
        settings_recycler_view.setAdapter(adapter);
    }

    Location _thisLocation,_prevLocation;
    int count=0;

    @Override
    public void onLocationChanged(Location location) {
        if(!isautomatic) {
            if (count == 0) {
                _thisLocation = location;
                _prevLocation = _thisLocation;
                count++;
            } else {
                _prevLocation = _thisLocation;
                _thisLocation = location;
            }
            if (location != null) {
                mLastLocation = location;
                AppHelper.sendDriverLocation(location);
                if (Driver == null) {
                    DropDriverOnMap(location.getLatitude(), location.getLongitude(), true);
                    if (drawPoly) {
                        APICallToDrawPolylinePickUpDriver();
                    }
                } else {
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            try {
                                float v = animation.getAnimatedFraction();
                                startPosition = new LatLng(_prevLocation.getLatitude(), _prevLocation.getLongitude());
                                endPosition = new LatLng(_thisLocation.getLatitude(), _thisLocation.getLongitude());
                                LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                                Driver.setPosition(newPosition);
                            } catch (Exception ex) {
                                // I don't care atm..
                            }
                        }
                    });

                    valueAnimator.start();
                }
                setBearing(_prevLocation, _thisLocation);
            }
        }
    }

    public void setBearing(Location _prevLocation,Location _thisLocation){
        if(map!=null){
        float bearing = _prevLocation.bearingTo(_thisLocation) ;
        Log.d("BEARING "," bearing is "+bearing);
        if(bearing!=0) {
            if (Driver != null) {
                Driver.setRotation(bearing);
                Driver.setAnchor(0.5f, 0.5f);
                Driver.setFlat(true);
                CameraPosition cameraPosition=null;
                if (PickupMarker != null && DropOffMarker != null && Driver.getPosition()!=null) {
                    cameraPosition = new CameraPosition.Builder().target(Driver.getPosition()).tilt(60).zoom(15).bearing(bearing).build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {}
                        @Override
                        public void onCancel() {}
                    });
               }
                else {
                    if (Driver.getPosition() != null) {
                        cameraPosition = new CameraPosition.Builder().target(Driver.getPosition()).tilt(0).zoom(15).bearing(bearing).build();
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                    }
                }
            }
        }
        else{
            if(Driver !=null && Driver.getPosition()!=null) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(Driver.getPosition()).tilt(0).zoom(15).bearing(bearing).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        }
        }

    }

    public void ZoomtoMyCurrentLocation(double latitudeValue, double longitudeValue) {
        if (map != null) {
            LatLng coordinate = new LatLng(latitudeValue, longitudeValue);
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
            map.animateCamera(location);
        }
    }

    public void DropDriverOnMap(double latitude, double longitude, boolean zoomtomylocation) {
        if (map != null) {
            Log.d("HeroJongi", "location Pick Up pin");
            MarkerOptions markerOpts = new MarkerOptions().position(new LatLng(latitude, longitude));
            markerOpts.icon(BitmapDescriptorFactory.fromResource(R.mipmap.cars));
            Driver = map.addMarker(markerOpts);
            ZoomtoMyCurrentLocation(latitude,longitude);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Utils.checkPlayServices(this);

        // Resuming the periodic location updates
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
        saveI.saveII(i[0]);
        SaveActiveDelivery(deliveryRequest);
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void startLocationUpdates() {
        Log.d("HeroJongi", "Here 1234");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Access_Location);
        } else {
            requestLocationUpdate();
        }

    }

    public void requestLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("HeroJongi ", "requestLocationUpdate");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);
        }

    }

    public void setCameraToInitial() {
        if(map!=null){
            LatLng latLng=new LatLng(33.888630,35.495480);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(10).tilt(0).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == Access_Location) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // handle deny case
            } else {
                requestLocationUpdate();
            }
        } else if (requestCode == PERMISSION_PHONE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }

            }
        }
    }

    @Override
    public void onNotificationRecieved(final PushType pushType,final Object object) {
        runOnUiThread(new Thread(new Runnable() {

            public void run() {
                switch (pushType) {
                    case delivery_update:
                       // show Dialog
                        DeliveryRequest deliveryRequests=(DeliveryRequest)object;
                        deliveryRequest=deliveryRequests;
                        if(deliveryRequest!=null) {
                           Dialog dialog=  Utils.showDriverDialog(InsideApp.this, deliveryRequests, rushUpDeliverySettings,driverStatusSharedPreference);
                           if(dialog!=null){
                               Utils.DeclareHandler(InsideApp.this,dialog,deliveryRequests);
                           }
                       }
                        break;
                }
            }

        }));
    }

    public void IntentsListener() {
        upperMenu.closeLayer(true);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getSerializableExtra("delivery_update") != null) {
                DeliveryRequest notification_object = (DeliveryRequest) intent.getSerializableExtra("delivery_update");
                deliveryRequest = notification_object;
                if (deliveryRequest != null) {
                    Dialog dialog = Utils.showDriverDialog(InsideApp.this, notification_object, rushUpDeliverySettings, driverStatusSharedPreference);
                    if (dialog != null) {
                        Utils.DeclareHandler(InsideApp.this, dialog, notification_object);
                    }
                }
            }
        }
    }

    @Override
    public void SaveActiveDelivery(DeliveryRequest deliveryRequest) {
        this.deliveryRequest=deliveryRequest;
        try {
            InternalStorage.writeObject(InsideApp.this,"ActiveDelivery",deliveryRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void PlotPins(DeliveryRequest deliveryRequest) {
        if (map!=null && deliveryRequest != null && deliveryRequest.getPickupLocation()!=null && deliveryRequest.getDropoffLocation()!=null && deliveryRequest.getDropoffLocation().getLatitude() != null && deliveryRequest.getDropoffLocation().getLongitude() != null && deliveryRequest.getPickupLocation().getLatitude()!= null && deliveryRequest.getPickupLocation().getLongitude() != null) {
            map.clear();
            LatLng pickup = new LatLng(Double.parseDouble(deliveryRequest.getPickupLocation().getLatitude()), Double.parseDouble(deliveryRequest.getPickupLocation().getLongitude()));
            LatLng dropoff = new LatLng(Double.parseDouble(deliveryRequest.getDropoffLocation().getLatitude()), Double.parseDouble(deliveryRequest.getDropoffLocation().getLongitude()));
            DropPickUpPin(pickup);
            DropDroOffPin(dropoff);
            if(mLastLocation!=null) {
                DropDriverOnMap(mLastLocation.getLatitude(), mLastLocation.getLongitude(), false);
            }
          //  APICallToDrawPolyline();
            APICallToDrawPolylinePickUpDriver();
        }
    }

    @Override
    public void showOptions() {
        options.setVisibility(View.VISIBLE);
        bottomMenu.setVisibility(View.GONE);
        bottomMenu.openLayer(false);
        bottomMenu.setSlidingEnabled(false);
    }

    @Override
    public void showBottomMenu(DeliveryRequest deliveryRequest) {
         options.setVisibility(View.GONE);
         bottomMenu.setVisibility(View.VISIBLE);
         bottomMenu.openLayer(true);
         bottomMenu.setSlidingEnabled(false);
        stimulate.setVisibility(View.VISIBLE);
    }

    @Override
    public void CheckPickUpCode(final  boolean isWrong) {
        runOnUiThread(new Thread(new Runnable() {
            public void run() {
                if (isWrong) {
                    Log.d("HeroJongi "," Check pick up wrong");
                    // show error and vibrate
                    insert_code_edittext.setError(getResources().getString(R.string.error_code));
                    Animation shake = AnimationUtils.loadAnimation(InsideApp.this, R.anim.shake);
                    insert_code_edittext.startAnimation(shake);
                } else {
                    // show drop off menu
                    Log.d("HeroJongi "," Check pick up not wrong");
                    isPickUp.saveisPickup(false);
                    package_pickup.setText(getResources().getString(R.string.package_dropoff));
                    if (deliveryRequest != null) {
                        FillUpBottomMenu(deliveryRequest, false);
                        insert_code_edittext.setText("");
                        insert_code_edittext.setVisibility(View.GONE);
                        insert_code.setVisibility(View.VISIBLE);
                    }
                }
            }
        }));
    }

    @Override
    public void CheckDropoffCode(final boolean isWrong) {
        runOnUiThread(new Thread(new Runnable() {
            public void run() {
                if (isWrong) {
                    //show error and vibrate
                    insert_code_edittext.setError(getResources().getString(R.string.error_code));
                    Animation shake = AnimationUtils.loadAnimation(InsideApp.this, R.anim.shake);
                    insert_code_edittext.startAnimation(shake);
                } else {
                    //reset
                    setRatingofDriver();
                    setCameraToInitial();
                    isPickUp.saveisPickup(true);
                    bottomMenu.setVisibility(View.GONE);
                    options.setVisibility(View.VISIBLE);
                    clearDelivery();
                    package_pickup.setText(getResources().getString(R.string.package_pickup));
                    insert_code_edittext.setText("");
                    insert_code_edittext.setVisibility(View.GONE);
                    insert_code.setVisibility(View.VISIBLE);
                }
            }
        }));
    }

    public void clearDelivery(){
        if(deliveryRequest!=null){
            DeliveryRequest processHistory=new DeliveryRequest(null,null,null,null,"","",Phonenumber,"",null,null,0,false,false,"","","");
            deliveryRequest=processHistory;
            SaveActiveDelivery(deliveryRequest);
            drawPoly=false;
            saveI.saveII(0);
            RemovePickUpPin();
            RemoveDropOffPin();
            RemoveDriverPin();
            if(drawPolyline!=null) {
                drawPolyline.RemovePolyline();
            }
            PickupMarker=null;
            DropOffMarker=null;
            Driver=null;
            stimulate.setVisibility(View.GONE);
        }
    }

    public void RemoveDriverPin(){
        if(Driver!=null){
            Driver.remove();
        }
    }

    public void RemovePickUpPin(){
        if(PickupMarker!=null){
            PickupMarker.remove();
        }
    }

    public void RemoveDropOffPin(){
        if(DropOffMarker!=null){
            DropOffMarker.remove();
        }
    }

    @Override
    public void FillUpBottomMenu(DeliveryRequest deliveryRequest,boolean isPickUp){
        if(deliveryRequest!=null) {
          if(isPickUp){
              if (deliveryRequest.getPickupLocation() != null && deliveryRequest.getPickupLocation().getName() != null && !deliveryRequest.getPickupLocation().getName().equalsIgnoreCase("")) {
                  pickupaddress.setText(deliveryRequest.getPickupLocation().getName());
              }
              if(deliveryRequest.getFrom()!=null && !deliveryRequest.getFrom().equalsIgnoreCase("")){
                  AppHelper.getPickUpOrDropOffImage(this,deliveryRequest.getFrom(),true,rushUpDeliverySettings,deliveryRequest);
              }
          }
          else{
              if (deliveryRequest.getDropoffLocation() != null && deliveryRequest.getDropoffLocation().getName() != null && !deliveryRequest.getDropoffLocation().getName().equalsIgnoreCase("")) {
                  pickupaddress.setText(deliveryRequest.getDropoffLocation().getName());
              }
              if(deliveryRequest.getTo()!=null && !deliveryRequest.getTo().equalsIgnoreCase("")){
                  AppHelper.getPickUpOrDropOffImage(this,deliveryRequest.getTo(),false,rushUpDeliverySettings,deliveryRequest);
              }
          }
        }
    }

    @Override
    public void displayPicture(File file) {
        if(user_image!=null && file!=null){
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            user_image.setImageBitmap(myBitmap);
        }
    }

    @Override
    public void openVerifyPhoneEmail(final Dialog dialog, String attribute) {
        AppHelper.getPool().getCognitoUserPool().getCurrentUser().getAttributeVerificationCodeInBackground(attribute, new VerificationHandler() {
            @Override
            public void onSuccess(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                Log.d("HeroJongi","Email send "+cognitoUserCodeDeliveryDetails.getDestination());
            }

            @Override
            public void onFailure(Exception exception) {
                Log.d("HeroJongi"," exception "+exception.getMessage());
                dialog.dismiss();
            }
        });
    }

    @Override
    public void UpdateAttribute(final String attribute_name, String attribute_value,final Dialog dialog) {
        CognitoUserAttributes updatedUserAttributes = new CognitoUserAttributes();
        updatedUserAttributes.addAttribute(attribute_name, attribute_value);
        if (AppHelper.getPool() != null && AppHelper.getPool().getCognitoUserPool().getCurrentUser() != null) {
            AppHelper.getPool().getCognitoUserPool().getCurrentUser().updateAttributesInBackground(updatedUserAttributes, new UpdateAttributesHandler() {
                @Override
                public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
                    AppHelper.getPool().getCognitoUserPool().getCurrentUser().getDetailsInBackground(detailsHandler);
                    settings_menu.setVisibility(View.GONE);
                    homelayout.setVisibility(View.VISIBLE);
                    if (attribute_name.equalsIgnoreCase("email")) {
                        adapter.AddVerifyEmail();
                    } else if (attribute_name.equalsIgnoreCase("phone_number")) {
                        adapter.AddVerifyPhoneNumber();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Exception exception) {
                    dialog.dismiss();
                }
            });
        }
    }



    @Override
    public void Verify_Email_Password(final String attribute, String code,final Dialog dialog) {
        if (AppHelper.getPool() != null && AppHelper.getPool().getCognitoUserPool().getCurrentUser() != null) {
            AppHelper.getPool().getCognitoUserPool().getCurrentUser().verifyAttributeInBackground(attribute, code, new GenericHandler() {
                @Override
                public void onSuccess() {
                    settings_menu.setVisibility(View.GONE);
                    homelayout.setVisibility(View.VISIBLE);
                    if (attribute.equalsIgnoreCase("email")) {
                        adapter.RemoveVerifyEmail();
                    } else if (attribute.equalsIgnoreCase("phone_number")) {
                        adapter.RemoveVerifyPhoneNumber();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Exception exception) {
                }
            });
        }
    }

    @Override
    public void ChangePassword(String newpassword, String oldPassword, final Dialog dialog) {
        if (AppHelper.getPool() != null && AppHelper.getPool().getCognitoUserPool().getCurrentUser() != null) {
            AppHelper.getPool().getCognitoUserPool().getCurrentUser().changePasswordInBackground(oldPassword, newpassword, new GenericHandler() {
                @Override
                public void onSuccess() {
                    settings_menu.setVisibility(View.GONE);
                    homelayout.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Exception exception) {
                    dialog.dismiss();
                }
            });
        }
    }
    @Override
    public void Signout() {
        if (AppHelper.getPool() != null && AppHelper.getPool().getCognitoUserPool().getCurrentUser() != null) {
            identityManager.signOut();
            Intent signout = new Intent(this, UserLogin.class);
            signout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(signout);
            finish();
        }
    }

    public String getDirectionUrl() {
        double pickupLatitude = PickupMarker.getPosition().latitude;
        double pickupLongitude = PickupMarker.getPosition().longitude;
        double dropOffLatitude = DropOffMarker.getPosition().latitude;
        double dropOffLongitude = DropOffMarker.getPosition().longitude;
        return Utils.getUrl(InsideApp.this, String.valueOf(pickupLatitude), String.valueOf(pickupLongitude), String.valueOf(dropOffLatitude), String.valueOf(dropOffLongitude));
    }

    public String getDirectionUrlDriver() {
        double pickupLatitude = PickupMarker.getPosition().latitude;
        double pickupLongitude = PickupMarker.getPosition().longitude;
        double driverLatitude = Driver.getPosition().latitude;
        double driverLongitude = Driver.getPosition().longitude;
        return Utils.getUrl(InsideApp.this, String.valueOf(driverLatitude), String.valueOf(driverLongitude), String.valueOf(pickupLatitude), String.valueOf(pickupLongitude));
    }


    public void APICallToDrawPolyline(){
        if(map!=null && DropOffMarker!=null && PickupMarker!=null) {
            String directionAPI = getDirectionUrl();
            drawPolyline.getDirectionFromDirectionApiServer(directionAPI, map);
        }
    }

    public void APICallToDrawPolylinePickUpDriver(){
        if(map!=null && PickupMarker!=null && Driver!=null){
            String pickUpDriver=getDirectionUrlDriver();
            driverPickupVolley.getDirectionFromDirectionApiServer(pickUpDriver,map);
        }
        else{
            drawPoly=true;
        }
    }



    public void DropPickUpPin(LatLng location){
        if(location!=null && location.latitude!=0 && location.longitude!=0 && map!=null) {
            MarkerOptions markerOpts = new MarkerOptions().position(location);
            markerOpts.icon(BitmapDescriptorFactory.fromResource(R.mipmap.greenpin));
            PickupMarker = map.addMarker(markerOpts);
        }
    }

    public void DropDroOffPin(LatLng location){
        if(location!=null && location.latitude!=0 && location.longitude!=0 && map!=null) {
            MarkerOptions markerOpts = new MarkerOptions().position(location);
            markerOpts.icon(BitmapDescriptorFactory.fromResource(R.mipmap.orangepin));
            DropOffMarker = map.addMarker(markerOpts);
        }
    }

    @Override
    public void onUserSignedIn() {
      Log.d("HeroJongi"," Here "+" on user sign in ");
    }

    @Override
    public void onUserSignedOut() {
        Log.d("HeroJongi"," Here "+" on user sign out ");
    }
}
