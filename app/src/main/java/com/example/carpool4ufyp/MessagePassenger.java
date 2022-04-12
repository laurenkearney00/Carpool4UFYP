package com.example.carpool4ufyp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


public class MessagePassenger extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY = "passengerID";
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
    public static MessagePassengerAdapter myAdapter;
    private String timestamp;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Button remove, cancel;
    private String messageID;
    private String sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_passenger);

        remove = (Button) findViewById(R.id.profile);
        cancel = (Button) findViewById(R.id.message);

        mAuth1 = FirebaseAuth.getInstance();

        messageButton = (ImageView) findViewById(R.id.messageButton);
        messageButton.setOnClickListener(this);

        TextView itemtext = (TextView) findViewById(R.id.edittxt_item);

        messageString = (EditText) findViewById(R.id.messagetv);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        receiver = intent.getStringExtra(DriverHomeFragment.KEY1);

        reference = FirebaseDatabase.getInstance().getReference("Users: Passengers");
        reference.child(receiver).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { //every time change data the event listener will execute ondatachangemethod
                UserPassenger userPassenger = snapshot.getValue(UserPassenger.class);

                if (userPassenger != null) {
                    String passengerName = userPassenger.fullName;
                    itemtext.setText(passengerName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessagePassenger.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });
        showChat();
    }

    public void showChat() {


        mRecyclerView = findViewById(R.id.my_recycler_view);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        myAdapter = new MessagePassengerAdapter(MessagePassenger.this, list);


        mRecyclerView.setAdapter(myAdapter);


        progressDialog = new ProgressDialog(MessagePassenger.this);

        progressDialog.setMessage("Loading Data from Firebase Database");

        progressDialog.show();
        sender = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(sender).child("Messages");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    Message message = dataSnapshot.getValue(Message.class);

                    if (message.getReceiver().equals(receiver) || message.getSender().equals(receiver)) {

                        list.add(message);
                    }


                }


                myAdapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();

            }
        });

        myAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int position, Message message) {
                builder = new AlertDialog.Builder(MessagePassenger.this);
                message.getMessage();
                builder.setTitle(message.getMessage());
                builder.setCancelable(false);
                View view = LayoutInflater.from(MessagePassenger.this).inflate(R.layout.message_dialog, null, false);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();

                messageID = message.getMessageID();

                remove = view.findViewById(R.id.profile);
                cancel = view.findViewById(R.id.message);

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        delete();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void delete() {
        sender = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(sender).child("Messages").child(messageID);
        fireDB.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {      // Write was successful!
                Toast.makeText(MessagePassenger.this, "Removal successful", Toast.LENGTH_LONG).show();
                MessagePassenger.myAdapter.updateList(list);
                showChat();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {// Write failed
                Toast.makeText(MessagePassenger.this, "Removal failed",
                        Toast.LENGTH_LONG).show();
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

            FirebaseDatabase.getInstance().getReference().child("Users: Passengers").child(receiver).child("Notifications")
                    .child(notificationID)
                    .setValue(notification).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MessagePassenger.this, "Message sent to passenger", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    } else
                        Toast.makeText(MessagePassenger.this, "Failed to send message! Try again!", Toast.LENGTH_LONG).show();
                }

            });

            Message message = new Message(text, receiver, sender, timestamp, messageID);

            FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(sender).child("Messages")
                    .child(messageID)
                    .setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MessagePassenger.this, "Message sent to passenger", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        MessagePassenger.myAdapter.addItemtoEnd(message);
                        MessagePassenger.myAdapter.updateList(list);
                        showChat();

                    } else {
                        Toast.makeText(MessagePassenger.this, "Failed to send message! Try again!", Toast.LENGTH_LONG).show();
                    }
                }

            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.booking_for_passenger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(),
                Toast.LENGTH_SHORT).show();

        if (item.getItemId() == R.id.bookingForPassenger) {
            //do suitable action, e.g.start an activity
            Intent i = new Intent(this, BookingForPassenger.class);
            i.putExtra(KEY, receiver);
            startActivity(i);
        }
        return true;
    }

}
