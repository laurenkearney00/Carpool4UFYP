package com.example.carpool4ufyp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewCarDetails extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private DatabaseReference fireDB;
    private String userID;
    private Button updateDetails;
    private TextView banner;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_view_car_details);

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        updateDetails = (Button) findViewById(R.id.update);
        updateDetails.setOnClickListener(this);

        Intent intent= getIntent();

        position = intent.getIntExtra(CarAdapter.MESSAGE_KEY2,0);

        String carID = intent.getStringExtra(CarAdapter.MESSAGE_KEY8);
        String text1 = intent.getStringExtra(CarAdapter.MESSAGE_KEY1);
        String text2 = intent.getStringExtra(CarAdapter.MESSAGE_KEY3);
        String text3 = intent.getStringExtra(CarAdapter.MESSAGE_KEY4);
        String text4 = intent.getStringExtra(CarAdapter.MESSAGE_KEY5);
        String text5 = intent.getStringExtra(CarAdapter.MESSAGE_KEY6);
        String text6 = intent.getStringExtra(CarAdapter.MESSAGE_KEY7);
        EditText licenceNumberEditText = (EditText) findViewById(R.id.licenceNumberTxt);
        EditText registrationNumberEditText = (EditText) findViewById(R.id.registrationNumberTxt);
        EditText numberOfSeatsEditText = (EditText) findViewById(R.id.numberOfSeatsTxt);
        EditText licenceExpirationEditText = (EditText) findViewById(R.id.licenceExpirationTxt);
        EditText makeAndModelEditText = (EditText) findViewById(R.id.makeAndModelTxt);
        EditText colourEditText = (EditText) findViewById(R.id.colourTxt);
        licenceNumberEditText.setText(text1);
        registrationNumberEditText.setText(text2);
        numberOfSeatsEditText.setText(text3);
        licenceExpirationEditText.setText(text4);
        makeAndModelEditText.setText(text5);
        colourEditText.setText(text6);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, DriverOptions.class));
                break;
            case R.id.update:
                updateDetails();
                break;
        }
    }

    private void updateDetails() {
        Intent intent = getIntent();
        String carID = intent.getStringExtra(CarAdapter.MESSAGE_KEY8);
        EditText licenceNumberEditText = (EditText) findViewById(R.id.licenceNumberTxt);
        EditText registrationNumberEditText = (EditText) findViewById(R.id.registrationNumberTxt);
        EditText numberOfSeatsEditText = (EditText) findViewById(R.id.numberOfSeatsTxt);
        EditText licenceExpirationEditText = (EditText) findViewById(R.id.licenceExpirationTxt);
        EditText makeAndModelEditText = (EditText) findViewById(R.id.makeAndModelTxt);
        EditText colourEditText = (EditText) findViewById(R.id.colourTxt);

        String licenceNumberString = licenceNumberEditText.getText().toString();
        String registrationNumberString = registrationNumberEditText.getText().toString();
        String numberOfSeatsString = numberOfSeatsEditText.getText().toString();
        String licenceExpirationString = licenceExpirationEditText.getText().toString();
        String makeAndModelString = makeAndModelEditText.getText().toString();
        String colourString = colourEditText.getText().toString();

        Car car = new Car();
        car.setLicenceNumber(licenceNumberString);
        car.setRegistrationNumber(registrationNumberString);
        car.setNumberOfSeats(numberOfSeatsString);
        car.setLicenceExpiration(licenceExpirationString);
        car.setMakeAndModel(makeAndModelString);
        car.setColour(colourString);

        DisplayCar.myAdapter.UpdateCar(position, car);


        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);


        if (!licenceNumberString.isEmpty()) {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("licenceNumber").setValue(licenceNumberString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                    Toast.makeText(ViewCarDetails.this, "Update successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            this.finish();
        }


        if (!registrationNumberString.isEmpty()) {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("registrationNumber").setValue(registrationNumberString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                    Toast.makeText(ViewCarDetails.this, "Update successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            this.finish();
        }

        if (!numberOfSeatsString.isEmpty()) {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("numberOfSeats").setValue(numberOfSeatsString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                    Toast.makeText(ViewCarDetails.this, "Update successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            this.finish();
        }

        if (!licenceExpirationString.isEmpty()) {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("licenceExpiration").setValue(licenceExpirationString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                    Toast.makeText(ViewCarDetails.this, "Update successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            this.finish();
        }

        if (!makeAndModelString.isEmpty()) {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("makeAndModel").setValue(makeAndModelString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                    Toast.makeText(ViewCarDetails.this, "Update successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            this.finish();
        }

        if (!colourString.isEmpty()) {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("colour").setValue(colourString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                    Toast.makeText(ViewCarDetails.this, "Update successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            this.finish();
        }

        Intent i = new Intent(ViewCarDetails.this, DriverOptions.class);
        startActivity(i);



    }
}

