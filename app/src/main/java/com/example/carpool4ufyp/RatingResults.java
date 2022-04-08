package com.example.carpool4ufyp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RatingResults extends AppCompatActivity {

    private DatabaseReference databaseReference;
    String driverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_results);

        TextView ratingText = (TextView) findViewById(R.id.rating);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(user).child("Ratings");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                double total = 0.0;
                int count = 0;
                double average;
                double rating;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rating aRating = dataSnapshot.getValue(Rating.class);
                    String result = aRating.getRating();
                    //Toast.makeText(RatingResults.this, "Result" + result, Toast.LENGTH_LONG).show();
                    rating = Double.parseDouble(result);
                    total =  total + rating;
                    count = count + 1;

                }
                average = total / count;
                final DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(user);
                newRef.child("AverageRating").setValue(average);
                ratingText.setText(average + " out of 5 stars");
                ratingBar.setRating((float) average);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

}




