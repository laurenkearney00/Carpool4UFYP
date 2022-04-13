package com.example.carpool4ufyp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {
    private ArrayList<Trip> list;

    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public TextView textView5;
        public TextView textView6;
        public TextView textView7;

        public MyViewHolder(View itemView) {
            super(itemView); //itemView corresponds to all views defined in row layout

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
            textView7 = itemView.findViewById(R.id.textView7);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = this.getLayoutPosition();
        }

    }

    public TripAdapter(ViewTrips viewTrips, ArrayList<Trip> trips) {
        list = trips;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TripAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.item_trip, parent, false);
        //false: inflate the row
        // layout to parent and return view, if true return parent+view
        MyViewHolder viewHolder= new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // -get element from your dataset at this position
        // -replace the contents of the view with that element


        Trip trip = list.get(position);

        holder.textView1.setText("Driver name: " + trip.getDriver());
        holder.textView2.setText("Passenger name: " + trip.getPassenger());
        holder.textView3.setText("Meeting point: " + trip.getMeetingPoint());
        holder.textView4.setText("Destination: " + trip.getDestination());
        holder.textView5.setText("Date: " + trip.getDate());
        holder.textView6.setText("Pickup time: " + trip.getPickupTime());
        holder.textView7.setText("Amount charged to passenger: €" + trip.getPrice());


    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return list.size();
        //return mylistvalues.size();
    }




    public void filterList(ArrayList<Trip> filteredList) {
        list = filteredList;
        notifyDataSetChanged();

    }
}

