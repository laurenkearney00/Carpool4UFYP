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

    private static final String TEXT = "text";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String RESULT = "result";
    private TextView ratingDisplayTextView;
    private String text;
    private RatingBar ratingRatingBar;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);

        ratingRatingBar = (RatingBar) findViewById(R.id.rating_rating_bar);
        Button submitButton = (Button) findViewById(R.id.submit_button);
        ratingDisplayTextView = (TextView) findViewById(R.id.rating_display_text_View);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ratingDisplayTextView.setText("Your rating is: " + ratingRatingBar.getRating());
                String rating = String.valueOf(ratingRatingBar.getRating());
                ratingDisplayTextView.setText(rating);
                saveData();
            }
        });

        loadData();
        updateViews();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, ratingDisplayTextView.getText().toString());

        editor.apply();

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");

    }

    public void updateViews() {
        ratingDisplayTextView.setText(text);
        //ratingRatingBar.setRating(Float.parseFloat(text));

        ratingRatingBar.setStepSize(Float.parseFloat(text));
    }

}