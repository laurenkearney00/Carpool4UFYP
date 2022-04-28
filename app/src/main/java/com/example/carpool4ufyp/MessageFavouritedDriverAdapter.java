package com.example.carpool4ufyp;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageFavouritedDriverAdapter extends RecyclerView.Adapter<MessageFavouritedDriverAdapter.MyViewHolder> {
    private ArrayList<Message> list;
    ItemClickListener itemClickListener;
    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPR_RIGHT=1;

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    // Provide a reference to the views for each data item
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView); //itemView corresponds to all views defined in row layout

            textView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick (View view){
            int position = this.getLayoutPosition();
            String message = list.get(position).getMessage();

        }
    }

    public MessageFavouritedDriverAdapter(MessageFavouritedDriver messageFavouritedDriver, ArrayList<Message> messages) {
        list = messages;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessageFavouritedDriverAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        if(viewType==MSG_TYPE_LEFT){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView= inflater.inflate(R.layout.right_chat, parent, false);
            MyViewHolder viewHolder= new MyViewHolder(itemView);
            return viewHolder;
        }
        else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.item_message, parent, false);
            //false: inflate the row
            // layout to parent and return view, if true return parent+view
            MyViewHolder viewHolder = new MyViewHolder(itemView);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // -get element from your dataset at this position
        // -replace the contents of the view with that element

        String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Message message = list.get(position);

        holder.textView.setText("   " + message.getMessage() + "\n" + "  " + message.getTimestamp());

        if(message.getSenderID().equals(senderID)) {
            holder.textView.setBackgroundResource(R.drawable.sender);
            holder.textView.setTextColor(Color.BLACK);

        }
        else {
            holder.textView.setBackgroundResource(R.drawable.receiver);
            holder.textView.setTextColor(Color.BLACK);

        }
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.OnItemClick(position, message);
            }
        });

    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addItemtoEnd(Message message){ //these functions are user-defined
        list.add(message);
        notifyItemInserted(list.size());
    }


    public void updateList(ArrayList<Message> list) {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(list.get(position).getSenderID().equals(senderID)){
            return MSG_TYPE_LEFT;
        }
        else {
            return MSG_TYPR_RIGHT;
        }
    }

}
