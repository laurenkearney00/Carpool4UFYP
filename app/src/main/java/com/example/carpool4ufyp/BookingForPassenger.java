package com.example.carpool4ufyp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class BookingForPassenger extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText editTextDriverName, editTextPassengerName, editTextMeetingPoint, editTextDestination, editTextDate, editTextPickupTime, editTextPrice;
    private ProgressBar progressBar;
    String passengerID;
    private Button sendBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_for_passenger);

        mAuth = FirebaseAuth.getInstance();

        sendBooking = (Button) findViewById(R.id.sendBooking);
        sendBooking.setOnClickListener(this);

        editTextDriverName = (EditText) findViewById(R.id.driverName);
        editTextPassengerName = (EditText) findViewById(R.id.passengerName);
        editTextMeetingPoint = (EditText) findViewById(R.id.meetingPoint);
        editTextDestination = (EditText) findViewById(R.id.destination);
        editTextDate = (EditText) findViewById(R.id.date);
        editTextPickupTime = (EditText) findViewById(R.id.time);
        editTextPrice = (EditText) findViewById(R.id.price);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendBooking:
                sendBooking();
                break;
        }
    }

    private void sendBooking() {
        String driver = editTextDriverName.getText().toString().trim();
        String passenger = editTextPassengerName.getText().toString().trim();
        String meetingPoint = editTextMeetingPoint.getText().toString().trim();
        String destination = editTextDestination.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String pickupTime = editTextPickupTime.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();

        if (driver.isEmpty()) {
            editTextDriverName.setError("Full name is required!");
            editTextDriverName.requestFocus();
            return;
        }

        if (passenger.isEmpty()) {
            editTextPassengerName.setError("Full name is required!");
            editTextPassengerName.requestFocus();
            return;
        }

        if (meetingPoint.isEmpty()) {
            editTextMeetingPoint.setError("Location is required!");
            editTextMeetingPoint.requestFocus();
            return;
        }

        if (destination.isEmpty()) {
            editTextDestination.setError("Location is required!");
            editTextDestination.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            editTextDate.setError("Date is required!");
            editTextDate.requestFocus();
            return;
        }

        if (pickupTime.isEmpty()) {
            editTextPickupTime.setError("Time is required!");
            editTextPickupTime.requestFocus();
            return;
        }

        if (price.isEmpty()) {
            editTextPrice.setError("Price is required!");
            editTextPrice.requestFocus();
            return;
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference().child("Bookings");
            String bookingID = fireDB.push().getKey();
            String driverID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Intent intent = getIntent();
            passengerID = intent.getStringExtra(MessagePassenger.KEY);

            Booking booking = new Booking(driver, passenger, meetingPoint, destination, date, pickupTime, price, driverID, passengerID);

            FirebaseDatabase.getInstance().getReference().child("Users: Passengers").child(passengerID).child("Bookings")
                    .child(bookingID)
                    .setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(BookingForPassenger.this, "Booking sent to Passenger", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(BookingForPassenger.this, "Failed to make booking! Try again!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

        }
    }
}