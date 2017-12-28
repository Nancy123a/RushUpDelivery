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
import me.zeroandone.technology.rushupdelivery.dialogs.Registration;
import me.zeroandone.technology.rushupdelivery.dialogs.UserLogin;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

public class MainActivity extends AppCompatActivity implements StartupAuthResultHandler {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    TextView rushup,theapp,deliver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        rushup=(TextView) findViewById(R.id.rushup) ;
        theapp=(TextView) findViewById(R.id.theapp);
        deliver=(TextView) findViewById(R.id.theappdev);
        Utils.setFontTypeOpenSansBold(this,rushup);
        Utils.setFontTypeOpenSansLight(this,theapp);
        Utils.setFontTypeOpenSans(this,deliver);


        final IdentityManager identityManager = IdentityManager.getDefaultIdentityManager();

        //Clear all existing logins , just for debugging purpose
//        final CognitoCachingCredentialsProvider credentialsProvider = identityManager.getUnderlyingProvider();
//        credentialsProvider.clearCredentials();

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
        if(identityManager.isUserSignedIn()){
            Log.d(LOG_TAG,"onSuccess Signin"+"user is sign in");
            Intent intent=new Intent(MainActivity.this,InsideApp.class);
            startActivity(intent);
        }
        else if (authResults.isUserAnonymous()) {
            Log.d(LOG_TAG,"onfailure Signin"+"user is not sign in");
            // Optionally, you can check whether refreshing a previously signed in provider failed.
            final StartupAuthErrorDetails errors = authResults.getErrorDetails();
            if (errors.didErrorOccurRefreshingProvider()) {
                final AuthException providerAuthException = errors.getProviderRefreshException();
                Log.w(LOG_TAG, String.format(
                        "Credentials for Previously signed-in provider %s could not be refreshed.",
                        providerAuthException.getProvider().getDisplayName()),
                        providerAuthException);
            }
            OpenSignInActivity();
        }
        else {
            Log.d(LOG_TAG,"onfailure Signin"+"user is not sign in");
            final StartupAuthErrorDetails errors = authResults.getErrorDetails();
            Log.e(LOG_TAG, "No Identity could be obtained. Continuing with no identity.",
                    errors.getUnauthenticatedErrorException());
            OpenSignInActivity();
        }
    }

    public void OpenSignInActivity() {
        FragmentManager fm = getSupportFragmentManager();
        UserLogin signin = UserLogin.newInstance("Some Title");
        signin.setStyle(android.app.DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
        signin.show(fm, "login");
    }
}
