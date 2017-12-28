package me.zeroandone.technology.rushupdelivery.utils;

import android.content.Context;
import android.util.Log;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobile.content.UserFileManager;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.regions.Regions;


public class AppHelper{
    public static final String S3_PREFIX_PRIVATE = "private/";
    //google key
    public static final String Google_key = "6488603275-vbipema563ljm0sfjitl87hg3ii63u1k.apps.googleusercontent.com";
    //User / Federated Pool
    public static final String userPoolId = "eu-west-2_9Rfg3SRNy";
    public static final String federatedPoolId = "eu-west-2:9ee028f7-5d7b-4bb5-9406-769a7dc72abd";
    /**
     * Add you app id
     */
    public static final String clientId = "3i6shcjg4ef7t1cu5gq5q7vkgk";
    /**
     * App secret associated with your app id - if the App id does not have an associated App secret,
     * set the App secret to null.
     * e.g. clientSecret = null;
     */
    public static final String clientSecret = "dd2gpb6sdv1fsnoeno941c3n7gbk4mn291o2phqig39812ii2kq";
    public static final String BUCKET_NAME = "rushup-userfiles-mobilehub-197211853";

    public static final String s3_base_url = "https://s3.eu-west-2.amazonaws.com/";


    public static final int SUCCESS_RESULT = 0;

    public static final int FAILURE_RESULT = 1;

    public static final String PACKAGE_NAME = "me.zeroandone.technology.rushup";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final String LOCATION_DATA_AREA = PACKAGE_NAME + ".LOCATION_DATA_AREA";
    public static final String LOCATION_DATA_CITY = PACKAGE_NAME + ".LOCATION_DATA_CITY";
    public static final String LOCATION_DATA_STREET = PACKAGE_NAME + ".LOCATION_DATA_STREET";


    private static final String LOG_TAG = AppHelper.class.getSimpleName();
    /**
     * Set Your User Pools region.
     * e.g. if your user pools are in US East (N Virginia) then set cognitoRegion = Regions.US_EAST_1.
     */
    public static final Regions cognitoRegion = Regions.EU_WEST_2;
    // User details from the service
    public static CognitoUserPoolsSignInProvider cognitoUserPoolsSignInProvider;
    private static AppHelper appHelper;
    //private static CognitoUserPool userPool;
    private static CognitoDevice newDevice;
    private static CognitoUserSession currSession;

    UserFileManager User_File;


    public static void init() {

        if (appHelper == null) {
            appHelper = new AppHelper();
        }

    }

    public static CognitoDevice getNewDevice() {
        return newDevice;
    }

    public static void setNewDevice(CognitoDevice newDevice) {
        AppHelper.newDevice = newDevice;
    }

    public static void initializeCognitoPool(Context context) {
        if (cognitoUserPoolsSignInProvider == null) {
            cognitoUserPoolsSignInProvider = new CognitoUserPoolsSignInProvider();
            cognitoUserPoolsSignInProvider.initialize(context, new AWSConfiguration(context));
        }
    }

    public static void federateWithProvider() {
        if(IdentityManager.getDefaultIdentityManager().getCurrentIdentityProvider() != null)
           Log.w(LOG_TAG,"Identity Manager already federated , confirm you want to change that");
        getPool().refreshUserSignInState();
        IdentityManager.getDefaultIdentityManager().federateWithProvider(getPool());
    }

    public static CognitoUserPoolsSignInProvider getPool() {
        if(IdentityManager.getDefaultIdentityManager().getCurrentIdentityProvider() == null)
            return cognitoUserPoolsSignInProvider;
        else {
            return (CognitoUserPoolsSignInProvider) IdentityManager.getDefaultIdentityManager().getCurrentIdentityProvider();
        }
    }

    public static CognitoUserSession getCurrSession() {
        return currSession;
    }

    public static void setCurrSession(CognitoUserSession session) {
        currSession = session;
    }

    public static String formatException(Exception exception) {
        String formattedString = "Internal Error";
        Log.e("App Error", exception.toString());
        Log.getStackTraceString(exception);

        String temp = exception.getMessage();

        if (temp != null && temp.length() > 0) {
            formattedString = temp.split("\\(")[0];
            if (temp != null && temp.length() > 0) {
                return formattedString;
            }
        }

        return formattedString;
    }




}