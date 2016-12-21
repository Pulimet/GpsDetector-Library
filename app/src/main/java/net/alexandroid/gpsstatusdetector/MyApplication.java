package net.alexandroid.gpsstatusdetector;

import android.app.Application;

import net.alexandroid.shpref.MyLog;
import net.alexandroid.shpref.ShPref;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ShPref.init(getApplicationContext(), ShPref.APPLY);
        MyLog.showLogs(true);
    }
}
