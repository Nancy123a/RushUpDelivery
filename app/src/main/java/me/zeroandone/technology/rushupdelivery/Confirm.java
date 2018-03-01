package me.zeroandone.technology.rushupdelivery;



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

import me.zeroandone.technology.rushupdelivery.utils.AppHelper;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

public class Confirm extends AppCompatActivity {
    private static final String LOG_TAG = Confirm.class.getSimpleName();
    private static CognitoCachingCredentialsProvider sCredProvider;
    String Username, Password,phone,type;
    EditText code;
    TextView resendCode;
    TextView step2, profile;
    ImageView close;
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        code = (EditText) findViewById(R.id.code);
        close = (ImageView) findViewById(R.id.imageView);
        step2 = (TextView) findViewById(R.id.steptwo);
        profile = (TextView)findViewById(R.id.profile);
        resendCode = (TextView) findViewById(R.id.resend_code);
        avLoadingIndicatorView=(AVLoadingIndicatorView) findViewById(R.id.indicator);
            Utils.setFontTypeOpenSans(getApplicationContext(), step2);
            Utils.setFontTypeOpenSans(getApplicationContext(), profile);
            Utils.setFontTypeOpenSans(getApplicationContext(), resendCode);
        code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    if (code.getText().toString() != null && code.getText().toString() != "") {
                        // This will cause confirmation to fail if the user attribute has been verified for another user in the same pool
                        boolean forcedAliasCreation = false;
                        if (AppHelper.getPool() != null && AppHelper.getPool().getCognitoUserPool().getUser(Username) != null) {
                            AppHelper.getPool().getCognitoUserPool().getUser(Username).confirmSignUpInBackground(code.getText().toString(), forcedAliasCreation, confHandler);
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
                Log.d("HeroJongi"," Success confirmation ");
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
            Log.d("HeroJongi"," Success login ");
            AppHelper.federateWithProvider();
            // success confirm and sign in
            Intent intent=new Intent(Confirm.this,InsideApp.class);
            startActivity(intent);
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
            Log.d("HeroJongi","fail");
            avLoadingIndicatorView.setVisibility(View.GONE);
        }
    };
}
