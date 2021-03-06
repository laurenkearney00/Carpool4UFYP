package com.example.carpool4ufyp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RatingResults extends AppCompatActivity {

    private DatabaseReference databaseReference, dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_results);

        TextView ratingText = (TextView) findViewById(R.id.nameOfDriver);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        String driverID = FirebaseAuth.getInstance().getCurrentUser().getUid();

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




