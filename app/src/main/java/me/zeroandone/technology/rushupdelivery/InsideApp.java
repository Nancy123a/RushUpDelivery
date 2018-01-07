package me.zeroandone.technology.rushupdelivery;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.view.View;
import android.widget.ImageView;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.zeroandone.technology.rushupdelivery.adapter.SettingsAdapter;
import me.zeroandone.technology.rushupdelivery.interfaces.RushUpDeliverySettings;
import me.zeroandone.technology.rushupdelivery.model.Driver;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.objects.PushType;
import me.zeroandone.technology.rushupdelivery.objects.Settings;
import me.zeroandone.technology.rushupdelivery.utils.AppHelper;
import me.zeroandone.technology.rushupdelivery.utils.Application;
import me.zeroandone.technology.rushupdelivery.utils.DrawPolylineVolley;
import me.zeroandone.technology.rushupdelivery.utils.InternalStorage;
import me.zeroandone.technology.rushupdelivery.utils.Utils;
import me.zeroandone.technology.rushupdelivery.widget.BalanceRecyclerView;
import me.zeroandone.technology.rushupdelivery.widget.HistoryRecycleView;


public class InsideApp extends AppCompatActivity implements RushUpDeliverySettings,OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    RelativeLayout homelayout, settings_menu, history_menu, balance_menu;
    TextView settings, history, balance, user_name;
    ImageView settings_close, history_close, balance_close, user_image;
    HistoryRecycleView historyRecycleView;
    BalanceRecyclerView balanceRecyclerView;
    RecyclerView settings_recycler_view;
    CognitoUserDetails details;
    String Username, Phonenumber;
    public static final int Access_Location = 70;
    LocationListener locationListener=this;
    Marker Driver;
    DrawPolylineVolley drawPolyline;
    Marker PickupMarker,DropOffMarker;
    DeliveryRequest deliveryRequest;
    RushUpDeliverySettings rushUpDeliverySettings=this;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 2000; // 5 sec
 //   private static int DISPLACEMENT = 2; // 10 meters


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

        if (Utils.checkPlayServices(InsideApp.this)) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
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
                    PlotPins(deliveryRequest);
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

    public void setAdapter() {
        List<Settings> settings_list = new ArrayList<>();
        settings_list.add(new Settings("My account", null, Settings.HEADER));
        settings_list.add(new Settings(null, "First Name", Settings.OPTION));
        settings_list.add(new Settings(null, "Last Name", Settings.OPTION));
        settings_list.add(new Settings(null, "Mobile number", Settings.OPTION));
        settings_list.add(new Settings(null, "Email", Settings.OPTION));
        settings_list.add(new Settings(null, "Password", Settings.OPTION));
        settings_list.add(new Settings(null, "Notification Sound", Settings.Notification));
        settings_list.add(new Settings("More Information", null, Settings.HEADER));
        settings_list.add(new Settings(null, "Support", Settings.OPTION));
        settings_list.add(new Settings(null, "Privacy Policy", Settings.OPTION));
        settings_list.add(new Settings(null, "Terms of service", Settings.OPTION));
        settings_list.add(new Settings(null, "Licenses", Settings.OPTION));
        settings_list.add(new Settings("Account actions", null, Settings.HEADER));
        if (details != null && details.getAttributes().getAttributes().get("email_verified").equalsIgnoreCase("false")) {
            settings_list.add(new Settings(null, "Verify Email", Settings.OPTION));
        }
        if (details != null && details.getAttributes().getAttributes().get("phone_number_verified").equalsIgnoreCase("false")) {
            settings_list.add(new Settings(null, "Verify Phone Number", Settings.OPTION));
        }
        settings_list.add(new Settings(null, "Clear History", Settings.OPTION));
        settings_list.add(new Settings(null, "Log Out", Settings.OPTION));
        settings_list.add(new Settings(null, "Add Account", Settings.OPTION));
        settings_list.add(new Settings(null, "Licenses", Settings.OPTION));
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
                ZoomtoMyCurrentLocation(location.getLatitude(), location.getLongitude());
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
                if(deliveryRequest!=null){
                    Utils.showDriverDialog(InsideApp.this,deliveryRequest,rushUpDeliverySettings);
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
                        DeliveryRequest deliveryRequest=(DeliveryRequest)object;
                       if(deliveryRequest!=null) {
                           Utils.showDriverDialog(InsideApp.this, deliveryRequest, rushUpDeliverySettings);
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
            LatLngBounds bounds = builder.build();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            int width = displayMetrics.widthPixels - 140;
            int height = displayMetrics.heightPixels;
            int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            map.animateCamera(cu);
        }
    }


}
