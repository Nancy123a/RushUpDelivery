package me.zeroandone.technology.rushupdelivery;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

import me.zeroandone.technology.rushupdelivery.dialogs.ForgetPassword;
import me.zeroandone.technology.rushupdelivery.objects.DeliveryRequest;
import me.zeroandone.technology.rushupdelivery.utils.AppHelper;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

public class UserLogin extends AppCompatActivity implements SignInStateChangeListener {
    AVLoadingIndicatorView indicator;
    EditText username, password;
    String Username, Password;
    TextView forgetpassword, login,forget_login;
    IdentityManager identityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        indicator=(AVLoadingIndicatorView) findViewById(R.id.indicator);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        login=(TextView) findViewById(R.id.login);
        forget_login=(TextView) findViewById(R.id.forget_login);
        identityManager = IdentityManager.getDefaultIdentityManager();
        identityManager.addSignInStateChangeListener(this);
        if (identityManager.getCurrentIdentityProvider() == null) {
            AppHelper.initializeCognitoPool(this);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indicator.setVisibility(View.VISIBLE);
                if (checkUsernameAndPassword()) {
                    AppHelper.getPool().getCognitoUserPool().getUser(Username).getSessionInBackground(authenticationHandler);
                }
            }
        });
        forget_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenForgetPasswordActivity();
            }
        });
    }

    private void OpenForgetPasswordActivity() {
        //   finish();
        FragmentManager fm = getSupportFragmentManager();
        ForgetPassword forgetDialog = ForgetPassword.newInstance("Some Title");
        forgetDialog.setStyle(android.app.DialogFragment.STYLE_NO_TITLE, R.style.MyTheme);
        forgetDialog.show(fm, "activity_profile");
    }

    private boolean checkUsernameAndPassword() {
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
        Log.d("HeroJongi"," Username "+username+" Password "+Password);
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(username, Password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(final CognitoUserSession cognitoUserSession, CognitoDevice device) {
            Log.d("HeroJongi", "OnSuccess");
            indicator.setVisibility(View.GONE);
            username.setError(null);
            password.setError(null);
            AppHelper.federateWithProvider();
            Intent i = new Intent(UserLogin.this, InsideApp.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (getIntent() != null) {
                if (i.getSerializableExtra("delivery_update") != null) {
                    DeliveryRequest notificationObject = (DeliveryRequest) i.getSerializableExtra("delivery_update");
                    i.putExtra("delivery_update", notificationObject);
                }
            }
            startActivity(i);
            finish();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            Log.d("HeroJongi","getAuthenticationDetails");
            getUserAuthentication(authenticationContinuation, Username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

        }

        @Override
        public void onFailure(Exception exception) {
            Log.d("HeroJongi"," Failure "+exception.getMessage());
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

        }

    };

    @Override
    public void onUserSignedIn() {

    }

    @Override
    public void onUserSignedOut() {

    }
}
