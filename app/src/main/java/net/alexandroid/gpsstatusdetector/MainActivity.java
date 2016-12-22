package net.alexandroid.gpsstatusdetector;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.alexandroid.gps.GpsStatusDetector;
import net.alexandroid.shpref.MyLog;

public class MainActivity extends AppCompatActivity
        implements GpsStatusDetector.GpsStatusDetectorCallBack {

    private GpsStatusDetector mGpsStatusDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGpsStatusDetector = new GpsStatusDetector(this);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGpsStatusDetector.checkGpsStatus();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mGpsStatusDetector.checkOnActivityResult(requestCode, resultCode);
    }


    // GpsStatusDetectorCallBack
    @Override
    public void onGpsSettingStatus(boolean enabled) {
        MyLog.d("Gps enabled: " + enabled);
        showSnackBar(enabled ? "GPS enabled" : "GPS disabled");
    }

    @Override
    public void onGpsAlertCanceledByUser() {
        MyLog.d("");
    }

    // Showing SnackBar
    private void showSnackBar(String text) {
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT).show();
    }

}
