package com.example.carpool4ufyp;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {
    private ArrayList<Car> list;

    public static final String MESSAGE_KEY1 ="licenceNumber";
    public static final String MESSAGE_KEY2 ="position";
    public static final String MESSAGE_KEY3 ="registrationNumber";
    public static final String MESSAGE_KEY4 ="numberOfSeats";
    public static final String MESSAGE_KEY5 ="licenceExpiration";
    public static final String MESSAGE_KEY6 ="makeAndModel";
    public static final String MESSAGE_KEY7 ="colour";
    public static final String MESSAGE_KEY8 ="carID";

    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;
        public TextView textView4;
        public TextView textView5;
        public TextView textView6;


        public MyViewHolder(View itemView) {
            super(itemView); //itemView corresponds to all views defined in row layout

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = this.getLayoutPosition();
            String licenceNumber = list.get(position).getLicenceNumber();
            String registrationNumber = list.get(position).getRegistrationNumber();
            String numberOfSeats = list.get(position).getNumberOfSeats();
            String licenceExpiration = list.get(position).getLicenceExpiration();
            String makeAndModel = list.get(position).getMakeAndModel();
            String colour = list.get(position).getColour();
            String carID = list.get(position).getCarID();
            Intent intent= new Intent(view.getContext(), ViewCarDetails.class);
            intent.putExtra(MESSAGE_KEY1 , licenceNumber);
            intent.putExtra(MESSAGE_KEY2, position);
            intent.putExtra(MESSAGE_KEY3, registrationNumber);
            intent.putExtra(MESSAGE_KEY4, numberOfSeats);
            intent.putExtra(MESSAGE_KEY5, licenceExpiration);
            intent.putExtra(MESSAGE_KEY6, makeAndModel);
            intent.putExtra(MESSAGE_KEY7, colour);
            intent.putExtra(MESSAGE_KEY8, carID);
            view.getContext().startActivity(intent);
        }



    }





    public CarAdapter(DisplayCar displayCars, ArrayList<Car> cars) {
        list = cars;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CarAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView= inflater.inflate(R.layout.item_car, parent, false);
        //false: inflate the row
        // layout to parent and return view, if true return parent+view
        MyViewHolder viewHolder= new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // -get element from your dataset at this position
        // -replace the contents of the view with that element


        Car car = list.get(position);

        holder.textView1.setText("Licence Number: " + car.getLicenceNumber());
        holder.textView2.setText("Registration Number: " + car.getRegistrationNumber());
        holder.textView3.setText("Number of Seats: " + car.getNumberOfSeats());
        holder.textView4.setText("Licence Expiration: " + car.getLicenceExpiration());
        holder.textView5.setText("Make and Model: " + car.getMakeAndModel());
        holder.textView6.setText("Colour: " + car.getColour());

    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return list.size();
        //return mylistvalues.size();
    }




    public void filterList(ArrayList<Car> filteredList) {
        list = filteredList;
        notifyDataSetChanged();

    }

    public void UpdateCar(int position, Car car){

        list.remove(position);
        list.add(car);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }




}

