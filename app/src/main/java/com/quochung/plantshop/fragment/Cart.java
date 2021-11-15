package com.quochung.plantshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.quochung.plantshop.R;
import com.quochung.plantshop.adapter.CartAdapter;
import com.quochung.plantshop.model.PlantCheckOut;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Cart extends Fragment {
    private View view;
    private AnimatedRecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<PlantCheckOut> plantlist, plantlisttemp;
    CartAdapter cartAdapter;
    TextView total;
    String useruid;
    Boolean alreadyhave;
    Long totalamount;
    public Cart() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        useruid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("users").hasChild(useruid)) {

                    initUI();
                    updateData();
                } else {
                    Toasty.error(view.getContext(), "You not have anything in cart yet ", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.error(view.getContext(), "Error when getting cart ", Toast.LENGTH_SHORT, true).show();
            }
        });
        return view;
    }

    private void initUI() {
        recyclerView = view.findViewById(R.id.recyclerViewcart);
        total = view.findViewById(R.id.total);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        plantlist = new ArrayList<PlantCheckOut>();
        plantlisttemp = new ArrayList<PlantCheckOut>();
        cartAdapter = new CartAdapter(this.getContext(), plantlist);
        recyclerView.setAdapter(cartAdapter);

    }
    private void updateData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalamount = Long.valueOf(0);

                //Prevent dupe item
                if (plantlist.isEmpty()) {
                    alreadyhave = false;
                    plantlisttemp.clear();
                } else {
                    alreadyhave = true;
                    plantlisttemp.clear();
                }
                //Clear the old list

                for (DataSnapshot plantsnapshot : snapshot.child("users").child(useruid).getChildren()) {
                    String plantname = plantsnapshot.getKey();
                    //Get the name of plant user added to cart
                    //Get that plant data
                    PlantCheckOut plantCheckOut = snapshot.child("plant").child(plantname).getValue(PlantCheckOut.class);
                    Long quantity = (Long) snapshot.child("users").child(useruid).child(plantname).getValue();
                    plantCheckOut.setQuantity(quantity);
                    plantlist.add(plantCheckOut);
                    plantlisttemp.add(plantCheckOut);
                    totalamount = totalamount + plantCheckOut.getPrice()*quantity;


                }

                //If the plantlist already have item, clear it and switch to plantlisttemp to make smooth transition
                if (alreadyhave) {
                    plantlist.clear();
                    for (PlantCheckOut plantmodel : plantlisttemp) {
                        plantlist.add(plantmodel);
                    }

                    cartAdapter.notifyDataSetChanged();
                    //recyclerView.scheduleLayoutAnimation();
                    alreadyhave = false;
                } else {
                    cartAdapter.notifyDataSetChanged();
                    recyclerView.scheduleLayoutAnimation();
                }
                total.setText("Totals: " +  String.valueOf(totalamount) + " VND");


                //
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.error(view.getContext(), "Error when getting cart ", Toast.LENGTH_SHORT, true).show();
            }
        });

    }




}