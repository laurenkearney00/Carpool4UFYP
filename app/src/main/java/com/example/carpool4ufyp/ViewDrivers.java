package com.example.carpool4ufyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.jar.Attributes;

public class ViewDrivers extends FragmentActivity implements OnMapReadyCallback {

    public static final String KEY = "driverID";
    public static final String KEY2 = "driverName";
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private FirebaseAuth mAuth1;
    private ProgressBar progressBar;
    private LatLng currentlocation;
    private double lat;
    private double lng;
    private String timestamp;
    private DatabaseReference passenger;
    private DatabaseReference reference;
    private String name;
    //private String driverIDgb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_drivers);
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings mapSettings;
        mapSettings = mMap.getUiSettings();
        mapSettings.setZoomControlsEnabled(true);
        mapSettings.setCompassEnabled(true);
        mapSettings.setMyLocationButtonEnabled(true);
// Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
        getLocation();


    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(ViewDrivers.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient = new FusedLocationProviderClient(getApplicationContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                lat = locationResult.getLastLocation().getLatitude();
                lng = locationResult.getLastLocation().getLongitude();
                currentlocation = new LatLng(lat, lng);
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
                    //mMap.addMarker(new MarkerOptions().position(latlng).title("Current location").snippet(add1));
                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(latlng).title("Current location").snippet(add1));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        location();
    }


    public void location() {
        //just get drivers location
        DatabaseReference currentDBcordinates = FirebaseDatabase.getInstance().getReference().child("DriverLocations");
        currentDBcordinates.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Create an array of markers
                int size = (int) dataSnapshot.getChildrenCount(); //
                Marker[] allMarkers = new Marker[size];
                mMap.clear();   //Assuming you're using mMap
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //Specify your model class here
                    CurrentLocation currentLocation = new CurrentLocation();
                    //lets create a loop
                    for (int i = 0; i <= size; i++) {
                        try {
                            //assuming you've set your getters and setters in the Model class
                            currentLocation.setLat(ds.getValue(CurrentLocation.class).getLat());
                            currentLocation.setLng(ds.getValue(CurrentLocation.class).getLng());
                            currentLocation.setUserID(ds.getValue(CurrentLocation.class).getUserID());
                            //currentLocation.setBrandName(ds.getValue(CurrentLocation.class).getBrandName());
                            //lets retrieve the coordinates and other information
                            Double latitude1 = currentLocation.getLat();
                            Double longitude1 = currentLocation.getLng();
                            String driverID = currentLocation.getUserID();

                            //convert the coordinates to LatLng
                            LatLng latLng = new LatLng(latitude1, longitude1);
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            //Now lets add updated markers
                            //lets add updated marker
                           allMarkers[i] = mMap.addMarker(new MarkerOptions()
                                          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).position(latLng).snippet(driverID));


                        } catch (Exception ex) {
                        }
                    }


                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       marker();

    }
    public void marker() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                showUpdateDialog(marker);
                return false;
            }

        });
    }
    private void showUpdateDialog(Marker marker) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final Button buttonSend = (Button) dialogView.findViewById(R.id.buttonSend);

        String userID = marker.getSnippet();

        reference = FirebaseDatabase.getInstance().getReference("Users: Drivers");
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDriver userProfile = snapshot.getValue(UserDriver.class);

                if (userProfile != null) {
                    name = userProfile.fullName;
                    dialogBuilder.setTitle("Drivers Name: " + name);

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    buttonSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String receiver = marker.getSnippet();
                            Intent i = new Intent(ViewDrivers.this, MessageDriver.class);
                            i.putExtra(KEY, receiver);
                            startActivity(i);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewDrivers.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });


    }



}
