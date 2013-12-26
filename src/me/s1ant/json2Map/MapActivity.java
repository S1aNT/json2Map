package me.s1ant.json2Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;


public class MapActivity extends ActionBarActivity implements LocationListener {
    final String LOG_TAG = "json2Map";

    final int RQS_GooglePlayServices = 1;
    private LocationManager locationManager;
    private String provider;
    private ShareActionProvider mShareActionProvider;
    private GoogleMap googleMap;

    double lat = 50;
    double longi = 50;

    // Keeps a reference of the application context
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();

        if (isGooglePlayOn()) {
            setContentView(R.layout.main);
        }

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.turnOnYourGPS))
                    .setMessage(getString(R.string.pleaseEnableGPS))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // if this button is clicked, close current activity
                            MapActivity.this.finish();

                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        }

        // Define the criteria how to select the location provider -> use  default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        setUpMapIfNeeded();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        // Log.d(LOG_TAG, "Cliced MenuItem is " + item.getTitle());
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(this, getString(R.string.action_search_toast), Toast.LENGTH_SHORT)
                        .show();
                //openSearch();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, getString(R.string.action_settings_toast), Toast.LENGTH_SHORT)
                        .show();
                //openSettings();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lng = (int) (location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar  Inflate menu resource file.
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        mShareActionProvider.setShareIntent(createShareIntent());

        // Return true to display menu
        return true;
    }

    private Intent createShareIntent() {
        Intent share = new Intent(Intent.ACTION_SEND);
        /**
         * TODO Create share action
         */
        share.setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, "TEST")
                .setType("text/plain");
        return share;
    }

    private boolean isGooglePlayOn() {
        // Check status of Google Play Services
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // Check Google Play Service Available
        try {
            if (status != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(status, this, RQS_GooglePlayServices).show();
                Toast.makeText(this, "Google Play Services cannot be found", Toast.LENGTH_SHORT).show();
            } else return true;

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error: GooglePlayServiceUtil:" + e);
        }
        return false;
    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);

            googleMap.setMyLocationEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // check if map is created successfully or not
            if (googleMap == null)
                Toast.makeText(mContext, getString(R.string.map_not_created), Toast.LENGTH_SHORT).show();
        }
    }
}
