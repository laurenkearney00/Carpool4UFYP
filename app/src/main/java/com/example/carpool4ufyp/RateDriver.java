package com.example.carpool4ufyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateDriver extends AppCompatActivity implements View.OnClickListener {

    private TextView ratingDisplayTextView;
    private RatingBar ratingRatingBar;
    String receiver;
    DatabaseReference reference, databaseReference;
    String starID;
    String passenger = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String driverName;
    String favouriteDriverID;
    private Button addButton;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);

        TextView fullNameTv = (TextView) findViewById(R.id.rating);
        ratingDisplayTextView = (TextView) findViewById(R.id.rating_display_text_View);
        ratingRatingBar = (RatingBar) findViewById(R.id.rating_bar);
        ratingDisplayTextView = (TextView) findViewById(R.id.rating_display_text_View);

        Intent intent = getIntent();
        receiver = intent.getStringExtra(DisplayDriversAdapter.MESSAGE_KEY3);

        reference = FirebaseDatabase.getInstance().getReference("Users: Drivers");
        reference.child(receiver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //every time change data the event listener will execute ondatachangemethod
                UserDriver userDriver = snapshot.getValue(UserDriver.class);

                if (userDriver != null) {
                    driverName = userDriver.fullName;
                    fullNameTv.setText(driverName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RateDriver.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(receiver).child("Ratings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rating aRating = dataSnapshot.getValue(Rating.class);
                    if (aRating.getReceiver().equals(receiver) && aRating.getSender().equals(passenger)) {
                        String rating = aRating.getRating();
                        starID = aRating.getRatingID();
                        submitButton.setText("Submitted Rating");
                        ratingDisplayTextView.setText("Rating given to driver is " + rating);
                        ratingRatingBar.setRating(Float.parseFloat(rating));
                        changeRating();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users: Passengers").child(passenger).child("FavouriteDrivers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FavouriteDriver driver = dataSnapshot.getValue(FavouriteDriver.class);
                    if (driver.getDriverID().equals(receiver)) {
                        addButton.setText("Added Driver");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void changeRating() {
        ratingRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromTouch) {
                String rate = String.valueOf(rating);
                String sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Rating aRating = new Rating(rate, receiver, sender, starID);
                FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(receiver).child("Ratings")
                        .child(starID)
                        .setValue(aRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RateDriver.this, "Updating rating", Toast.LENGTH_SHORT).show();

                        } else
                            Toast.makeText(RateDriver.this, "Failed to save rating! Try again!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                addButton();
                break;
            case R.id.submit_button:
                submitButton();
                break;
        }
    }

    private void submitButton() {
        if (submitButton.getText().equals("Submitted Rating")) {
            Toast.makeText(RateDriver.this, "Rating has already been created for driver", Toast.LENGTH_LONG).show();
        } else {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference().child("Ratings");
            starID = fireDB.push().getKey();
            if (ratingDisplayTextView.getText().toString().equals("Your rating will appear here")) {
                String rate = String.valueOf(ratingRatingBar.getRating());
                ratingDisplayTextView.setText("Rating given to driver is " + rate);
                String sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Rating aRating = new Rating(rate, receiver, sender, starID);

                FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(receiver).child("Ratings")
                        .child(starID)
                        .setValue(aRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RateDriver.this, "Rating saved", Toast.LENGTH_LONG).show();
                            submitButton.setText("Submitted Rating");

                        } else
                            Toast.makeText(RateDriver.this, "Failed to save rating! Try again!", Toast.LENGTH_LONG).show();
                    }

                });
            } else {
                Toast.makeText(RateDriver.this, "Rating is already saved", Toast.LENGTH_LONG).show();
            }
        }
    }

        private void addButton () {
            if (addButton.getText().equals("Added Driver")) {
                Toast.makeText(RateDriver.this, "Driver has already been added to favourites", Toast.LENGTH_LONG).show();
            } else {
                DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference().child("FavouriteDrivers");
                favouriteDriverID = fireDB.push().getKey();
                FavouriteDriver driver = new FavouriteDriver(driverName, receiver);
                FirebaseDatabase.getInstance().getReference().child("Users: Passengers").child(passenger).child("FavouriteDrivers")
                        .child(favouriteDriverID)
                        .setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(RateDriver.this, "Driver has been added to favourites", Toast.LENGTH_LONG).show();
                            addButton.setText("Added Driver");

                        } else {
                            Toast.makeText(RateDriver.this, "Failed to add driver to favourites! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        }
    }
