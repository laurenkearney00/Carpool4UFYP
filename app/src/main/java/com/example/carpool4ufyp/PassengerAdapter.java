package com.example.carpool4ufyp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class PassengerAdapter extends RecyclerView.Adapter<PassengerAdapter.MyViewHolder> {
    private ArrayList<UserPassenger> list;
    public static final String MESSAGE_KEY1 ="text";
    public static final String MESSAGE_KEY2 ="position";
    public static final String MESSAGE_KEY3 ="passengerID";
    public static final String MESSAGE_KEY4 ="userID";
    public static final String MESSAGE_KEY5 ="timestamp";


    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;


        public MyViewHolder(View itemView) {
            super(itemView); //itemView corresponds to all views defined in row layout

            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick (View view){
            int position = this.getLayoutPosition();
            String name = list.get(position).getFullName();
            String passengerID = list.get(position).getPassengerID();
            Intent intent= new Intent(view.getContext(), MessagePassenger.class);
            intent.putExtra(MESSAGE_KEY1 , name);
            intent.putExtra(MESSAGE_KEY3 , passengerID);
            intent.putExtra(MESSAGE_KEY2, position);
            view.getContext().startActivity(intent);

        }
    }

    public PassengerAdapter(DisplayPassengers displayPassengers, ArrayList<UserPassenger> passengers) {
        list = passengers;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PassengerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.item_passengers, parent, false);
        //false: inflate the row
        // layout to parent and return view, if true return parent+view
        MyViewHolder viewHolder= new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // -get element from your dataset at this position
        // -replace the contents of the view with that element


        UserPassenger userPassenger = list.get(position);

        holder.textView.setText(userPassenger.getFullName());
        //holder.textView2.setText("Driver's ID: " + message.getReceiver());
        //holder.textView3.setText("Passenger's ID: " + message.getSender());
        //holder.textView4.setText("Timestamp: " + message.getTimestamp());



    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return list.size();
        //return mylistvalues.size();
    }

    public void filterList(ArrayList<UserPassenger> filteredList) {
        list = filteredList;
        notifyDataSetChanged();

    }

    public void addItemtoEnd(UserPassenger userPassenger){ //these functions are user-defined
        list.add(userPassenger);
        notifyItemInserted(list.size());
    }








}