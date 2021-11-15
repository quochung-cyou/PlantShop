package com.quochung.plantshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quochung.plantshop.R;
import com.quochung.plantshop.model.PlantCheckOut;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    private Context context;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<PlantCheckOut> plantlist;
    public CartAdapter(Context context , ArrayList<PlantCheckOut> plant){
        this.context = context;
        plantlist = plant;
    }
    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plant_checkout , parent , false);
        return new CartHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        PlantCheckOut plant = plantlist.get(position);
        holder.name.setText(plant.getName());
        holder.quantity.setText(String.valueOf(plant.getQuantity()));
        Glide.with(context).load(plant.getUrl()).placeholder(R.drawable.peacelily).into(holder.image);
        holder.price.setText(plant.getPrice().toString() + " VND");
        holder.addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useruid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if have the plant in cart already, then add 1
                        if(dataSnapshot.child("users").child(useruid).hasChild(plant.getName())){
                            if (dataSnapshot.child("users").child(useruid).child(plant.getName()).getValue() instanceof  Long) {
                                Long newamount = ((Long) dataSnapshot.child("users").child(useruid).child(plant.getName()).getValue()).longValue() + 1;

                                databaseReference.child("users").child(useruid).child(plant.getName()).setValue(newamount);
                            }
                        } else {
                            databaseReference.child("users").child(useruid).child(plant.getName()).setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toasty.error(view.getContext(), "Error when add item to cart", Toast.LENGTH_SHORT, true).show();
                    }
                });
            }
        });

        holder.subbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String useruid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if have the plant in cart already, then add 1
                        if(dataSnapshot.child("users").child(useruid).hasChild(plant.getName())){
                            if (dataSnapshot.child("users").child(useruid).child(plant.getName()).getValue() instanceof  Long) {
                                Long newamount = ((Long) dataSnapshot.child("users").child(useruid).child(plant.getName()).getValue()).longValue() - 1;
                                if (newamount < 1) {
                                    databaseReference.child("users").child(useruid).child(plant.getName()).removeValue();
                                } else {
                                    databaseReference.child("users").child(useruid).child(plant.getName()).setValue(newamount);
                                }
                            }
                        } else {
                            databaseReference.child("users").child(useruid).child(plant.getName()).setValue(1);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toasty.error(view.getContext(), "Error when add item to cart", Toast.LENGTH_SHORT, true).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return plantlist.size();
    }



    public class CartHolder extends RecyclerView.ViewHolder {

        TextView name, price, quantity;
        ImageView image, addbutton, subbutton;

        public CartHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.plantnamecheckout);
            image = itemView.findViewById(R.id.imagecheckout);
            price = itemView.findViewById(R.id.pricecheckout);
            quantity = itemView.findViewById(R.id.quantity);
            addbutton = itemView.findViewById(R.id.addbutton);
            subbutton = itemView.findViewById(R.id.subbutton);

        }
    }



}
