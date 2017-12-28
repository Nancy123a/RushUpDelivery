package me.zeroandone.technology.rushupdelivery.dialogs;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
import me.zeroandone.technology.rushupdelivery.utils.AppHelper;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

public class UserLogin extends DialogFragment implements SignInStateChangeListener,View.OnClickListener {

    private static final String LOG_TAG = UserLogin.class.getSimpleName();
    EditText username,password;
    IdentityManager identityManager;
    AVLoadingIndicatorView indicator;
    ImageView close;
    TextView login;
    String Username,Password;

    public static UserLogin newInstance(String title) {
        UserLogin frag = new UserLogin();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.setCancelable(false);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        identityManager = IdentityManager.getDefaultIdentityManager();
        identityManager.addSignInStateChangeListener(this);
        if (identityManager.getCurrentIdentityProvider() == null) {
            AppHelper.initializeCognitoPool(getActivity().getApplicationContext());
        }
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        login=(TextView) view.findViewById(R.id.login);
        close=(ImageView) view.findViewById(R.id.close);
        indicator = (AVLoadingIndicatorView) view.findViewById(R.id.indicator);
        close.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //lock screen to portrait
        if(getActivity()!=null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
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
            Intent intent=new Intent(getActivity(),InsideApp.class);
            startActivity(intent);
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
