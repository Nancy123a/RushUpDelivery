package me.zeroandone.technology.rushupdelivery.dialogs;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.zeroandone.technology.rushupdelivery.InsideApp;
import me.zeroandone.technology.rushupdelivery.R;

public class Registration extends DialogFragment implements View.OnClickListener{

    TextView submit;

    public static Registration newInstance(String title) {
        Registration frag = new Registration();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.setCancelable(false);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.registration, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submit=(TextView) view.findViewById(R.id.submit);
        submit.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                Intent inside=new Intent(getActivity(), InsideApp.class);
                startActivity(inside);
                break;
        }
    }
}
