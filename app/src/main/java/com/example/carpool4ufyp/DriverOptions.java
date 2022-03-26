package com.example.carpool4ufyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverOptions extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_options);

        logout = (Button) findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DriverOptions.this, SignUpDriver.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users: Drivers");
        userID = user.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.greeting);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDriver userProfile = snapshot.getValue(UserDriver.class);

                if(userProfile != null) {
                    String name = userProfile.fullName;
                    greetingTextView.setText("Welcome " + name + "!" + "\n" + "View Driver Menu Options");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverOptions.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
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
        if (item.getItemId() == R.id.passengers) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this,DisplayPassengers.class);
            startActivity(intent);
        }
        return true;
    }
}
