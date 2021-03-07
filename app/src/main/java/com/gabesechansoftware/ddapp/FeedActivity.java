package com.gabesechansoftware.ddapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.gabesechansoftware.ddapp.api.DoorDashApiController;
import com.gabesechansoftware.ddapp.databinding.ActivityMainBinding;
import com.gabesechansoftware.ddapp.feedfetcher.FeedFetcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Activity which displays the feed.  Handles keeping an updated location and telling the feed
 * fetcher when that location has changed.
 */
public class FeedActivity extends AppCompatActivity {
    private static final int LOCATION_REQUEST = 1;
    private double lat = 37.422740;
    private double lng = -122.139956;

    private FeedFetcher feedFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.feed.setHasFixedSize(true);
        binding.feed.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,
                        false));
        feedFetcher = new FeedFetcher(new DoorDashApiController());
        FeedRecyclerAdapter feedAdapter = new FeedRecyclerAdapter(feedFetcher);
        binding.feed.setAdapter(feedAdapter);
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }
        else {
            requestPermissions( new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    LOCATION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation();
                }  else {
                    Toast.makeText(this,
                            "No location permission, using default location",
                            Toast.LENGTH_LONG).show();
                    feedFetcher.resetLatLng(lat, lng);
                }
            default:
                throw new RuntimeException("Invalid request code");
        }
    }

    private void requestLocation() throws SecurityException {
        LocationManager locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager == null) {
            //This should never happen unless the OS has no location ability.
            feedFetcher.resetLatLng(lat, lng);
            return;
        }
        locationManager.requestSingleUpdate("gps", new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                feedFetcher.resetLatLng(lat, lng);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }, Looper.myLooper());
    }
}
