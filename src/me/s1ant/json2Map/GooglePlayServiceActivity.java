package me.s1ant.json2Map;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import me.s1ant.tools.Constants;

/**
 * Created by slant on 27.12.13.
 */
public class GooglePlayServiceActivity extends FragmentActivity implements

        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_SECONDS);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(Constants.FAST_CEILING_IN_SECONDS);

        // Create a new location client, using the enclosing class to handle callbacks.
        mLocationClient = new LocationClient(this, this, this);

    }

    @Override
    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.removeLocationUpdates(this);
            // Destroy the current location client
            mLocationClient = null;
        }
        Log.d(Constants.LOG_TAG, "Location client destroyed");
        super.onDestroy();
    }

    public Location getLocation() {
        return mLocationClient.getLastLocation();
    }

    /**
     * Called when the Activity no longer visible. Stop all updates and disconnect
     */
    @Override
    public void onStop() {
        if (mLocationClient.isConnected()) {
            stopPeriodicUpdates();
        }
        mLocationClient.disconnect();
        super.onStop();
    }

    /**
     * Called when Activity going in background
     */
    @Override
    public void onPause() {
        //save current settings
        super.onPause();
        stopPeriodicUpdates();
    }

    /**
     * Called when the system detects that this Activity is now visible
     */
    @Override
    public void onResume() {
        super.onResume();
        startPeriodicUpdates();
    }

    /*
      * Called by Location Services when the request to connect the client finishes successfully.
      * At this point, you can request the current location or start periodic updates
      */
    @Override
    public void onConnected(Bundle bundle) {
        // Request location updates using static settings
        startPeriodicUpdates();
        Log.d(Constants.LOG_TAG, "Location client connected");
    }

    /*
     * Called by Location Services if the connection to the location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        stopPeriodicUpdates();
        mLocationClient.disconnect();

        // Destroy the current location client
        mLocationClient = null;
        Log.d(Constants.LOG_TAG, "Location client disconnected");
    }

    /*
     * Called by Location Services if the attempt to Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                Log.d(Constants.LOG_TAG, " Connection to Google Play service error code:" + e);
            }
        } else {
            Log.d(Constants.LOG_TAG, " Connection to Google Play service error code:" + connectionResult.getErrorCode());
        }
    }

    // Define the callback method that receives location updates
    @Override
    public void onLocationChanged(Location location) {
        Log.d(Constants.LOG_TAG,
                Double.toString(location.getLatitude()) + "," +
                        Double.toString(location.getLongitude()));
    }


    /**
     * Verify that Google play services is available before making a request
     *
     * @return true is Google Services available, otherwise return false
     */
    public boolean isGooglePlayServicesAvailable() {
        // Check status of Google Play Services
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        try {
            if (status != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(status, this, Constants.RQS_GooglePlayServices).show();
            } else return true;

        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "GooglePlayServiceUtil: " + e);
        }
        return false;
    }

    /**
     * In response a request to start updates, send a request to Location Services
     */
    public void startPeriodicUpdates() {
        if (mLocationClient.isConnected()) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
    }

    /**
     * In response a request to stop updates, send a request to Location Services
     */
    public void stopPeriodicUpdates() {
        if ( mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
        }
    }
}
