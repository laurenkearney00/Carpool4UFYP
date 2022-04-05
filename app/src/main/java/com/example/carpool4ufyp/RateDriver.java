package com.example.carpool4ufyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateDriver extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private TextView ratingDisplayTextView;
    private RatingBar ratingRatingBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String receiver;
    DatabaseReference reference;
    String star;
    String passenger = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String rateID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);
        ratingDisplayTextView = (TextView) findViewById(R.id.rating_display_text_View);
        ratingRatingBar = (RatingBar) findViewById(R.id.rating_rating_bar);
        ratingDisplayTextView = (TextView) findViewById(R.id.rating_display_text_View);

        Intent intent = getIntent();
        receiver = intent.getStringExtra(DisplayDriversAdapter.MESSAGE_KEY3);


        reference = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(receiver).child("Ratings");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Rating aRating = dataSnapshot.getValue(Rating.class);
                    if (aRating.getReceiver().equals(receiver) && aRating.getSender().equals(passenger)) {
                        String rating = aRating.getRating();
                        star = aRating.getRatingID();
                        ratingDisplayTextView.setText(rating);
                        ratingRatingBar.setRating(Float.parseFloat(rating));
                        changeRating();
                    }
                    else {
                        DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference().child("Ratings");
                        star = fireDB.push().getKey();
                        Button submitButton = (Button) findViewById(R.id.submit_button);
                        submitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ratingDisplayTextView.getText().toString().equals("Your rating will appear here")) {
                                    String rate = String.valueOf(ratingRatingBar.getRating());
                                    ratingDisplayTextView.setText(rate);
                                    String sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    Rating aRating = new Rating(rate, receiver, sender, star);

                                    FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(receiver).child("Ratings")
                                            .child(star)
                                            .setValue(aRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(RateDriver.this, "Rating saved", Toast.LENGTH_LONG).show();

                                            } else
                                                Toast.makeText(RateDriver.this, "Failed to save rating! Try again!", Toast.LENGTH_LONG).show();
                                        }

                                    });
                                }
                                else {
                                    Toast.makeText(RateDriver.this, "Rating is already saved", Toast.LENGTH_LONG).show();
                                }
                            }

                        });
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
                Rating aRating = new Rating(rate, receiver, sender, star);
                FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(receiver).child("Ratings")
                        .child(star)
                        .setValue(aRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RateDriver.this, "Saving selected rating", Toast.LENGTH_SHORT).show();

                        } else
                            Toast.makeText(RateDriver.this, "Failed to save rating! Try again!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}




