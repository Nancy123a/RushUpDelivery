package me.zeroandone.technology.rushupdelivery;

import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.amazonaws.RequestClientOptions;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.StartupAuthErrorDetails;
import com.amazonaws.mobile.auth.core.StartupAuthResult;
import com.amazonaws.mobile.auth.core.StartupAuthResultHandler;
import com.amazonaws.mobile.auth.core.signin.AuthException;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.utils.FirstTimeOpen;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

public class MainActivity extends AppCompatActivity implements StartupAuthResultHandler {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    TextView rushup,theapp,deliver;
    FirstTimeOpen firstTimeOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        rushup=(TextView) findViewById(R.id.rushup) ;
        theapp=(TextView) findViewById(R.id.theapp);
        deliver=(TextView) findViewById(R.id.theappdev);
        firstTimeOpen=new FirstTimeOpen(this);
        Utils.setFontTypeOpenSansBold(this,rushup);
        Utils.setFontTypeOpenSansLight(this,theapp);
        Utils.setFontTypeOpenSans(this,deliver);
        final IdentityManager identityManager = IdentityManager.getDefaultIdentityManager();
        Log.d("XXXN"," "+identityManager.getCachedUserID());
        identityManager.resumeSession(this,this,1000);
    }

    @Override
    public void onBackPressed() {
        //make some alert for the user
        return;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Touch event bypasses waiting for the splash timeout to expire.
        IdentityManager.getDefaultIdentityManager().expireSignInTimeout();
        return true;
    }

    @Override
    public void onComplete(StartupAuthResult authResults) {
        final IdentityManager identityManager = authResults.getIdentityManager();
        Log.d("HeroJongi","StartUpAuthResult");
        if(identityManager.isUserSignedIn()){
            Log.d("HeroJongi","onSuccess Signin"+"user is sign in");
            Intent intent=new Intent(MainActivity.this,InsideApp.class);
            putIntent(intent,getIntent());
            startActivity(intent);
        }
        else if (authResults.isUserAnonymous()) {
            OpenSignInActivity();
        }
        else {
            OpenSignInActivity();
        }
    }

    public void putIntent(Intent intent,Intent getIntent) {
        if (getIntent != null) {
            if (getIntent.getSerializableExtra("delivery_update") != null) {
                DeliveryRequest notificationObject = (DeliveryRequest) getIntent.getSerializableExtra("delivery_update");
                intent.putExtra("delivery_update", notificationObject);
            }
        }
    }

    public void OpenSignInActivity() {
        Intent intent=null;
        if(!firstTimeOpen.getLogin()) {
          intent = new Intent(MainActivity.this, Registration.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       }
       else {
            intent = new Intent(MainActivity.this, UserLogin.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        if(intent!=null) {
            putIntent(intent, getIntent());
            startActivity(intent);
            finish();
        }
    }
}
