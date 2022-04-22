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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewCarDetails extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private DatabaseReference fireDB;
    private String userID;
    private Button updateDetails;
    private TextView banner;
    private int position;
    private String timestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_view_car_details);

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        updateDetails = (Button) findViewById(R.id.pay_button);
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
            case R.id.pay_button:
                try {
                    updateDetails();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void updateDetails() throws ParseException {
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

        int licenceNum = Integer.parseInt(licenceNumberString);
        int seatsNum = Integer.parseInt(numberOfSeatsString);

        Car car = new Car();
        car.setLicenceNumber(licenceNum);
        car.setRegistrationNumber(registrationNumberString);
        car.setNumberOfSeats(seatsNum);
        car.setLicenceExpiration(licenceExpirationString);
        car.setMakeAndModel(makeAndModelString);
        car.setColour(colourString);

        DisplayCar.myAdapter.UpdateCar(position, car);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
        if (licenceNumberString.isEmpty()) {
            licenceNumberEditText.setError("Licence Number is required!");
            licenceNumberEditText.requestFocus();
            return;
        }
        else if (!licenceNumberString.matches("[0-9]{9}")) {
            licenceNumberEditText.setError("Valid licence number must have 9 number digits!");
            licenceNumberEditText.requestFocus();
            return;
        }
        else {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("licenceNumber").setValue(licenceNumberString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful
                    Toast.makeText(ViewCarDetails.this, "Update for licence number is successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            //this.finish();
        }
        if (registrationNumberString.isEmpty()) {
            registrationNumberEditText.setError("Registration is required!");
            registrationNumberEditText.requestFocus();
            return;
        }

        else if (!registrationNumberString.matches("[0-9]{2,3}[A-Z]{1,2}[0-9]{5}")) {
            registrationNumberEditText.setError("Valid registration number must have format 131MH12345/131D12345!");
            registrationNumberEditText.requestFocus();
            return;
        }

        else {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("registrationNumber").setValue(registrationNumberString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                Toast.makeText(ViewCarDetails.this, "Update for registration number is successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            //this.finish();
        }

        if (numberOfSeatsString.isEmpty()) {
            numberOfSeatsEditText.setError("Number of seats are required!");
            numberOfSeatsEditText.requestFocus();
            return;
        }

        else {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("numberOfSeats").setValue(numberOfSeatsString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                   Toast.makeText(ViewCarDetails.this, "Update for number of seats is successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            //this.finish();
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        timestamp = simpleDateFormat.format(calendar.getTime());
        licenceExpirationString = licenceExpirationEditText.getText().toString();

        if (licenceExpirationString.isEmpty()) {
            licenceExpirationEditText.setError("Licence expiration is required!");
            licenceExpirationEditText.requestFocus();
            return;
        }

        if (!licenceExpirationString.matches("[0-9]{2}[/][0-9]{2}[/][0-9]{4}")) {
            licenceExpirationEditText.setError("Valid licence expiration must have format 22/07/2022!");
            licenceExpirationEditText.requestFocus();
            return;
        }
        Date date1 =  simpleDateFormat.parse(licenceExpirationString);
        Date date2 =  simpleDateFormat.parse(timestamp);
        if (date1.compareTo(date2) < 0) {
            licenceExpirationEditText.setError("Date must be greater than current date!");
            licenceExpirationEditText.requestFocus();
            return;
        }

        else {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("licenceExpiration").setValue(licenceExpirationString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                 Toast.makeText(ViewCarDetails.this, "Update for licence expiration is successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            //this.finish();
        }

        if (makeAndModelString.isEmpty()) {
            makeAndModelEditText.setError("Make and Model is required!");
            makeAndModelEditText.requestFocus();
            return;
        }

        else {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("makeAndModel").setValue(makeAndModelString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                  Toast.makeText(ViewCarDetails.this, "Update for make and model is successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            //this.finish();
        }

        if (colourString.isEmpty()) {
            colourEditText.setError("Colour of car is required!");
            colourEditText.requestFocus();
            return;
        }

        else {
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(userID).child("Cars").child(carID);
            fireDB.child("colour").setValue(colourString).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                  Toast.makeText(ViewCarDetails.this, "Update for colour is successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ViewCarDetails.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
            //this.finish();
        }

        Intent i = new Intent(ViewCarDetails.this, DriverOptions.class);
        startActivity(i);



    }
}

