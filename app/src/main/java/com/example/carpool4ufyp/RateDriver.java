package com.example.carpool4ufyp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RateDriver extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private TextView ratingDisplayTextView;
    private RatingBar ratingRatingBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);
        ratingDisplayTextView = (TextView) findViewById(R.id.rating_display_text_View);
        ratingRatingBar = (RatingBar) findViewById(R.id.rating_rating_bar);
        Button submitButton = (Button) findViewById(R.id.submit_button);
        ratingDisplayTextView = (TextView) findViewById(R.id.rating_display_text_View);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating = String.valueOf(ratingRatingBar.getRating());
                ratingDisplayTextView.setText(rating);
                ratingRatingBar = findViewById(R.id.rating_rating_bar);
                editor = sharedPreferences.edit();
                editor.putFloat("numStars", Float.parseFloat(rating));
                editor.commit();
                ratingRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromTouch) {
                        editor = sharedPreferences.edit();
                        editor.putFloat("numStars", rating);
                        editor.commit();
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        float rating = sharedPreferences.getFloat("numStars", 0f);
        ratingRatingBar.setRating(rating);
        ratingDisplayTextView.setText(rating + "/" + String.valueOf(rating));

    }
}
