package com.example.carpool4ufyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PassengerOptions extends AppCompatActivity {

    BottomNavigationView mbottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_options);

        //assigning the bottom navigation to navigate between the fragments
        mbottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationView);
        Menu menuNav = mbottomNavigationView.getMenu();

        //setting the homeFragment as default Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new PassengerHomeFragment()).commit();
        //BottomNavigationMethod to perform bottomNavigation Action
        mbottomNavigationView.setOnNavigationItemSelectedListener(bottomnavMethod);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu_passenger, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(),
                Toast.LENGTH_SHORT).show();

        if (item.getItemId() == R.id.weatherUpdate) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, WeatherUpdate.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.rateDriver) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, DisplayDrivers.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.viewTraffic) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, ViewTraffic.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.viewFavouriteDrivers) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, DisplayFavouriteDrivers.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.viewPassengerBookings) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, ViewPassengerBookings.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.mapDistance) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, ViewDriversBasedOnLocation.class);
            startActivity(intent);
        }
        return true;
    }


    //Performs the Bottom Navigation aciton
    private BottomNavigationView.OnNavigationItemSelectedListener bottomnavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    //Setting the fragment to null
                    Fragment fragment = null;
                    switch (item.getItemId()) {

                        //Navigates between the activities by using the id of the menu
                        case R.id.homeMenu:
                            fragment = new PassengerHomeFragment();
                            break;
                        case R.id.profileMenu:
                            fragment = new PassengerProfileFragment();
                            break;

                    }
                    //Replaces the fragment in the FrameLayout
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, fragment).commit();
                    return true;
                }
            };
}