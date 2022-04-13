package com.example.carpool4ufyp;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.List;

public class ViewTraffic extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_traffic);
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        UiSettings mapSettings;
        mapSettings = mMap.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setCompassEnabled(true);
        mapSettings.setMyLocationButtonEnabled(true);
        getLocation();
    }

    //Capturing user/device current location and display on map
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(ViewTraffic.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(600000);
        //mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient = new FusedLocationProviderClient(getApplicationContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                double lat = locationResult.getLastLocation().getLatitude();
                double lng = locationResult.getLastLocation().getLongitude();
                LatLng currentlocation = new LatLng(lat, lng);
                displayLocation(currentlocation);
            }
        }, null);
    }

    public void displayLocation(LatLng latlng) {
        if (mMap != null) {
            Geocoder coder = new Geocoder(this);
            try {
                List<Address> locations = coder.getFromLocation(latlng.latitude,
                        latlng.longitude, 1);
                if (locations != null) {
                    String add1 = locations.get(0).getAddressLine(0);
                    mMap.addMarker(new MarkerOptions().position(latlng).title("Current location").snippet(add1));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

