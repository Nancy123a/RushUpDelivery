package me.zeroandone.technology.rushupdelivery.utils;


import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.util.AbstractApplicationLifeCycleHelper;
import com.android.volley.RequestQueue;

import me.zeroandone.technology.rushupdelivery.interfaces.RushUpDeliverySettings;

//import com.amazonaws.mobile.auth.google.GoogleButton;
//import com.amazonaws.mobile.auth.google.GoogleSignInProvider;

public class Application extends android.app.Application {
    private static final String LOG_TAG = Application.class.getSimpleName();
    public static AWSConfiguration awsConfiguration;
    public static PinpointManager pinpointManager;
    RushUpDeliverySettings rushUpDeliverySettings;
    public static AuthUIConfiguration sAuthUIConfiguration =
            new AuthUIConfiguration.Builder()
                    .userPools(true)
                   // .signInButton(FacebookButton.class)
                    // .signInButton(GoogleButton.class)
                    .build();
    /**
     * To change the logo and background color, use the following API
     * <p>
     * AuthUIConfiguration sAuthUIConfiguration =
     * new AuthUIConfiguration.Builder()
     * .logoResId(R.drawable.image);
     * .backgroundColor(Color.BLACK);
     */
    private static Application mInstance;
    private RequestQueue requestQueue;
    private AbstractApplicationLifeCycleHelper applicationLifeCycleHelper;

    public static synchronized Application getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        mInstance=this;
        Log.d(LOG_TAG, "Application.onCreate - Initializing application...");
        super.onCreate();

        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();

        initializeApplication();
        Log.d(LOG_TAG, "Application.onCreate - Application initialized OK");
    }

    public void setConnectivityListener(InternetConnector_Receiver.ConnectivityReceiverListener listener) {
        InternetConnector_Receiver.connectivityReceiverListener = listener;
    }

    public RushUpDeliverySettings getRushUpDeliverySettings() {
        return rushUpDeliverySettings;
    }

    public void setRushUpDeliverySettings(RushUpDeliverySettings rushUpDeliverySettings) {
        this.rushUpDeliverySettings = rushUpDeliverySettings;
    }

    public RequestQueue getVolleyRequestQueue() {
        return requestQueue;
    }


    private void initializeApplication() {
        awsConfiguration = new AWSConfiguration(this);

        if (IdentityManager.getDefaultIdentityManager() == null) {
            final IdentityManager identityManager = new IdentityManager(getApplicationContext(), awsConfiguration);
            IdentityManager.setDefaultIdentityManager(identityManager);
        }


      //  FacebookSignInProvider.setPermissions("public_profile");

        // Add Facebook as an SignIn Provider.
      //  IdentityManager.getDefaultIdentityManager().addSignInProvider(FacebookSignInProvider.class);

        //  GoogleSignInProvider.setPermissions(Scopes.EMAIL);

        // Add Google as an SignIn Provider.
        //  IdentityManager.getDefaultIdentityManager().addSignInProvider(GoogleSignInProvider.class);

        // Add UserPools as an SignIn Provider.
        IdentityManager.getDefaultIdentityManager().addSignInProvider(CognitoUserPoolsSignInProvider.class);


        try {
            final PinpointConfiguration config =
                    new PinpointConfiguration(this,
                            IdentityManager.getDefaultIdentityManager().getCredentialsProvider(),
                            awsConfiguration);
            Application.pinpointManager = new PinpointManager(config);
        } catch (final AmazonClientException ex) {
            Log.e(LOG_TAG, "Unable to initialize PinpointManager. " + ex.getMessage(), ex);
        }

        // The Helper registers itself to receive application lifecycle events when it is constructed.
        // A reference is kept here in order to pass through the onTrimMemory() call from
        // the Application class to properly track when the application enters the background.
        applicationLifeCycleHelper = new AbstractApplicationLifeCycleHelper(this) {
            @Override
            protected void applicationEnteredForeground() {
                Application.pinpointManager.getSessionClient().startSession();
                // handle any events that should occur when your app has come to the foreground...
            }

            @Override
            protected void applicationEnteredBackground() {
                Log.d(LOG_TAG, "Detected application has entered the background.");
                Application.pinpointManager.getSessionClient().stopSession();
                Application.pinpointManager.getAnalyticsClient().submitEvents();
                // handle any events that should occur when your app has gone into the background...
            }
        };
    }


    @Override
    public void onTrimMemory(final int level) {
        Log.d(LOG_TAG, "onTrimMemory " + level);
        applicationLifeCycleHelper.handleOnTrimMemory(level);
        super.onTrimMemory(level);
    }



}
