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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePassenger extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button updateDetails;
    private TextView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_passenger);

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        updateDetails = (Button) findViewById(R.id.update);
        updateDetails.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users: Passengers");
        userID = user.getUid();

        EditText nameEditText = (EditText) findViewById(R.id.name);
        EditText phoneNumberEditText = (EditText) findViewById(R.id.phoneNumber);

        //retrieve user details and updateUI
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //every time change data the event listener will execute ondatachangemethod
                UserPassenger userPassenger = snapshot.getValue(UserPassenger.class);

                if(userPassenger != null){
                    String name = userPassenger.fullName;
                    String phoneNumber = userPassenger.phoneNumber;

                    nameEditText.setText(name);
                    phoneNumberEditText.setText(phoneNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfilePassenger.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                updateDetails();
                break;
        }
    }

    private void updateDetails() {
        EditText name = (EditText) findViewById(R.id.name);
        String username = name.getText().toString();
        EditText phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        String number = phoneNumber.getText().toString();
        if (username.isEmpty()) {
            name.setError("Name is required!");
            name.requestFocus();
            return;
        }
        else {
            String userID = user.getUid();
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Passengers").child(userID);
            fireDB.child("fullName").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                    Toast.makeText(ProfilePassenger.this, "Update successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ProfilePassenger.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });

        }
        if (number.isEmpty() && !number.matches("[0][0-9]{9}")) {
            phoneNumber.setError("Number is required and correct format for phone number is: 0xx xxx xxxx!");
            phoneNumber.requestFocus();
            return;
        }
        else {
            String userID = user.getUid();
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Passengers").child(userID);
            fireDB.child("phoneNumber").setValue(number).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                    Toast.makeText(ProfilePassenger.this, "Update successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ProfilePassenger.this, "Update failed",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}