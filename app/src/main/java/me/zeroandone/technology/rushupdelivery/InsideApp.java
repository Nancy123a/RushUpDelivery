package me.zeroandone.technology.rushupdelivery;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;

import com.google.android.gms.location.LocationListener;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zeroandone.technology.rushupdelivery.adapter.SettingsAdapter;
import me.zeroandone.technology.rushupdelivery.interfaces.RushUpDeliverySettings;
import me.zeroandone.technology.rushupdelivery.model.Driver;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryStatus;
import me.zeroandone.technology.rushupdelivery.objects.DriverStatus;
import me.zeroandone.technology.rushupdelivery.objects.DriverStatusRequest;
import me.zeroandone.technology.rushupdelivery.objects.PushType;
import me.zeroandone.technology.rushupdelivery.objects.Settings;
import me.zeroandone.technology.rushupdelivery.utils.AppHelper;
import me.zeroandone.technology.rushupdelivery.utils.Application;
import me.zeroandone.technology.rushupdelivery.utils.DrawPolylineVolley;
import me.zeroandone.technology.rushupdelivery.utils.DriverStatusSharedPreference;
import me.zeroandone.technology.rushupdelivery.utils.InternalStorage;
import me.zeroandone.technology.rushupdelivery.utils.Utils;
import me.zeroandone.technology.rushupdelivery.utils.isPickUp;
import me.zeroandone.technology.rushupdelivery.widget.BalanceRecyclerView;
import me.zeroandone.technology.rushupdelivery.widget.HistoryRecycleView;


public class InsideApp extends AppCompatActivity implements RushUpDeliverySettings,OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    RelativeLayout homelayout, settings_menu, history_menu, balance_menu;
    TextView settings, history, balance, user_name,insert_code,package_pickup;
    ImageView settings_close, history_close, balance_close, user_image;
    EditText insert_code_edittext;
    HistoryRecycleView historyRecycleView;
    BalanceRecyclerView balanceRecyclerView;
    RecyclerView settings_recycler_view;
    CognitoUserDetails details;
    String Username, Phonenumber;
    LinearLayout options;
    isPickUp isPickUp;
    DriverStatusSharedPreference driverStatusSharedPreference;
    public static final int Access_Location = 70;
    LocationListener locationListener=this;
    Marker Driver;
    FrameLayout frameLayout;
    DrawPolylineVolley drawPolyline;
    Marker PickupMarker,DropOffMarker;
    DeliveryRequest deliveryRequest;
    RushUpDeliverySettings rushUpDeliverySettings=this;
    SlidingLayer bottomMenu;
    Button off,on;
    TextView pickupname,pickupaddress,Boy_photoname;
    CircleImageView boy_photo;


    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 60*1000; // 1min


    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_app);

        Application.getInstance().setRushUpDeliverySettings(rushUpDeliverySettings);
        drawPolyline = new DrawPolylineVolley(this);
        deliveryRequest=new DeliveryRequest();
        isPickUp=new isPickUp(this);


        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        settings = (TextView) findViewById(R.id.settings);
        homelayout = (RelativeLayout) findViewById(R.id.homelayout);
        settings_menu = (RelativeLayout) findViewById(R.id.settings_menu);
        history = (TextView) findViewById(R.id.history);
        balance = (TextView) findViewById(R.id.balance);
        history_menu = (RelativeLayout) findViewById(R.id.history_menu);
        settings_close = (ImageView) findViewById(R.id.settings_close);
        history_close = (ImageView) findViewById(R.id.history_close);
        balance_close = (ImageView) findViewById(R.id.balance_close);
        user_image = (ImageView) findViewById(R.id.userimage);
        user_name = (TextView) findViewById(R.id.username);
        balance_menu = (RelativeLayout) findViewById(R.id.balance_menu);
        historyRecycleView = (HistoryRecycleView) findViewById(R.id.historyRecycleView);
        balanceRecyclerView = (BalanceRecyclerView) findViewById(R.id.balanceRecycleView);
        settings_recycler_view = (RecyclerView) findViewById(R.id.settings_recycle_view);
        bottomMenu=(SlidingLayer) findViewById(R.id.slidingLayer2);
        options=(LinearLayout) findViewById(R.id.relative);
        frameLayout=(FrameLayout) findViewById(R.id.maplayout);
        off=(Button) findViewById(R.id.off);
        on=(Button) findViewById(R.id.on);
        pickupaddress=(TextView) findViewById(R.id.addresspickup);
        pickupname=(TextView) findViewById(R.id.pickupname);
        boy_photo=(CircleImageView) findViewById(R.id.boy_photo);
        Boy_photoname=(TextView) findViewById(R.id.driver_xxphoto);
        insert_code=(TextView) findViewById(R.id.insertcode);
        insert_code_edittext=(EditText) findViewById(R.id.edittext_insertcode);
        package_pickup=(TextView) findViewById(R.id.package_pickup);

        driverStatusSharedPreference=new DriverStatusSharedPreference(this);

        if(driverStatusSharedPreference.getStatus()!=null && !driverStatusSharedPreference.getStatus().equalsIgnoreCase("")){
            if(driverStatusSharedPreference.getStatus().equalsIgnoreCase("on") || driverStatusSharedPreference.getStatus().equalsIgnoreCase("occupied")){
                activeButton(on);
                inactiveButton(off);
            }
            else {
                activeButton(off);
                inactiveButton(on);
            }
        }
        else{
            activeButton(off);
            inactiveButton(on);
        }


        if (AppHelper.getPool() != null && AppHelper.getPool().getCognitoUserPool().getCurrentUser() != null) {
            AppHelper.getPool().getCognitoUserPool().getCurrentUser().getDetailsInBackground(detailsHandler);
        }

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        settings.setOnClickListener(this);
        history.setOnClickListener(this);
        balance.setOnClickListener(this);
        settings_close.setOnClickListener(this);
        history_close.setOnClickListener(this);
        balance_close.setOnClickListener(this);
        off.setOnClickListener(this);
        on.setOnClickListener(this);
        insert_code.setOnClickListener(this);

        if (Utils.checkPlayServices(InsideApp.this)) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }

        insert_code_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    Log.d("HeroJongi","editText"+insert_code_edittext.getText().toString());
                    if ( !insert_code_edittext.getText().toString().equalsIgnoreCase("")) {
                      if(isPickUp.getisPickUp()) {
                          Log.d("HeroJongi","ItsPickup");
                          AppHelper.CheckCode(deliveryRequest,insert_code_edittext.getText().toString(), DeliveryStatus.with_delivery, true, rushUpDeliverySettings);
                      }
                      else{
                          Log.d("HeroJongi","ItsDropOffup");
                          AppHelper.CheckCode(deliveryRequest,insert_code_edittext.getText().toString(),DeliveryStatus.delivered,false,rushUpDeliverySettings);
                      }
                    }
                    else{
                        // hide kwyboard
                        Log.d("HeroJongi","editText is empty "+insert_code_edittext.getText().toString());

                        insert_code_edittext.setError(getResources().getString(R.string.please_insert_code));
                    }
                }

                return false;
            }
        });

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
            details = cognitoUserDetails;
            Username = AppHelper.getPool().getCognitoUserPool().getCurrentUser().getUserId();
            Phonenumber = cognitoUserDetails.getAttributes().getAttributes().get("phone_number");
            ReadSettings();
            SharedPreferences settingss = getSharedPreferences("showpopup", 0);
            SharedPreferences.Editor editor = settingss.edit();
            if (!settingss.contains("firsttime")) {
                editor.putBoolean("firsttime", true);
                editor.apply();
                try {
                    String Token = InternalStorage.readFCMToken(InsideApp.this, "FCMToken");
                    AppHelper.SaveDriverFCMTokenToCloud(Token, Phonenumber);

                } catch (FileNotFoundException e) {
                    Log.d("HeroJongi", "message 1" + e.getMessage());
                } catch (IOException e) {
                    Log.d("HeroJongi", "message 2" + e.getMessage());
                } catch (ClassNotFoundException e) {
                    Log.d("HeroJongi", "message 3" + e.getMessage());
                }

            }
            setAdapter();
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
                DeliveryRequest deliveryRequest=(DeliveryRequest)data;
                this.deliveryRequest=deliveryRequest;
             // plot the pins
                if(deliveryRequest!=null) {
                    Log.d("HeroJongi ","Read settings ");
                    if(deliveryRequest.getPickupLocation()!=null && deliveryRequest.getDropoffLocation()!=null) {
                        options.setVisibility(View.GONE);
                        PlotPins(deliveryRequest);
                        showBottomMenu(deliveryRequest);
                        if(isPickUp.getisPickUp()){
                            FillUpBottomMenu(deliveryRequest,true);
                        }
                        else{
                            FillUpBottomMenu(deliveryRequest,false);
                        }
                    }


                }
            }
        }
            catch (IOException e) {
                Log.d("HeroJongi", "error" + e.getMessage());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                Log.d("HeroJongi", "error" + e.getMessage());
                e.printStackTrace();
            }
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                historyRecycleView.setAdapter(historyRecycleView);
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
                balanceRecyclerView.setAdapter(balanceRecyclerView);
                break;


        }
    }

    public void activeButton(Button button){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.width =(int) convertDpToPixel(90,this);
        params.height=(int) convertDpToPixel(90,this);
        button.setLayoutParams(params);
    }

    public void inactiveButton(Button button){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button.getLayoutParams();
        params.width =(int) convertDpToPixel(50,this);
        params.height=(int) convertDpToPixel(50,this);
        button.setLayoutParams(params);
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
    public void setAdapter() {
        List<Settings> settings_list = new ArrayList<>();
        settings_list.add(new Settings(getResources().getString(R.string.myaccount), null, Settings.HEADER));
        settings_list.add(new Settings(null, getResources().getString(R.string.first_name), Settings.OPTION));
        settings_list.add(new Settings(null,  getResources().getString(R.string.lastname), Settings.OPTION));
        settings_list.add(new Settings(null,  getResources().getString(R.string.mobile), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.email), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.password), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.notification), Settings.Notification));
        settings_list.add(new Settings(getResources().getString(R.string.more_info), null, Settings.HEADER));
        settings_list.add(new Settings(null, getResources().getString(R.string.support), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.privacy_policy), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.terms), Settings.OPTION));
        settings_list.add(new Settings(null,getResources().getString(R.string.license) , Settings.OPTION));
        settings_list.add(new Settings(getResources().getString(R.string.account_actions), null, Settings.HEADER));
        if (details != null && details.getAttributes().getAttributes().get("email_verified").equalsIgnoreCase("false")) {
            settings_list.add(new Settings(null, getResources().getString(R.string.verify_email), Settings.OPTION));
        }
        if (details != null && details.getAttributes().getAttributes().get("phone_number_verified").equalsIgnoreCase("false")) {
            settings_list.add(new Settings(null, getResources().getString(R.string.verify_phone), Settings.OPTION));
        }
        settings_list.add(new Settings(null, getResources().getString(R.string.clear_history), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.logout), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.add_account), Settings.OPTION));
        settings_list.add(new Settings(null, getResources().getString(R.string.licenses), Settings.OPTION));
        SettingsAdapter adapter = new SettingsAdapter(this, settings_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        settings_recycler_view.setLayoutManager(linearLayoutManager);
        settings_recycler_view.setItemAnimator(new DefaultItemAnimator());
        settings_recycler_view.setAdapter(adapter);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null) {
            mLastLocation = location;
            Log.d("HeroJongi", mLastLocation + "");
            AppHelper.sendDriverLocation(location);
            if (Driver == null) {
                DropDriverOnMap(location.getLatitude(), location.getLongitude(),true);
            } else {
                Driver.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                ZoomCameraToBothPins();
            }
        }
    }

    public void ZoomtoMyCurrentLocation(double latitudeValue,double longitudeValue){
        if(map!=null) {
            LatLng coordinate = new LatLng(latitudeValue,longitudeValue);
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
            map.animateCamera(location);
        }
    }

    public void DropDriverOnMap(double latitude,double longitude,boolean zoomtomylocation){
        if( map!=null) {
            Log.d("HeroJongi","location "+"Pick Up pin");
            MarkerOptions markerOpts = new MarkerOptions().position(new LatLng(latitude,longitude));
            markerOpts.icon(BitmapDescriptorFactory.fromResource(R.mipmap.bike));
            Driver = map.addMarker(markerOpts);
            if(zoomtomylocation) {
                ZoomtoMyCurrentLocation(latitude, longitude);
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
            startLocationUpdates();
            IntentsListener();
    }

    private void IntentsListener() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getSerializableExtra("delivery_update") != null) {
                DeliveryRequest deliveryRequest=(DeliveryRequest)intent.getSerializableExtra("delivery_update");
                if(deliveryRequest!=null) {
                    Dialog dialog=  Utils.showDriverDialog(InsideApp.this, deliveryRequest, rushUpDeliverySettings,driverStatusSharedPreference);
                    if(dialog!=null){
                        Utils.DeclareHandler(InsideApp.this,dialog,deliveryRequest);
                    }
                }
            }
        }
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
        if (mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
        SaveActiveDelivery(deliveryRequest);
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void startLocationUpdates() {
        Log.d("HeroJongi","Here 1234");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Access_Location);
        } else {
            requestLocationUpdate();
        }

    }

    public void requestLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           Log.d("HeroJongi ","requestLocationUpdate");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);
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
            }
            else {
               requestLocationUpdate();
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
        if (map!=null && deliveryRequest != null && deliveryRequest.getPickupLocation()!=null && deliveryRequest.getDropoffLocation()!=null && deliveryRequest.getDropoffLocation().getLatitude() != null && deliveryRequest.getDropoffLocation().getLongitude() != null && deliveryRequest.getPickupLocation().getLatitude()
                != null && deliveryRequest.getPickupLocation().getLongitude() != null) {
            map.clear();
            LatLng pickup = new LatLng(Double.parseDouble(deliveryRequest.getPickupLocation().getLatitude()), Double.parseDouble(deliveryRequest.getPickupLocation().getLongitude()));
            LatLng dropoff = new LatLng(Double.parseDouble(deliveryRequest.getDropoffLocation().getLatitude()), Double.parseDouble(deliveryRequest.getDropoffLocation().getLongitude()));
            DropPickUpPin(pickup);
            DropDroOffPin(dropoff);
            if(mLastLocation!=null) {
                DropDriverOnMap(mLastLocation.getLatitude(), mLastLocation.getLongitude(), false);
            }
            ZoomCameraToBothPins();
            APICallToDrawPolyline();
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
         isPickUp.saveisPickup(true);
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
                    isPickUp.saveisPickup(false);
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

            RemovePickUpPin();
            RemoveDropOffPin();
            RemoveDriverPin();
            if(drawPolyline!=null) {
                drawPolyline.RemovePolyline();
            }
            PickupMarker=null;
            DropOffMarker=null;
            Driver=null;
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
            if(isPickUp) {
                if (deliveryRequest.getPickupName() != null && !deliveryRequest.getPickupName().equalsIgnoreCase("")) {
                    pickupname.setText(deliveryRequest.getPickupName());
                    boy_photo.setVisibility(View.GONE);
                    Boy_photoname.setVisibility(View.VISIBLE);
                    char firstletter = Character.toUpperCase(deliveryRequest.getPickupName().charAt(0));
                    Boy_photoname.setText(String.valueOf(firstletter));
                }
                if (deliveryRequest.getPickupLocation() != null && deliveryRequest.getPickupLocation().getName() != null && !deliveryRequest.getPickupLocation().getName().equalsIgnoreCase("")) {
                    pickupaddress.setText(deliveryRequest.getPickupLocation().getName());
                }
            }
            else{
                if (deliveryRequest.getDropoffName()!= null && !deliveryRequest.getDropoffName().equalsIgnoreCase("")) {
                    pickupname.setText(deliveryRequest.getDropoffName());
                    boy_photo.setVisibility(View.GONE);
                    Boy_photoname.setVisibility(View.VISIBLE);
                    char firstletter = Character.toUpperCase(deliveryRequest.getDropoffName().charAt(0));
                    Boy_photoname.setText(String.valueOf(firstletter));
                }
                if (deliveryRequest.getDropoffLocation() != null && deliveryRequest.getDropoffLocation().getName() != null && !deliveryRequest.getDropoffLocation().getName().equalsIgnoreCase("")) {
                    pickupaddress.setText(deliveryRequest.getDropoffLocation().getName());
                }
            }
        }
    }

    public String getDirectionUrl() {
        double pickupLatitude = PickupMarker.getPosition().latitude;
        double pickupLongitude = PickupMarker.getPosition().longitude;
        double dropOffLatitude = DropOffMarker.getPosition().latitude;
        double dropOffLongitude = DropOffMarker.getPosition().longitude;
        return Utils.getUrl(InsideApp.this, String.valueOf(pickupLatitude), String.valueOf(pickupLongitude),
                String.valueOf(dropOffLatitude), String.valueOf(dropOffLongitude));
    }


    public void APICallToDrawPolyline(){
        if(map!=null && DropOffMarker!=null && PickupMarker!=null) {
            String directionAPI = getDirectionUrl();
            drawPolyline.getDirectionFromDirectionApiServer(directionAPI, map);
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

    private void ZoomCameraToBothPins() {
        if(map!=null) {
         LatLngBounds.Builder builder = new LatLngBounds.Builder();
          if(PickupMarker!=null) {
           builder.include(PickupMarker.getPosition());
           }
           if(DropOffMarker!=null) {
            builder.include(DropOffMarker.getPosition());
          }
          if(Driver!=null) {
           builder.include(Driver.getPosition());
           }
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
        }
    }


}
