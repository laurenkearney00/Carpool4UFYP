package com.example.carpool4ufyp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayFavouriteDrivers extends AppCompatActivity {
    public static final String KEY = "driverID";
    private ArrayList<FavouriteDriver> list = new ArrayList<>();
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    RecyclerView mRecyclerView;
    public static DisplayFavouriteDriversAdapter myAdapter;
    String userID;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Button profileDriver, messageDriver, favouritedDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_favourite_drivers);
        mRecyclerView = findViewById(R.id.my_recycler_view);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        myAdapter = new DisplayFavouriteDriversAdapter(DisplayFavouriteDrivers.this, list);


        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        progressDialog = new ProgressDialog(DisplayFavouriteDrivers.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users: Passengers").child(userID).child("FavouriteDrivers");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    FavouriteDriver favouriteDriver = dataSnapshot.getValue(FavouriteDriver.class);

                    list.add(favouriteDriver);


                }


                myAdapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

        myAdapter.setOnFavouriteDriverClickListener(new FavouriteDriverClickListener() {
            @Override
            public void OnDriverClick(int position, FavouriteDriver favouriteDriver) {
                builder = new AlertDialog.Builder(DisplayFavouriteDrivers.this);
                favouriteDriver.getDriverID();
                builder.setTitle("View Options");
                builder.setCancelable(false);
                View view = LayoutInflater.from(DisplayFavouriteDrivers.this).inflate(R.layout.favourite_driver_dialog, null, false);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();

                String driverID = favouriteDriver.getDriverID();

                profileDriver = view.findViewById(R.id.profile);
                messageDriver = view.findViewById(R.id.message);
                favouritedDriver = view.findViewById(R.id.driver);

                profileDriver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(DisplayFavouriteDrivers.this, ViewFavouritedDriversDetails.class);
                        i.putExtra(KEY, driverID);
                        startActivity(i);
                    }
                });

                messageDriver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(DisplayFavouriteDrivers.this, MessageFavouritedDriver.class);
                        i.putExtra(KEY, driverID);
                        startActivity(i);
                    }
                });

                favouritedDriver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }
}