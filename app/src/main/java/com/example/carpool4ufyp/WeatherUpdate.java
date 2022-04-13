package com.example.carpool4ufyp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;


public class WeatherUpdate extends AppCompatActivity {
    TextView tvResult;
    TextView tvLocation;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "18f3c1e254c9b5f8c5b410f3157e3d89";
    DecimalFormat df = new DecimalFormat("#.##");
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    public double lat;
    public double lng;
    private LatLng currentlocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_update);


        tvResult = findViewById(R.id.tvResult);
        tvLocation = findViewById(R.id.tvLocation);
        getLocation();
    }

        public void getLocation(){
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(getApplicationContext(), "No permission", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(WeatherUpdate.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }
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
            }        , null);
        }

        public void displayLocation(LatLng latlng){
            Geocoder coder = new Geocoder( this );

                // Toast.makeText(getApplicationContext(), "lat " + latlng.longitude, Toast.LENGTH_SHORT).show();
                String tempUrl = "";
                tempUrl = url + "?lat=" + latlng.latitude //e.g. 53.2906 - lat
                        + "&lon=" + latlng.longitude //e.g. -6.3064 - lng
                        + "&appid=" + appid;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String output = "";
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                            String description = jsonObjectWeather.getString("description");
                            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                            double temp = jsonObjectMain.getDouble("temp") - 273.15;
                            double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                            float pressure = jsonObjectMain.getInt("pressure");
                            int humidity = jsonObjectMain.getInt("humidity");
                            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                            String clouds = jsonObjectClouds.getString("all");
                            JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                            String countryName = jsonObjectSys.getString("country");
                            String cityName = jsonResponse.getString("name");
                            //tvResult.setTextColor(Color.rgb(68, 134, 199));

                            try {
                                List<Address> locations =  coder.getFromLocation(latlng.latitude,
                                        latlng.longitude, 1);
                                if (locations != null) {
                                    String add1 = locations.get(0).getAddressLine(0);
                                    String location = "";
                                    location = "Location: " + add1;
                                    tvLocation.setText(location);
                                    output +=
                                     "\n \n Current weather for " + cityName + " (" + countryName + ")"
                                    + "\n Temp: " + df.format(temp) + " °C"
                                    + "\n Feels Like: " + df.format(feelsLike) + " °C"
                                    + "\n Humidity: " + humidity + "%"
                                    + "\n Description: " + description
                                    + "\n Wind Speed: " + wind + "m/s (meters per second)"
                                    + "\n Cloudiness: " + clouds + "%"
                                    + "\n Pressure: " + pressure + " hPa";
                            tvResult.setText(output);
                                }
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
    }





