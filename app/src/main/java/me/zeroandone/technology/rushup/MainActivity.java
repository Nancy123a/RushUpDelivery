package me.zeroandone.technology.rushup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import me.zeroandone.technology.rushup.utils.Utils;

public class MainActivity extends AppCompatActivity {

    TextView rushup,theapp,deliver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        rushup=(TextView) findViewById(R.id.rushup) ;
        theapp=(TextView) findViewById(R.id.theapp);
        deliver=(TextView) findViewById(R.id.theappdev);
        Utils.setFontTypeOpenSansBold(this,rushup);
        Utils.setFontTypeOpenSansLight(this,theapp);
        Utils.setFontTypeOpenSans(this,deliver);

       Intent insideactivity=new Intent(this,InsideApp.class);
      startActivity(insideactivity);

    }
    @Override
    public void onBackPressed() {
        //make some alert for the user
        return;
    }


}
