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


public class DriverOptions extends AppCompatActivity {

    BottomNavigationView mbottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_options);

        //assigning the bottom navigation to navigate between the fragments
        mbottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationView);
        Menu menuNav = mbottomNavigationView.getMenu();

        //setting the homeFragment as default Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new DriverHomeFragment()).commit();
        //BottomNavigationMethod to perform bottomNavigation Action
        mbottomNavigationView.setOnNavigationItemSelectedListener(bottomnavMethod);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu_driver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(),
                Toast.LENGTH_SHORT).show();

        if (item.getItemId() == R.id.profileDriver) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, ProfileDriver.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.displayCar) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, DisplayCar.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.weatherUpdate) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, WeatherUpdate.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.locationsettings) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, DriversLocation.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.viewTraffic) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, ViewTraffic.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.ratingResult) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, RatingResults.class);
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
                            fragment = new DriverHomeFragment();
                            break;
                        case R.id.profileMenu:
                            fragment = new DriverProfileFragment();
                            break;

                    }
                    //Replaces the fragment in the FrameLayout
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, fragment).commit();
                    return true;
                }
            };
}


