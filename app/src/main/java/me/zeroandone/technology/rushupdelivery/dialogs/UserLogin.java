package me.zeroandone.technology.rushupdelivery.dialogs;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.SignInStateChangeListener;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.wang.avi.AVLoadingIndicatorView;

import me.zeroandone.technology.rushupdelivery.InsideApp;
import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.utils.AppHelper;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

public class UserLogin extends AppCompatActivity implements SignInStateChangeListener,View.OnClickListener {

    private static final String LOG_TAG = UserLogin.class.getSimpleName();
    EditText username,password;
    IdentityManager identityManager;
    AVLoadingIndicatorView indicator;
    ImageView close;
    TextView login;
    String Username,Password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        identityManager = IdentityManager.getDefaultIdentityManager();
        identityManager.addSignInStateChangeListener(this);
        if (identityManager.getCurrentIdentityProvider() == null) {
            AppHelper.initializeCognitoPool(this);
        }
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login=(TextView) findViewById(R.id.login);
        close=(ImageView) findViewById(R.id.close);
        indicator = (AVLoadingIndicatorView) findViewById(R.id.indicator);
        close.setOnClickListener(this);
        login.setOnClickListener(this);
    }


    @Override
    public void onUserSignedIn() {

    }

    @Override
    public void onUserSignedOut() {

    }

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(final CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Log.i(LOG_TAG, "Auth Success");
            Log.d("HeroJongi","OnSuccess");
            indicator.setVisibility(View.GONE);
            username.setError(null);
            password.setError(null);
            AppHelper.federateWithProvider();
            openInsideActivity();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            Log.i(LOG_TAG, authenticationContinuation.getParameters() + "");
            Log.d("HeroJongi","getAuthenticationDetails");
            getUserAuthentication(authenticationContinuation, Username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

        }

        @Override
        public void onFailure(Exception exception) {
            Log.d("HeroJongi","onError "+exception.getMessage());
            indicator.setVisibility(View.GONE);
            if(exception.getMessage().contains("Incorrect username or password.")){
                password.setError(getResources().getString(R.string.enter_correct_password));
            }
            else if(exception.getMessage().contains("User does not exist.")){
                username.setError(getResources().getString(R.string.user_does_not_exist));
            }

        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            Log.d("HeroJongi","authenticationChallenge");
        }

    };

    private void openInsideActivity() {
        Log.d("HeroJongi","Reach Here inside app");
        Intent intent=new Intent(this,InsideApp.class);
        putIntent(intent,getIntent());
        startActivity(intent);
    }

    public void putIntent(Intent intent,Intent getIntent) {
        if (getIntent != null) {
            if (getIntent.getSerializableExtra("delivery_update") != null) {
                DeliveryRequest notificationObject = (DeliveryRequest) getIntent.getSerializableExtra("delivery_update");
                intent.putExtra("delivery_update", notificationObject);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                indicator.setVisibility(View.VISIBLE);
                if (checkUsernameAndPassword()) {
                    AppHelper.getPool().getCognitoUserPool().getUser(Username).getSessionInBackground(authenticationHandler);
                }
                break;
            case R.id.close:

                break;

        }
    }



    public boolean checkUsernameAndPassword(){
        Username = username.getText().toString();
        Password = password.getText().toString();
        if (Username == null || Username.equalsIgnoreCase("")) {
            username.setError(getResources().getString(R.string.username_error));
            return false;
        }
        if (Password == null || Password.equalsIgnoreCase("")) {
            password.setError(getResources().getString(R.string.password_error));
            return false;
        }
        if (!Utils.isPasswordValid(Password)) {
            password.setError(getResources().getString(R.string.password_valid));
            return false;
        }
        return true;
    }


    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(username, Password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }
}
