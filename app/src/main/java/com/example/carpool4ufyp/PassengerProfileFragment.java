package com.example.carpool4ufyp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class PassengerProfileFragment extends Fragment{

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button updateDetails;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public PassengerProfileFragment() {
        // Required empty public constructor
    }

    public static PassengerProfileFragment newInstance(String param1, String param2) {
        PassengerProfileFragment fragment = new PassengerProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_passenger_profile_fragment, container, false);

        updateDetails = (Button) view.findViewById(R.id.pay_button);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users: Passengers");
        userID = user.getUid();

        EditText nameEditText = (EditText) view.findViewById(R.id.name);
        EditText phoneNumberEditText = (EditText) view.findViewById(R.id.destination);

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
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
        EditText name = (EditText) view.findViewById(R.id.name);
        EditText phoneNumber = (EditText) view.findViewById(R.id.destination);

        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = name.getText().toString();
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
                        public void onSuccess(Void aVoid) {   // Write was successful!
                            Toast.makeText(getActivity().getApplicationContext(), "Update for name is successful", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {// Write failed
                            Toast.makeText(getActivity().getApplicationContext(), "Update failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }
                if (number.isEmpty()) {
                    phoneNumber.setError("Phone number is required!");
                    phoneNumber.requestFocus();
                    return;

                }
                else if (!number.matches("[0][0-9]{9}")) {
                    phoneNumber.setError("Correct format for phone number is: 0xx xxx xxxx");
                    phoneNumber.requestFocus();
                    return;
                }
                else {
                    String userID = user.getUid();
                    DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Passengers").child(userID);
                    fireDB.child("phoneNumber").setValue(number).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {      // Write was successful!
                            Toast.makeText(getActivity().getApplicationContext(), "Update for phone number is successful", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {// Write failed
                            Toast.makeText(getActivity().getApplicationContext(), "Update failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

        });

        return view;
    }
}