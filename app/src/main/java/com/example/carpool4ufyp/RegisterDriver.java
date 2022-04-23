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
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class RegisterDriver extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText editTextFullName, editTextDateOfBirth, editTextPhoneNumber, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private Button registerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.nameOfDriver);
        editTextDateOfBirth = (EditText) findViewById(R.id.birth);
        editTextEmail = (EditText) findViewById(R.id.emailtxt);
        editTextPhoneNumber = (EditText) findViewById(R.id.pnum);
        editTextPassword = (EditText) findViewById(R.id.passwordtxt);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerUser:
                try {
                    registerUser();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registerUser() throws ParseException {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String dateOfBirth = editTextDateOfBirth.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if(fullName.isEmpty()) {
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }

        if (dateOfBirth.isEmpty()){
            editTextDateOfBirth.setError("Date of Birth is required!");
            editTextDateOfBirth.requestFocus();
            return;
        }

        if (!dateOfBirth.matches("[0-9]{2}[/][0-9]{2}[/][0-9]{4}")) {
            editTextDateOfBirth.setError("Valid date of birth must have format 22/07/2022!");
            editTextDateOfBirth.requestFocus();
            return;
        }

        //Converting String to Date
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = formatter.parse(dateOfBirth);
        //Converting obtained Date object to LocalDate object
        Instant instant = date.toInstant();
        ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
        LocalDate givenDate = zone.toLocalDate();
        //Calculating the difference between given date to current date.
        Period period = Period.between(givenDate, LocalDate.now());
        if (period.getYears() < 17) {
            editTextDateOfBirth.setError("Driver must be age 17 or over");
            editTextDateOfBirth.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()){
            editTextPhoneNumber.setError("Phone number is required!");
            editTextPhoneNumber.requestFocus();
            return;
        }

        if (!phoneNumber.matches("[0][0-9]{9}")) {
            editTextPhoneNumber.setError("Correct format for phone number is: 0xx xxx xxxx");
            editTextPhoneNumber.requestFocus();
            return;
        }

        if (email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6 || password.contains(" ")) {
            editTextPassword.setError("Min password length should be 6 characters and contain no whitespaces!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            String driverID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            UserDriver userDriver = new UserDriver(fullName, dateOfBirth, email, phoneNumber, driverID);

                            FirebaseDatabase.getInstance().getReference("Users: Drivers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(userDriver).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterDriver.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        Intent intent = new Intent(RegisterDriver.this, RegisterCar.class);
                                        startActivity(intent);

                                        // redirect to login layout!
                                    }else{
                                        Toast.makeText(RegisterDriver.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(RegisterDriver.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });



    }
}