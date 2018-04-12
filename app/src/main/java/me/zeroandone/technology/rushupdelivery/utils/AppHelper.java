package me.zeroandone.technology.rushupdelivery.utils;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.amazonaws.api.idrevskyx266.RushupClient;
import com.amazonaws.content.ContentDownloadPolicy;
import com.amazonaws.content.ContentItem;
import com.amazonaws.content.ContentProgressListener;
import com.amazonaws.content.UserFileManager;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.userpools.CognitoUserPoolsSignInProvider;
import com.amazonaws.mobile.config.AWSConfiguration;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;

import java.io.File;

import me.zeroandone.technology.rushupdelivery.interfaces.RushUpDeliverySettings;
import me.zeroandone.technology.rushupdelivery.model.DriverLocationRequest;
import me.zeroandone.technology.rushupdelivery.model.TokenRequest;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryPickUpDropoff;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryStatus;
import me.zeroandone.technology.rushupdelivery.objects.DriverCode;
import me.zeroandone.technology.rushupdelivery.objects.DriverStatus;
import me.zeroandone.technology.rushupdelivery.objects.DriverStatusRequest;


public class AppHelper{
    public static final String S3_PREFIX_PRIVATE = "private/";

    private static final String LOG_TAG = AppHelper.class.getSimpleName();



    // User details from the service
    public static CognitoUserPoolsSignInProvider cognitoUserPoolsSignInProvider;
    private static AppHelper appHelper;
    //private static CognitoUserPool userPool;
    private static CognitoDevice newDevice;
    private static CognitoUserSession currSession;



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
           Log.d(LOG_TAG,"Identity Manager already federated , confirm you want to change that");
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


    public static RushupClient getRushUpClient() {
            IdentityManager identityManager=IdentityManager.getDefaultIdentityManager();
            ApiClientFactory factory = new ApiClientFactory().credentialsProvider(identityManager.getUnderlyingProvider());
            return  factory.build(RushupClient.class);
    }

    public static RushupClient getRushUpClientUnauthenticated() {
            ApiClientFactory factory = new ApiClientFactory();
            return factory.build(RushupClient.class);
    }


    public static void SaveDriverFCMTokenToCloud(final Context context, final String Username, final RushUpDeliverySettings rushUpDeliverySettings){
            // do api call
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final String Token = InternalStorage.readFCMToken(context,  "FCMToken");
                        TokenRequest token = new TokenRequest(Token);
                        RushupClient rushupClient=AppHelper.getRushUpClient();
                        if (rushupClient != null) {
                            rushupClient.driverTokenPost(token);
                            AddDriverToDriversGroup(Username,rushUpDeliverySettings);
                        }
                        Log.d("HeroJongi", "Success token post");
                    } catch (Exception ex) {
                        Log.d("HeroJongi", "Error in token" + ex.getMessage());
                    }
                }
            }).start();

    }

    public static void AddDriverToDriversGroup(final String username, final RushUpDeliverySettings rushUpDeliverySettings){
        new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DriverCode driverCode = new DriverCode(username, "");
                        AppHelper.getRushUpClient().driverGroupAssignPost(driverCode);
                        Log.d("HeroJongi"," driver code "+username);
                    } catch (Exception e) {
                        Log.d("HeroJongi", " Fail add driver to group " + e.getMessage());
                    }
                }
            }).start();
    }


    public static void UpdateStatusofDriver(final DriverStatus status){
        if(AppHelper.getRushUpClient()!=null) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Log.d("HeroJongi"," Update driver status success");
                        AppHelper.getRushUpClient().driverStatusPut(new DriverStatusRequest(status));
                    }
                    catch (Exception ex) {
                        Log.d("HeroJongi","Error in token",ex);
                    }
                }
            }).start();
        }
    }


    public static void sendDriverLocation(final Location location) {
        new Thread(new Runnable() {
            public void run() {
                if (location != null) {
                    try {
                        DriverLocationRequest driver = new DriverLocationRequest(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        getRushUpClient().driverLocationPut(driver);
                        Log.d("HeroJongi", "Driver Location post success");
                    } catch (Exception ex) {
                        Log.d("HeroJongi", "Driver Location post error " + ex.getMessage());
                    }
                }
            }
        }).start();
    }

    public static void UpdateDriverStatus(final DriverStatus status){
        new Thread(new Runnable() {
            public void run() {
                if (status != null) {
                    try {
                        DriverStatusRequest driverStatusRequest=new DriverStatusRequest(status);
                        getRushUpClient().driverStatusPut(driverStatusRequest);
                        Log.d("HeroJongi", "Driver Sttus updated");
                    } catch (Exception ex) {
                        Log.d("HeroJongi", "Driver Location post error " + ex.getMessage());
                    }
                }
            }
        }).start();
    }


    public static void assignDeliveryToDriver(final DeliveryRequest deliveryRequest){
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("HeroJongi"," assignDeliveryToDriver "+deliveryRequest.getId());
                  getRushUpClient().deliveryDeliveryIdAssignPut(deliveryRequest.getId());
                }catch (Exception ex) {
                    Log.e("HeroJongi","Fail status update",ex);
                }
            }
        }).start();
    }

    public  static void CheckCode(final DeliveryRequest deliveryRequest, final String code , final DeliveryStatus status, final boolean isPickup, final RushUpDeliverySettings rushUpDeliverySettings){
        new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("HeroJongi "," its correct");
                    String Status=String.valueOf(status);
                    DeliveryPickUpDropoff deliveryPickUpDropoff=new DeliveryPickUpDropoff(code,Status);
                    getRushUpClient().deliveryDeliveryIdPickupdropoffPut(deliveryRequest.getId(),deliveryPickUpDropoff);
                    if(rushUpDeliverySettings!=null) {
                        if (isPickup) {
                            rushUpDeliverySettings.CheckPickUpCode(false);
                        } else {
                            rushUpDeliverySettings.CheckDropoffCode(false);
                        }
                    }
                }catch (Exception ex) {
                    Log.d("HeroJongi "," its wrong "+ex.getMessage());
                   if(ex.getMessage().contains("Wrong Code")){
                       Log.d("HeroJongi "," its wrong");
                    if(rushUpDeliverySettings!=null){
                        if(isPickup){
                            rushUpDeliverySettings.CheckPickUpCode(true);
                        }
                        else{
                           rushUpDeliverySettings.CheckDropoffCode(true);
                        }
                    }
                   }
                }
            }
        }).start();
    }


    public static void uploadDownloadPicture(final boolean upload, final Context context, final File file, IdentityManager identityManager, final RushUpDeliverySettings rushUpSettings){
        final String identityId = identityManager.getCachedUserID();
        final String prefix =  S3_PREFIX_PRIVATE + identityId + "/";
        Log.d("HeroJongi"," perfix "+prefix);
        new UserFileManager.Builder()
                .withContext(context)
                .withIdentityManager(IdentityManager.getDefaultIdentityManager())
                .withS3ObjectDirPrefix(prefix)
                .withLocalBasePath(context.getFilesDir().getAbsolutePath())
                .withAWSConfiguration(new AWSConfiguration(context))
                .build(new UserFileManager.BuilderResultHandler() {
                    @Override
                    public void onComplete( UserFileManager userFileManager) {
                        if(upload){
                            Log.d("HeroJongi","on Upload");
                            uploadPicture(userFileManager,file,rushUpSettings);
                        }
                        else{
                            // download
                            Log.d("HeroJongi","on Download");
                            downloadPicture(userFileManager,rushUpSettings);
                        }
                    }
                });

    }

    public static void uploadPicture(final UserFileManager userFileManager, File file, final RushUpDeliverySettings rushUpSettings){
        if(userFileManager!=null && file!=null){
            Log.d("HeroJongi","upload "+userFileManager+"  "+file+"  "+rushUpSettings+"  "+file.getName());
            userFileManager.uploadContent(file, file.getName(), new ContentProgressListener() {
                @Override
                public void onSuccess(ContentItem contentItem) {
                    Log.d("HeroJongi"," upload on success");
                    if(rushUpSettings!=null){
                        rushUpSettings.deleteFile();
                    }
                    downloadPicture(userFileManager,rushUpSettings);
                }

                @Override
                public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {
                    Log.d("HeroJongi"," upload on progress");
                }

                @Override
                public void onError(String filePath, Exception ex) {
                    Log.d("HeroJongi","Picture upload on error");
                }
            });
        }
    }

    public static void downloadPicture(UserFileManager userFileManager,final RushUpDeliverySettings rushUpSettings){
        if(userFileManager!=null && rushUpSettings!=null) {
            userFileManager.getContent("driverPicture",0,
                    ContentDownloadPolicy.DOWNLOAD_IF_NEWER_EXIST, true, new ContentProgressListener() {
                        @Override
                        public void onSuccess(ContentItem contentItem) {
                            Log.d("HeroJongi","on Download success ");
                            if(contentItem!=null && contentItem.getFile()!=null){
                                rushUpSettings.displayPicture(contentItem.getFile());
                            }
                        }

                        @Override
                        public void onProgressUpdate(String filePath, boolean isWaiting, long bytesCurrent, long bytesTotal) {

                        }

                        @Override
                        public void onError(String filePath, Exception ex) {
                            Log.d("HeroJongi","download on error "+ex.getMessage());
                        }
                    });
        }
    }


}