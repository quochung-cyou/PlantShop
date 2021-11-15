package com.quochung.plantshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.quochung.plantshop.R;
import com.quochung.plantshop.adapter.PlantAdapter;
import com.quochung.plantshop.model.Plant;

import java.util.ArrayList;


public class PlantOverview extends Fragment  {

    AnimatedRecyclerView recyclerView;

    DatabaseReference databaseReference;
    ArrayList<Plant> plantlist;
    PlantAdapter plantAdapter;
    View view;

    public PlantOverview() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plant_overview, container, false);
        initUI();
        updateData();



        return view;


    }


    protected  void initUI() {
        recyclerView = view.findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("plant");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
        plantlist = new ArrayList<Plant>();
        plantAdapter = new PlantAdapter(this.getContext(), plantlist);
        recyclerView.setAdapter(plantAdapter);
    }

    protected  void updateData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Plant plant = dataSnapshot.getValue(Plant.class);
                    plantlist.add(plant);
                }
                plantAdapter.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}