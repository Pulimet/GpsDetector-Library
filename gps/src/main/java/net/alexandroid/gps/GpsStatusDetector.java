package net.alexandroid.gps;


import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.lang.ref.WeakReference;

import static android.app.Activity.RESULT_OK;

public class GpsStatusDetector {

    private static final int REQUEST_CODE = 2;

    private WeakReference<Activity> mActivityWeakReference;
    private WeakReference<GpsStatusDetectorCallBack> mCallBackWeakReference;

    public GpsStatusDetector(Activity activity) {
        this.mActivityWeakReference = new WeakReference<>(activity);
        this.mCallBackWeakReference = new WeakReference<>((GpsStatusDetectorCallBack) activity);
    }

    public GpsStatusDetector(Fragment fragment) {
        this.mActivityWeakReference = new WeakReference<>((Activity) fragment.getActivity());
        this.mCallBackWeakReference = new WeakReference<>((GpsStatusDetectorCallBack) fragment);
    }

    public void checkGpsStatus() {
        Activity activity = mActivityWeakReference.get();
        GpsStatusDetectorCallBack callBack = mCallBackWeakReference.get();
        if (activity == null || callBack == null) {
            return;
        }

        if (isGpsEnabled(activity)) {
            callBack.onGpsSettingStatus(true);
        } else {
            setLocationRequest(activity, callBack);
        }
    }

    private boolean isGpsEnabled(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    private void setLocationRequest(final Activity activity, final GpsStatusDetectorCallBack callBack) {
        final GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(30 * 1000)
                .setFastestInterval(5 * 1000);

        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true) // important!
                .build();

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, locationSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        callBack.onGpsSettingStatus(true);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(activity, REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            callBack.onGpsSettingStatus(false);
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        callBack.onGpsSettingStatus(false);
                        break;
                }

                mGoogleApiClient.disconnect(); // If you do not disconnect, causes a memory leak
            }
        });
    }


    public void checkOnActivityResult(int requestCode, int resultCode) {
        Activity activity = mActivityWeakReference.get();
        GpsStatusDetectorCallBack callBack = mCallBackWeakReference.get();
        if (activity == null || callBack == null) {
            return;
        }

        if (requestCode == GpsStatusDetector.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                callBack.onGpsSettingStatus(true);
            } else {
                callBack.onGpsSettingStatus(false);
                callBack.onGpsAlertCanceledByUser();
            }
        }
    }

    public interface GpsStatusDetectorCallBack {
        void onGpsSettingStatus(boolean enabled);

        void onGpsAlertCanceledByUser();
    }

}
