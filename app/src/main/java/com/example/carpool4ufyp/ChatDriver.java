package com.example.carpool4ufyp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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


public class ChatDriver extends AppCompatActivity implements View.OnClickListener {

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
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Button remove, cancel;
    private String messageID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_driver);


        remove = (Button) findViewById(R.id.profile);
        cancel = (Button) findViewById(R.id.message);


        mAuth1 = FirebaseAuth.getInstance();

        messageButton = (ImageView) findViewById(R.id.messageButton);
        messageButton.setOnClickListener(this);

        TextView itemtext = (TextView) findViewById(R.id.edittxt_item);

        messageString = (EditText) findViewById(R.id.messagetv);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent intent = getIntent();
        receiver = intent.getStringExtra(PassengerHomeFragment.KEY1);

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

        myAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void OnItemClick(int position, Message message) {
                builder = new AlertDialog.Builder(ChatDriver.this);
                message.getMessage();
                builder.setTitle(message.getMessage());
                builder.setCancelable(false);
                View view = LayoutInflater.from(ChatDriver.this).inflate(R.layout.message_dialog, null, false);
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
            DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users: Drivers").child(receiver).child("Messages").child(messageID);
            fireDB.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {      // Write was successful!
                    Toast.makeText(ChatDriver.this, "Removal successful", Toast.LENGTH_LONG).show();
                    ChatDriver.myAdapter.updateList(list);
                    showChat();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {// Write failed
                    Toast.makeText(ChatDriver.this, "Removal failed",
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

            Message message = new Message(text, receiver, sender, timestamp, messageID);

            FirebaseDatabase.getInstance().getReference().child("Users: Drivers").child(receiver).child("Messages")
                    .child(messageID)
                    .setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ChatDriver.this, "Message sent to driver", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        ChatDriver.myAdapter.addItemtoEnd(message);
                        ChatDriver.myAdapter.updateList(list);
                        showChat();

                    } else
                        Toast.makeText(ChatDriver.this, "Failed to send message! Try again!", Toast.LENGTH_LONG).show();
                }

            });


        }
    }

}