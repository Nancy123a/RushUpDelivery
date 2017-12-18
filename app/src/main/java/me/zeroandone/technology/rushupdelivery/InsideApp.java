package me.zeroandone.technology.rushupdelivery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import me.zeroandone.technology.rushupdelivery.widget.BalanceRecyclerView;
import me.zeroandone.technology.rushupdelivery.widget.HistoryRecycleView;
import me.zeroandone.technology.rushupdelivery.widget.SettingsRecycleView;


public class InsideApp extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    RelativeLayout homelayout,settings_menu,history_menu,balance_menu;
    TextView settings,history,balance;
    ImageView settings_close,history_close,balance_close;
    HistoryRecycleView historyRecycleView;
    BalanceRecyclerView balanceRecyclerView;
    SettingsRecycleView settings_recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_app);

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        settings=(TextView)findViewById(R.id.settings);
        homelayout=(RelativeLayout) findViewById(R.id.homelayout);
        settings_menu=(RelativeLayout) findViewById(R.id.settings_menu);
        history=(TextView) findViewById(R.id.history);
        balance=(TextView) findViewById(R.id.balance);
        history_menu=(RelativeLayout) findViewById(R.id.history_menu);
        settings_close=(ImageView) findViewById(R.id.settings_close);
        history_close=(ImageView) findViewById(R.id.history_close);
        balance_close=(ImageView) findViewById(R.id.balance_close);
        balance_menu=(RelativeLayout) findViewById(R.id.balance_menu);
        historyRecycleView=(HistoryRecycleView) findViewById(R.id.historyRecycleView);
        balanceRecyclerView=(BalanceRecyclerView) findViewById(R.id.balanceRecycleView);
        settings_recycler_view = (SettingsRecycleView) findViewById(R.id.settings_recycle_view);

        settings_recycler_view.setAdapter(settings_recycler_view);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        settings.setOnClickListener(this);
        history.setOnClickListener(this);
        balance.setOnClickListener(this);
        settings_close.setOnClickListener(this);
        history_close.setOnClickListener(this);
        balance_close.setOnClickListener(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map=googleMap;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings:
                homelayout.setVisibility(View.GONE);
                settings_menu.setVisibility(View.VISIBLE);
                break;
            case R.id.history:
                homelayout.setVisibility(View.GONE);
                history_menu.setVisibility(View.VISIBLE);
                historyRecycleView.setAdapter(historyRecycleView);
                break;
            case R.id.settings_close:
                homelayout.setVisibility(View.VISIBLE);
                settings_menu.setVisibility(View.GONE);
                break;
            case R.id.history_close:
                homelayout.setVisibility(View.VISIBLE);
                history_menu.setVisibility(View.GONE);
                break;
            case R.id.balance_close:
                homelayout.setVisibility(View.VISIBLE);
                balance_menu.setVisibility(View.GONE);
                break;
            case R.id.balance:
                homelayout.setVisibility(View.GONE);
                balance_menu.setVisibility(View.VISIBLE);
                balanceRecyclerView.setAdapter(balanceRecyclerView);
                break;


        }
    }
}
