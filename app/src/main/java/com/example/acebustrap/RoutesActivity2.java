package com.example.acebustrap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;

import com.example.acebustrap.Model.LocationCoordinates;
import com.example.acebustrap.Model.NewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoutesActivity2 extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference ref;
    NewAdapter newAdapter;
    ArrayList<LocationCoordinates> list;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes2);
        recyclerView=findViewById(R.id.RouteList);
        ref= FirebaseDatabase.getInstance().getReference("BUS ROUTES");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        newAdapter=new NewAdapter(this,list);
        recyclerView.setAdapter(newAdapter);
        getdata();
        swipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                newAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                getdata();
            }
        });
    }

    private void getdata() {
        list.clear();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    LocationCoordinates coordinates=dataSnapshot.getValue(LocationCoordinates.class);
                    list.add(coordinates);
                }
                newAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}