package me.zeroandone.technology.rushupdelivery.dialogs;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;

import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.utils.AppHelper;
import me.zeroandone.technology.rushupdelivery.utils.Utils;


public class ForgetPassword extends DialogFragment {
    private static final String LOG_TAG = ForgetPassword.class.getSimpleName();
    EditText Email, Code;
    String email;
    TextView SendLink;
    ForgotPasswordHandler forgetPasswordHandler = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {
           Log.d("HeroJongi","on Success");
        }

        @Override
        public void getResetCode(ForgotPasswordContinuation continuation) {
            Log.d("HeroJongi","Forget Password continuation ");
            getForgotPasswordCode(continuation);
            Utils.dismissDialog(getDialog());
        }

        @Override
        public void onFailure(Exception e) {
           Log.d("HeroJongi"," Error "+e.getMessage()+"  "+e.getCause()+"  "+e.getStackTrace());
        }
    };


    public ForgetPassword() {
    }

    public static ForgetPassword newInstance(String title) {
        ForgetPassword frag = new ForgetPassword();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.setCancelable(false);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forget_password, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SendLink = (TextView) view.findViewById(R.id.forget_password);
        Email = (EditText) view.findViewById(R.id.email);
        Code = (EditText) view.findViewById(R.id.code);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        SendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPasswordUser();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void forgetPasswordUser() {
        email = Email.getText().toString();
        if (VerifyPassword()) {
            Log.d("HeroJongi","Handler call ");
          CognitoUser user= AppHelper.getPool().getCognitoUserPool().getUser(email);
          user.forgotPasswordInBackground(forgetPasswordHandler);
        }
    }

    public boolean VerifyPassword() {
        if (email == null || email.equalsIgnoreCase("")) {
            Email.setError(getResources().getString(R.string.username_error));
            return false;
        }
        else {
            Email.setError(null);
        }
        return true;
    }

    private void getForgotPasswordCode(ForgotPasswordContinuation forgotPasswordContinuation) {
        Utils.dismissDialog(getDialog());
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ForgetPasswordContinuation forgetPasswordContinuation = ForgetPasswordContinuation.newInstance("Some Title");
        forgetPasswordContinuation.setNewPassword(forgotPasswordContinuation);
        forgetPasswordContinuation.setStyle(android.app.DialogFragment.STYLE_NO_TITLE, R.style.MyTheme);
        forgetPasswordContinuation.show(fm, "forget_password_continuation");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        //lock screen to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

}
