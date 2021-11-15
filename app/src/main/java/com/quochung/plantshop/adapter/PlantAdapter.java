package com.quochung.plantshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.quochung.plantshop.model.Plant;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantHolder> {

    private Context context;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Plant> plantlist;
    public PlantAdapter(Context context , ArrayList<Plant> plant){
        this.context = context;
        plantlist = plant;
    }
    @NonNull
    @Override
    public PlantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plant , parent , false);
        return new PlantHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull PlantHolder holder, int position) {
        Plant plant = plantlist.get(position);
        holder.name.setText(plant.getName());
        holder.desc.setText(plant.getDesc());
        holder.type.setText(plant.getType());
        Glide.with(context).load(plant.getUrl()).placeholder(R.drawable.peacelily).into(holder.image);
        holder.relativeLayout.setBackgroundColor(Color.parseColor(plant.getColor()));
        holder.price.setText("Buy for " + plant.getPrice().toString() + " VND");
        holder.price.setOnClickListener(new View.OnClickListener() {
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
                                Toasty.success(view.getContext(), "Added to cart. " + plant.getName() + ": " + newamount, Toast.LENGTH_SHORT, true).show();
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

    }

    @Override
    public int getItemCount() {
        return plantlist.size();
    }



    public class PlantHolder extends RecyclerView.ViewHolder {

        TextView name, desc, type, price;
        ImageView image;
        RelativeLayout relativeLayout;

        public PlantHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.plantname);
            desc = itemView.findViewById(R.id.plantdesc);
            image = itemView.findViewById(R.id.image);
            type = itemView.findViewById(R.id.planttype);
            price = itemView.findViewById(R.id.plantprice);
            relativeLayout = itemView.findViewById(R.id.relativelayout);
        }
    }



}
