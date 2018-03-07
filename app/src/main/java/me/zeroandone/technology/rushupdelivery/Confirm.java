package me.zeroandone.technology.rushupdelivery;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.core.IdentityProvider;
import com.amazonaws.mobile.auth.core.SignInResultHandler;
import com.amazonaws.mobile.auth.core.SignInStateChangeListener;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.wang.avi.AVLoadingIndicatorView;

import me.zeroandone.technology.rushupdelivery.model.Driver;
import me.zeroandone.technology.rushupdelivery.model.TokenRequest;
import me.zeroandone.technology.rushupdelivery.objects.DriverCode;
import me.zeroandone.technology.rushupdelivery.utils.AppHelper;
import me.zeroandone.technology.rushupdelivery.utils.FirstTimeOpen;
import me.zeroandone.technology.rushupdelivery.utils.InternalStorage;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

public class Confirm extends AppCompatActivity{
    private static final String LOG_TAG = Confirm.class.getSimpleName();
    String Username, Password,Code,Phone;
    EditText code;
    TextView resendCode;
    TextView step2, profile;
    ImageView close;
    FirstTimeOpen firstTimeOpen;
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        code = (EditText) findViewById(R.id.code);
        close = (ImageView) findViewById(R.id.imageView);
        step2 = (TextView) findViewById(R.id.steptwo);
        profile = (TextView)findViewById(R.id.profile);
        resendCode = (TextView) findViewById(R.id.resend_code);
        firstTimeOpen=new FirstTimeOpen(this);
        avLoadingIndicatorView=(AVLoadingIndicatorView) findViewById(R.id.indicator);
        Bundle bundle = getIntent().getExtras();
        Username=bundle.getString("username");
        Password=bundle.getString("password");
        Code=bundle.getString("code");
        Phone=bundle.getString("phone");
            Utils.setFontTypeOpenSans(getApplicationContext(), step2);
            Utils.setFontTypeOpenSans(getApplicationContext(), profile);
            Utils.setFontTypeOpenSans(getApplicationContext(), resendCode);
        code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String _code=code.getText().toString();
                    if ( !_code.equalsIgnoreCase("")) {
                        if (AppHelper.getPool() != null && AppHelper.getPool().getCognitoUserPool().getUser(Username) != null) {
                            AppHelper.getPool().getCognitoUserPool().getUser(Username).confirmSignUpInBackground(code.getText().toString(), false, confHandler);
                        }
                    }
                }
                return false;
            }
        });


        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppHelper.getPool() != null && AppHelper.getPool().getCognitoUserPool().getUser(Username) != null) {
                    Log.d("HeroJongi"," Trigger on resend code ");
                    avLoadingIndicatorView.setVisibility(View.VISIBLE);
                    AppHelper.getPool().getCognitoUserPool().getUser(Username).resendConfirmationCodeInBackground(resendConfCodeHandler);

                }
            }
        });

    }

    GenericHandler confHandler = new GenericHandler() {
        // code is confirmed here
        @Override
        public void onSuccess() {
            // This is a bug if getPool is null we just stop here
            if (AppHelper.getPool() != null && AppHelper.getPool().getCognitoUserPool().getUser(Username) != null) {
                Log.d("HeroJongi"," Success confirmation "+Username);
                AppHelper.getPool().getCognitoUserPool().getUser(Username).getSessionInBackground(authenticationHandler);
            }
        }

        @Override
        public void onFailure(Exception exception) {
            Log.d("HeroJongi"," error "+exception.getMessage());
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                code.startAnimation(shake);

        }
    };

    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(final CognitoUserSession cognitoUserSession, CognitoDevice device) {
            AppHelper.federateWithProvider();
            firstTimeOpen.isSignin(true);
            Intent intent=new Intent(Confirm.this,InsideApp.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            getUserAuthentication(authenticationContinuation, username);

        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

        }

        @Override
        public void onFailure(Exception e) {
            Log.d(LOG_TAG, "SignIn Failure " + e.getMessage());

        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

        }


        private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
            Log.d("getUserAuthentication", username);
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(username, Password, null);
            continuation.setAuthenticationDetails(authenticationDetails);
            continuation.continueTask();
        }
    };

    VerificationHandler resendConfCodeHandler = new VerificationHandler() {
        @Override
        public void onSuccess(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            Log.d("HeroJongi"," success ");
            avLoadingIndicatorView.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Exception exception) {
            Log.d("HeroJongi","fail "+exception.getMessage());
            avLoadingIndicatorView.setVisibility(View.GONE);
        }
    };

}
