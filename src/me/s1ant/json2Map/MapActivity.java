package me.s1ant.json2Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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


public class MapActivity extends ActionBarActivity {
    final String LOG_TAG = "json2Map";
    final int RQS_GooglePlayServices = 1;
    private ShareActionProvider mShareActionProvider;
    // Google Map
    private GoogleMap googleMap;
    /**
     * Keeps a reference of the application context
     */
    private static Context mContext;


    public MapActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mContext = getApplicationContext();


        // Check status of Google Play Services
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // Check Google Play Service Available
        try {
            if (status != ConnectionResult.SUCCESS) {
                GooglePlayServicesUtil.getErrorDialog(status, this, RQS_GooglePlayServices).show();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error: GooglePlayServiceUtil:" + e);
        }
        // Loading map
        initMapEngine();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        // Inflate menu resource file.
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

    @Override
    protected void onResume() {
        super.onResume();
        initMapEngine();
    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initMapEngine() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);
            

            // check if map is created successfully or not
            if (googleMap == null)
                Toast.makeText(mContext, getString(R.string.map_not_created), Toast.LENGTH_SHORT).show();
        }
    }

}
