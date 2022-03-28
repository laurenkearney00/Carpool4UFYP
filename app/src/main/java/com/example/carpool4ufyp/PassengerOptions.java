package com.example.carpool4ufyp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class PassengerOptions extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    private Button logout;
    private static final String CHANNEL_ID = "ID";
    private static final CharSequence CHANNEL_NAME = "name";
    private static final String CHANNEL_DES = "Des";
    DatabaseReference databaseReference;
    private String text;
    private String sender;
    private String driver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_options);

        logout = (Button) findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PassengerOptions.this, SignUpPassenger.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users: Passengers");
        userID = user.getUid();

        final TextView greetingTextView = (TextView) findViewById(R.id.greeting);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserPassenger userProfile = snapshot.getValue(UserPassenger.class);

                if(userProfile != null) {
                    String name = userProfile.fullName;
                    greetingTextView.setText("Welcome " + name + "!" + "\n" + "View Passenger Menu Options");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PassengerOptions.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DES);
// Register the channel with the system; you can't change the importance
// or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
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
                    sender = notification.getSender();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(sender);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserDriver userProfile = snapshot.getValue(UserDriver.class);

                            if (userProfile != null) {
                                driver = userProfile.fullName;
// create pending intent
                                Intent intent = new Intent(getApplicationContext(), ViewDrivers.class);
                               // intent.putExtra(KEY1, sender);
                                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
// in case there is no pending intent/action, remove setContentIntent
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                                        CHANNEL_ID)
                                        .setSmallIcon(android.R.drawable.stat_notify_chat)
                                        .setContentTitle(driver)
                                        .setContentText(text)
                                        .setContentIntent(pendingIntent);
                                NotificationManagerCompat notificationManager =
                                        NotificationManagerCompat.from(getApplicationContext());
// notificationId is a unique int for each notification that you must define
                                int notificationId = 0;
                                notificationManager.notify(notificationId, builder.build());

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                           // Toast.makeText(DriverOptions.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu_passenger, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(),
                Toast.LENGTH_SHORT).show();

        if (item.getItemId() == R.id.profilePassenger) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, ProfilePassenger.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.weatherUpdate) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, WeatherUpdate.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.map) {
            //do suitable action, e.g.start an activity
            Intent intent = new Intent(this, ViewDrivers.class);
            startActivity(intent);
        }


        return true;
    }
}
