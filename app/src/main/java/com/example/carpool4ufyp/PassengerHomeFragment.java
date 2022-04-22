package com.example.carpool4ufyp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PassengerHomeFragment extends Fragment {


    public static final String KEY1 = "DriverID";
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button logout;
    private static final String CHANNEL_ID = "ID";
    private static final CharSequence CHANNEL_NAME = "name";
    private static final String CHANNEL_DES = "Des";
    DatabaseReference databaseReference;
    private String text;
    private String senderID;
    private String driverID;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerHomeFragment() {
        // Required empty public constructor
    }


    public static PassengerHomeFragment newInstance(String param1, String param2) {
        PassengerHomeFragment fragment = new PassengerHomeFragment();
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
        View view = inflater.inflate(R.layout.activity_passenger_home_fragment, container, false);

        logout = (Button) view.findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users: Passengers");
        userID = user.getUid();

        final TextView greetingTextView = (TextView) view.findViewById(R.id.greeting);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserPassenger userProfile = snapshot.getValue(UserPassenger.class);

                if(userProfile != null) {
                    String name = userProfile.fullName;
                    greetingTextView.setText("Welcome " + name + " to Carpool4U" + "\n" + "View Passenger Menu Options");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(HomeFragment.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DES);
// Register the channel with the system; you can't change the importance
// or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users: Passengers").child(userID).child("Notifications");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Notification notification = dataSnapshot.getValue(Notification.class);

                    text = notification.getMessage();
                    senderID = notification.getSenderID();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(senderID);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserDriver userProfile = snapshot.getValue(UserDriver.class);

                            if (userProfile != null) {
                                driverID = userProfile.fullName;
// create pending intent
                                Intent intent = new Intent(getActivity().getApplicationContext(), ChatDriver.class);
                                intent.putExtra(KEY1, senderID);
                                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(),
                                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
// in case there is no pending intent/action, remove setContentIntent
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity().getApplicationContext(),
                                        CHANNEL_ID)
                                        .setSmallIcon(android.R.drawable.stat_notify_chat)
                                        .setContentTitle(driverID)
                                        .setContentText(text)
                                        .setAutoCancel(true)
                                        .setContentIntent(pendingIntent);
                                NotificationManagerCompat notificationManager =
                                        NotificationManagerCompat.from(getActivity().getApplicationContext());
// notificationId is a unique int for each notification that you must define
                                int notificationId = 0;
                                notificationManager.notify(notificationId, builder.build());

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity().getApplicationContext(), "Something wrong happened!", Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

}