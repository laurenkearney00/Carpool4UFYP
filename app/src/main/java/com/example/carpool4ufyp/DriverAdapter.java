package com.example.carpool4ufyp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.MyViewHolder> {
    private ArrayList<UserDriver> list;
    public static final String MESSAGE_KEY1 ="text";
    public static final String MESSAGE_KEY2 ="position";
    public static final String MESSAGE_KEY3 ="driverID";
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
            String driverID = list.get(position).getDriverID();
            Intent intent= new Intent(view.getContext(), MessageDriver.class);
            intent.putExtra(MESSAGE_KEY1 , name);
            intent.putExtra(MESSAGE_KEY3 , driverID);
            intent.putExtra(MESSAGE_KEY2, position);
            view.getContext().startActivity(intent);

        }
    }

    public DriverAdapter(DisplayDrivers displayDrivers, ArrayList<UserDriver> drivers) {
        list = drivers;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DriverAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.item_drivers, parent, false);
        //false: inflate the row
        // layout to parent and return view, if true return parent+view
        MyViewHolder viewHolder= new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // -get element from your dataset at this position
        // -replace the contents of the view with that element


        UserDriver userDriver = list.get(position);

        holder.textView.setText(userDriver.getFullName());
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

    public void filterList(ArrayList<UserDriver> filteredList) {
        list = filteredList;
        notifyDataSetChanged();

    }

    public void addItemtoEnd(UserDriver userDriver){ //these functions are user-defined
        list.add(userDriver);
        notifyItemInserted(list.size());
    }








}