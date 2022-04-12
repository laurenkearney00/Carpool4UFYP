package com.example.carpool4ufyp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewFavouritedDriversDetails extends AppCompatActivity {

    private DatabaseReference reference, databaseReference;
    ProgressDialog progressDialog;
    String receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_favourited_drivers_details);

        reference = FirebaseDatabase.getInstance().getReference("Users: Drivers");
        Intent intent = getIntent();
        receiver = intent.getStringExtra(DisplayFavouriteDrivers.KEY);

        TextView nameText = (TextView) findViewById(R.id.name);
        TextView phoneNumberText = (TextView) findViewById(R.id.destination);
        TextView licenceNumberText = (TextView) findViewById(R.id.licenceNumber);
        TextView registrationNumberText = (TextView) findViewById(R.id.registrationNumber);
        TextView numberOfSeatsText = (TextView) findViewById(R.id.carSeats);
        TextView licenceExpirationText = (TextView) findViewById(R.id.licenceExpiration);
        TextView makeAndModelText = (TextView) findViewById(R.id.makeAndModel);
        TextView colourText = (TextView) findViewById(R.id.colour);

        //retrieve user details and updateUI
        reference.child(receiver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //every time change data the event listener will execute ondatachangemethod
                UserDriver userDriver = snapshot.getValue(UserDriver.class);

                if(userDriver != null){
                    String name = userDriver.fullName;
                    String phoneNumber = userDriver.phoneNumber;

                    nameText.setText("Name: " + name);
                    phoneNumberText.setText("Phone Number: " + phoneNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFavouritedDriversDetails.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        progressDialog = new ProgressDialog(ViewFavouritedDriversDetails.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(receiver).child("Cars");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Car cars = dataSnapshot.getValue(Car.class);
                    String licenceNumber = cars.getLicenceNumber();
                    licenceNumberText.setText("Licence Number: " + licenceNumber);
                    String registrationNumber = cars.getRegistrationNumber();
                    registrationNumberText.setText("Registration Number: " + registrationNumber);
                    String numberOfSeats = cars.getNumberOfSeats();
                    numberOfSeatsText.setText("Number of Seats: " + numberOfSeats);
                    String licenceExpiration = cars.getLicenceExpiration();
                    licenceExpirationText.setText("Licence Expiration: " + licenceExpiration);
                    String makeAndModel = cars.getMakeAndModel();
                    makeAndModelText.setText("Make and Model: " + makeAndModel);
                    String colour = cars.getColour();
                    colourText.setText("Colour: " + colour);

                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });



    }

}




