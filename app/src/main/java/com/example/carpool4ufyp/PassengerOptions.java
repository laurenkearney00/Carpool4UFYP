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

public class PassengerOptions extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_options);

        logout = (Button) findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PassengerOptions.this, SignUpPassenger.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users: Passengers");
        userID = user.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.greeting);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserPassenger userProfile = snapshot.getValue(UserPassenger.class);

                if(userProfile != null) {
                    String name = userProfile.fullName;
                    greetingTextView.setText("Welcome " + name + "!" + "\n" + "View Passenger Menu Options");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PassengerOptions.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

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

        if (item.getItemId() == R.id.profilePassenger) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, ProfilePassenger.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.weatherUpdate) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, WeatherUpdate.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.map) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, ViewDrivers.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.passengerLocation) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, PassengersLocation.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.passengers) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this,DisplayDrivers.class);
            startActivity(intent);
        }


        return true;
    }
}
