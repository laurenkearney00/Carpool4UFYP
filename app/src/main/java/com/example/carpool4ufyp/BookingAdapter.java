package com.example.carpool4ufyp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {
    private ArrayList<Booking> list;

    public static final String MESSAGE_KEY1 ="driver";
    public static final String MESSAGE_KEY2 ="position";
    public static final String MESSAGE_KEY3 ="passenger";
    public static final String MESSAGE_KEY4 ="meetingPoint";
    public static final String MESSAGE_KEY5 ="destination";
    public static final String MESSAGE_KEY6 ="date";
    public static final String MESSAGE_KEY7 ="pickupTime";
    public static final String MESSAGE_KEY8 ="total";
    public static final String MESSAGE_KEY9 ="driverID";
    public static final String MESSAGE_KEY10 ="passengerID";
    public static final String MESSAGE_KEY11 ="bookingID";
    public static final String MESSAGE_KEY12 ="statusID";

    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public TextView textView5;
        public TextView textView6;
        public TextView textView7;
        public TextView textView8;

        public MyViewHolder(View itemView) {
            super(itemView); //itemView corresponds to all views defined in row layout

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
            textView7 = itemView.findViewById(R.id.textView7);
            textView8 = itemView.findViewById(R.id.textView8);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = this.getLayoutPosition();
            String driver = list.get(position).getDriverName();
            String passenger = list.get(position).getPassengerName();
            String meetingPoint = list.get(position).getMeetingPoint();
            String destination = list.get(position).getDestination();
            String date = list.get(position).getDate();
            String pickupTime = list.get(position).getPickupTime();
            double price = list.get(position).getPrice();
            String total = String.valueOf(price);
            String driverID = list.get(position).getDriverID();
            String passengerID = list.get(position).getPassengerID();
            String bookingID = list.get(position).getBookingID();
            String status = list.get(position).getStatus();
            Intent intent= new Intent(view.getContext(), Payment.class);
            intent.putExtra(MESSAGE_KEY1 , driver);
            intent.putExtra(MESSAGE_KEY2, position);
            intent.putExtra(MESSAGE_KEY3, passenger);
            intent.putExtra(MESSAGE_KEY4, meetingPoint);
            intent.putExtra(MESSAGE_KEY5, destination);
            intent.putExtra(MESSAGE_KEY6, date);
            intent.putExtra(MESSAGE_KEY7, pickupTime);
            intent.putExtra(MESSAGE_KEY8, total);
            intent.putExtra(MESSAGE_KEY9, driverID);
            intent.putExtra(MESSAGE_KEY10, passengerID);
            intent.putExtra(MESSAGE_KEY11, bookingID);
            intent.putExtra(MESSAGE_KEY12, status);
            view.getContext().startActivity(intent);
        }



    }





    public BookingAdapter(ViewPassengerBookings viewPassengerBookings, ArrayList<Booking> bookings) {
        list = bookings;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BookingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.item_booking, parent, false);
        //false: inflate the row
        // layout to parent and return view, if true return parent+view
        MyViewHolder viewHolder= new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // -get element from your dataset at this position
        // -replace the contents of the view with that element


        Booking booking = list.get(position);
        double price = booking.getPrice();
        String total = String.valueOf(price);

        holder.textView1.setText("Driver name: " + booking.getDriverName());
        holder.textView2.setText("Passenger name: " + booking.getPassengerName());
        holder.textView3.setText("Meeting point: " + booking.getMeetingPoint());
        holder.textView4.setText("Destination: " + booking.getDestination());
        holder.textView5.setText("Date: " + booking.getDate());
        holder.textView6.setText("Pickup time: " + booking.getPickupTime());
        holder.textView7.setText("Price: €" + total);
        holder.textView8.setText("Status: " + booking.getStatus());


    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return list.size();
    }




    public void filterList(ArrayList<Booking> filteredList) {
        list = filteredList;
        notifyDataSetChanged();

    }
}

