package me.s1ant.json2Map;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class MapActivity extends Activity {
    final String LOG_TAG = "json2Map";
    // Google Map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null)
                Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    private boolean checkConnection() {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if ((ni.getTypeName().equalsIgnoreCase("WIFI")
                        || ni.getTypeName().equalsIgnoreCase("MOBILE"))
                        & ni.isConnected() & ni.isAvailable()) {
                    connected = true;
                }
            }
        }

        if (connected) {
            //  check if google is reachable
            try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(10 * 1000); // Ten seconds timeout in milliseconds
                urlc.connect();
                // return boolean
                return urlc.getResponseCode() == 200;
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }
        return false;
    }

}
