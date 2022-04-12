package com.example.carpool4ufyp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void signInDriver(View v) {
        Intent intent = new Intent(getApplicationContext(), LoginDriver.class);
        startActivity(intent);
    }

    public void signInPassenger(View v) {
        Intent intent = new Intent(getApplicationContext(), LoginPassenger.class);
        startActivity(intent);
    }

}