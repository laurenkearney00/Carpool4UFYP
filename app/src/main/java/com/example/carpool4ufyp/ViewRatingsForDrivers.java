package com.example.carpool4ufyp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewRatingsForDrivers extends AppCompatActivity {

    private String driverID, driverName;
    private DatabaseReference databaseReference, dataRef, reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ratings_for_drivers);

        TextView ratingText = (TextView) findViewById(R.id.ratingTv);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        TextView nameText = (TextView) findViewById(R.id.nameOfDriver);

        Intent intent = getIntent();
        driverID = intent.getStringExtra(DisplayDrivers.KEY);

        FirebaseDatabase.getInstance().getReference("Users: Drivers").child(driverID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //every time change data the event listener will execute ondatachangemethod
                UserDriver userDriver = snapshot.getValue(UserDriver.class);

                if (userDriver != null) {
                    driverName = userDriver.fullName;
                    nameText.setText(driverName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewRatingsForDrivers.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });


        dataRef = FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(driverID).child("Ratings");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(driverID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("Ratings")) {

                    dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            double total = 0.0;
                            int count = 0;
                            double average = 0.0;
                            double rating;

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Rating aRating = dataSnapshot.getValue(Rating.class);
                                String result = aRating.getRating();
                                rating = Double.parseDouble(result);
                                total = total + rating;
                                count = count + 1;

                            }
                            average = total / count;
                            final DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(driverID);
                            newRef.child("AverageRating").setValue(average);
                            ratingText.setText(average + " out of 5 stars");
                            ratingBar.setRating((float) average);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            });

    }
}




