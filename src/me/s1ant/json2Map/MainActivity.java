package me.s1ant.json2Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * Created by slant on 27.12.13.
 */
public class MainActivity extends GooglePlayServiceActivity {
    private GoogleMap googleMap;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (isGooglePlayServicesAvailable()) {
            startPeriodicUpdates();
            setUpMapIfNeeded();
        }

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
            if (googleMap == null){
                Toast.makeText(this, getString(R.string.map_not_created), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
