package me.zeroandone.technology.rushupdelivery.dialogs;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;

import me.zeroandone.technology.rushupdelivery.R;
import me.zeroandone.technology.rushupdelivery.utils.Utils;

public class ForgetPasswordContinuation extends DialogFragment {
    ForgotPasswordContinuation continuation;
    EditText newpassword, newcode;
    TextView done;

    public ForgetPasswordContinuation() {
    }

    public static ForgetPasswordContinuation newInstance(String title) {
        ForgetPasswordContinuation frag = new ForgetPasswordContinuation();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.setCancelable(false);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.forget_password_continuation, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public void setNewPassword(ForgotPasswordContinuation forgetpasswordcontinuation) {
        continuation = forgetpasswordcontinuation;
    }

    @Override
    public void onResume() {
        super.onResume();
        //lock screen to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        done = (TextView) view.findViewById(R.id.done);
        newpassword = (EditText) view.findViewById(R.id.password);
        newcode = (EditText) view.findViewById(R.id.code);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VerifyFormEnter(newpassword.getText().toString(), newcode.getText().toString())) {
                    Log.d("HeroJongi", " Verify Email Enter ");
                    continuation.setPassword(newpassword.getText().toString());

                    // Set the code to verify
                    continuation.setVerificationCode(newcode.getText().toString());

                    // Let the forgot password process proceed
                    continuation.continueTask();
                    getDialog().dismiss();
                }

            }
        });


    }

    public boolean VerifyFormEnter(String password, String code) {

        if (password == null || password.equalsIgnoreCase("")) {
            newpassword.setError(getResources().getString(R.string.password_error));
            return false;
        } else if (!Utils.isPasswordValid(password)) {
            newpassword.setError(getResources().getString(R.string.password_valid));
            return false;
        } else {
            newpassword.setError(null);
        }
        if (code == null || code.equalsIgnoreCase("")) {
            newcode.setError(getResources().getString(R.string.error_code));
            return false;
        } else {
            newcode.setError(null);
        }
        return true;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
