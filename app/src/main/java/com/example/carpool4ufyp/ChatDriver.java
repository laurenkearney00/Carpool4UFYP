package com.example.carpool4ufyp;


import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ChatDriver extends AppCompatActivity implements View.OnClickListener {

    private static final String CHANNEL_ID = "ID";
    private FirebaseAuth mAuth1;
    private ImageView messageButton;
    private EditText messageString;
    private ProgressBar progressBar;
    String receiver;
    public ArrayList<Message> list = new ArrayList<>();
    DatabaseReference databaseReference, reference;
    ProgressDialog progressDialog;
    RecyclerView mRecyclerView;
    EditText editText;
    public static ChatDriverAdapter myAdapter;
    private String timestamp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_driver);

        mAuth1 = FirebaseAuth.getInstance();

        messageButton = (ImageView) findViewById(R.id.messageButton);
        messageButton.setOnClickListener(this);

        TextView itemtext = (TextView) findViewById(R.id.edittxt_item);

        messageString = (EditText) findViewById(R.id.message);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        //driverName = intent.getStringExtra(ViewDrivers.KEY2);
        receiver = intent.getStringExtra(PassengerOptions.KEY1);

        reference = FirebaseDatabase.getInstance().getReference("Users: Drivers");
        reference.child(receiver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //every time change data the event listener will execute ondatachangemethod
                UserDriver userDriver = snapshot.getValue(UserDriver.class);

                if (userDriver != null) {
                    String driverName = userDriver.fullName;
                    itemtext.setText(driverName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatDriver.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
        showChat();
    }

    public void showChat() {


        mRecyclerView = findViewById(R.id.my_recycler_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        myAdapter = new ChatDriverAdapter(ChatDriver.this, list);


        mRecyclerView.setAdapter(myAdapter);


        progressDialog = new ProgressDialog(ChatDriver.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(receiver).child("Messages");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Message message = dataSnapshot.getValue(Message.class);

                    list.add(message);


                }


                myAdapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.messageButton:
                insertMessage();
                break;
        }
    }

    private void insertMessage() {
        progressBar.setVisibility(View.VISIBLE);

        String text = messageString.getText().toString().trim();

        if (text.isEmpty()) {
            messageString.setError("Message is required!");
            messageString.requestFocus();
            return;
        } else {

            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference().child("Messages");
            String messageID = fireDB.push().getKey();
            String sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            timestamp = simpleDateFormat.format(calendar.getTime());

            DatabaseReference DB = FirebaseDatabase.getInstance().getReference().child("Notifications");
            String notificationID = DB.push().getKey();
            Notification notification = new Notification(text, receiver, sender, timestamp);

            FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(receiver).child("Notifications")
                    .child(notificationID)
                    .setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChatDriver.this, "Message sent to driver", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    } else
                        Toast.makeText(ChatDriver.this, "Failed to send message! Try again!", Toast.LENGTH_LONG).show();
                }

            });

            Message message = new Message(text, receiver, sender, timestamp);

            FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(receiver).child("Messages")
                    .child(messageID)
                    .setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChatDriver.this, "Message sent to driver", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        //MessageDriver.myAdapter.addItemtoEnd(message);
                        ChatDriver.myAdapter.addItemtoEnd(message);
                        ChatDriver.myAdapter.updateList(list);
                        showChat();

                        //Intent intent = new Intent(MessageDriver.this, ViewDrivers.class);
                        //startActivity(intent);
                    } else
                        Toast.makeText(ChatDriver.this, "Failed to send message! Try again!", Toast.LENGTH_LONG).show();
                }

            });


        }
    }

}









