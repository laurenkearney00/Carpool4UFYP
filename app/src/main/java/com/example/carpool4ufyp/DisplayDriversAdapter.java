package com.example.carpool4ufyp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayDriversAdapter extends RecyclerView.Adapter<DisplayDriversAdapter.MyViewHolder> {
    private ArrayList<UserDriver> list;
    private DriverClickListener driverClickListener;

    public void setOnDriverClickListener(DriverClickListener driverClickListener){
        this.driverClickListener = driverClickListener;
    }

    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView1;
        public ImageView imgview;

        public MyViewHolder(View itemView) {
            super(itemView); //itemView corresponds to all views defined in row layout

            textView1 = itemView.findViewById(R.id.textView1);
            imgview = itemView.findViewById(R.id.imgview);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = this.getLayoutPosition();
            String fullName = list.get(position).getFullName();
            String driverID = list.get(position).getDriverID();
        }
    }

    public DisplayDriversAdapter(DisplayDrivers displayDrivers, ArrayList<UserDriver> drivers) {
        list = drivers;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DisplayDriversAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        holder.textView1.setText(userDriver.getFullName() +  "\n"  + userDriver.getPhoneNumber());

        holder.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverClickListener.OnDriverClick(position, userDriver);
            }
        });
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<UserDriver> filteredList) {
        list = filteredList;
        notifyDataSetChanged();

    }

}