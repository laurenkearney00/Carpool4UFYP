package com.example.carpool4ufyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RegisterCar extends AppCompatActivity implements View.OnClickListener {
    public static final String KEY1 = "CarID";
    ArrayList<String> Cars = new ArrayList<String>();
    private FirebaseAuth mAuth1;
    private EditText editTextLicenceNumber, editTextRegistrationNumber, editTextNumberOfSeats, editTextLicenceExpiration, editTextMakeAndModel, editTextColour;
    private ProgressBar progressBar;
    private Button makeCarRegistration;
    private String timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_car);

        mAuth1 = FirebaseAuth.getInstance();

        makeCarRegistration = (Button) findViewById(R.id.registerCar);
        makeCarRegistration.setOnClickListener(this);

        editTextLicenceNumber = (EditText) findViewById(R.id.licenceNumber);
        editTextRegistrationNumber = (EditText) findViewById(R.id.registrationNumber);
        editTextNumberOfSeats = (EditText) findViewById(R.id.carSeats);
        editTextLicenceExpiration = (EditText) findViewById(R.id.licenceExpiration);
        editTextMakeAndModel = (EditText) findViewById(R.id.makeAndModel);
        editTextColour = (EditText) findViewById(R.id.colour);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerCar:
                try {
                    makeCarRegistration();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void makeCarRegistration () throws ParseException {

        progressBar.setVisibility(View.VISIBLE);
        String licenceNumber = editTextLicenceNumber.getText().toString().trim();
        String registrationNumber = editTextRegistrationNumber.getText().toString().trim();
        String numberOfSeats = editTextNumberOfSeats.getText().toString().trim();
        String licenceExpiration = editTextLicenceExpiration.getText().toString().trim();
        String makeAndModel = editTextMakeAndModel.getText().toString().trim();
        String colour = editTextColour.getText().toString().trim();
        String carDetails = "Licence Number:" + licenceNumber + ", " + "Registration Number:" + registrationNumber + ", "
                + "Number of Seats:" + numberOfSeats + ", " + "Licence Expiration:" +
                licenceExpiration + ", " + "Make and Model:" + makeAndModel + "Colour:" + colour + "\n";


        if (licenceNumber.isEmpty()) {
            editTextLicenceNumber.setError("Licence number is required!");
            editTextLicenceNumber.requestFocus();
            return;
        }
        if (!licenceNumber.matches("[0-9]{9}")) {
            editTextLicenceNumber.setError("Valid licence number must have 9 number digits!");
            editTextLicenceNumber.requestFocus();
            return;
        }

        if (registrationNumber.isEmpty()) {
            editTextRegistrationNumber.setError("Registration is required!");
            editTextRegistrationNumber.requestFocus();
            return;
        }

        if (!registrationNumber.matches("[0-9]{2,3}[A-Z]{1,2}[0-9]{5}")) {
            editTextRegistrationNumber.setError("Valid registration number must have format 131MH12345/131D12345!");
            editTextRegistrationNumber.requestFocus();
            return;
        }

        if (numberOfSeats.isEmpty()) {
            editTextNumberOfSeats.setError("Number of seats are required!");
            editTextNumberOfSeats.requestFocus();
            return;
        }

        if (licenceExpiration.isEmpty()) {
            editTextLicenceExpiration.setError("Licence expiration is required!");
            editTextLicenceExpiration.requestFocus();
            return;
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timestamp = simpleDateFormat.format(calendar.getTime());

        Date date1 =  simpleDateFormat.parse(licenceExpiration);
        Date date2 =  simpleDateFormat.parse(timestamp);

        if (!licenceExpiration.matches("[0-9]{2}[/][0-9]{2}[/][0-9]{4}")) {
            editTextLicenceExpiration.setError("Valid licence expiration must have format 22/07/2022!");
            editTextLicenceExpiration.requestFocus();
            return;
        }

        if (date1.compareTo(date2) < 0) {
            editTextLicenceExpiration.setError("Date must be greater than current date!");
            editTextLicenceExpiration.requestFocus();
            return;
        }

        if (makeAndModel.isEmpty()) {
            editTextMakeAndModel.setError("Make and Model is required!");
            editTextMakeAndModel.requestFocus();
            return;
        }
        if (colour.isEmpty()) {
            editTextLicenceNumber.setError("Colour of car is required!");
            editTextLicenceNumber.requestFocus();
            return;
        }
        if (!Cars.contains(carDetails)) {
            Cars.add(carDetails);

        } else {
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference().child("Cars");
            String carID = fireDB.push().getKey();

            FirebaseUser aCar = mAuth1.getCurrentUser();

            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


            Car car = new Car(licenceNumber, registrationNumber, numberOfSeats, licenceExpiration, makeAndModel, colour, userID, carID);

            FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(userID).child("Cars")
                    .child(carID)
                    .setValue(car).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterCar.this, "Car registration is successful", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        Intent i = new Intent(RegisterCar.this, ViewCarDetails.class);
                        i.putExtra(KEY1, carID);
                        Intent intent = new Intent(RegisterCar.this, SignUpDriver.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterCar.this, "Failed to make car registration! Try again!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

        }
    }

}