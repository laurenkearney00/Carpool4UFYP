package com.example.carpool4ufyp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayDrivers extends AppCompatActivity {


    private ArrayList<UserDriver> list = new ArrayList<>();
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    RecyclerView mRecyclerView;
    public static DisplayDriversAdapter myAdapter;
    EditText editText;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Button averageRating, createRating, driver;
    public static final String KEY ="driverID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_drivers);

        editText = (EditText) findViewById(R.id.search);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        mRecyclerView = findViewById(R.id.my_recycler_view);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        myAdapter = new DisplayDriversAdapter(DisplayDrivers.this, list);


        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        progressDialog = new ProgressDialog(DisplayDrivers.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users: Drivers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserDriver userDriver = dataSnapshot.getValue(UserDriver.class);

                    list.add(userDriver);


                }


                myAdapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

        myAdapter.setOnDriverClickListener(new DriverClickListener() {
            @Override
            public void OnDriverClick(int position, UserDriver userDriver) {
                builder = new AlertDialog.Builder(DisplayDrivers.this);
                userDriver.getDriverID();
                builder.setTitle("View Options");
                builder.setCancelable(false);
                View view = LayoutInflater.from(DisplayDrivers.this).inflate(R.layout.driver_dialog, null, false);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();

                String driverID = userDriver.getDriverID();

                averageRating = view.findViewById(R.id.averageRating);
                createRating = view.findViewById(R.id.createRating);
                driver = view.findViewById(R.id.driver);

                averageRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(DisplayDrivers.this, ViewRatingsForDrivers.class);
                        i.putExtra(KEY, driverID);
                        startActivity(i);
                    }
                });

                createRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(DisplayDrivers.this, RateDriver.class);
                        i.putExtra(KEY, driverID);
                        startActivity(i);
                    }
                });

                driver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void filter(String text) {
        ArrayList<UserDriver> filteredList = new ArrayList<>();

        for (UserDriver userDriver : list) {
            if (userDriver.getFullName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(userDriver);
            }
        }

        myAdapter.filterList(filteredList);
    }

}