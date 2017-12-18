package me.zeroandone.technology.rushupdelivery.widget;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import me.zeroandone.technology.rushupdelivery.adapter.SettingsAdapter;
import me.zeroandone.technology.rushupdelivery.objects.Settings;

public class SettingsRecycleView extends RecyclerView{
    Context context;

    public SettingsRecycleView(Context context) {
        super(context);
        this.context=context;
    }

    public SettingsRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public SettingsRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
    }

    public void setAdapter(SettingsRecycleView settings_recycler_view){
        List<Settings> settings_list = new ArrayList<>();
        settings_list.add(new Settings("My account", null, Settings.HEADER));
        settings_list.add(new Settings(null, "First Name", Settings.OPTION));
        settings_list.add(new Settings(null, "Last Name", Settings.OPTION));
        settings_list.add(new Settings(null, "Mobile number", Settings.OPTION));
        settings_list.add(new Settings(null, "Email", Settings.OPTION));
        settings_list.add(new Settings(null, "Password", Settings.OPTION));
        settings_list.add(new Settings(null, "Notification Sound", Settings.Notification));
        settings_list.add(new Settings("More Information", null, Settings.HEADER));
        settings_list.add(new Settings(null, "Support", Settings.OPTION));
        settings_list.add(new Settings(null, "Privacy Policy", Settings.OPTION));
        settings_list.add(new Settings(null, "Terms of service", Settings.OPTION));
        settings_list.add(new Settings(null, "Licenses", Settings.OPTION));
        settings_list.add(new Settings("Account actions", null, Settings.HEADER));
//        if (details != null && details.getAttributes().getAttributes().get("email_verified").equalsIgnoreCase("false")) {
//            settings_list.add(new Settings(null, "Verify Email", Settings.OPTION));
//        }
//        if (details != null && details.getAttributes().getAttributes().get("phone_number_verified").equalsIgnoreCase("false")) {
//            settings_list.add(new Settings(null, "Verify Phone Number", Settings.OPTION));
//        }
        settings_list.add(new Settings(null, "Clear History", Settings.OPTION));
        settings_list.add(new Settings(null, "Log Out", Settings.OPTION));
        settings_list.add(new Settings(null, "Add Account", Settings.OPTION));
        settings_list.add(new Settings(null, "Licenses", Settings.OPTION));
       SettingsAdapter adapter = new SettingsAdapter(context, settings_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);
        settings_recycler_view.setLayoutManager(linearLayoutManager);
        settings_recycler_view.setItemAnimator(new DefaultItemAnimator());
        settings_recycler_view.setAdapter(adapter);
    }
}
