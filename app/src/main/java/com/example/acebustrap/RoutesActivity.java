package com.example.acebustrap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.example.acebustrap.Model.LocationCoordinates;
import com.example.acebustrap.Model.MyAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoutesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    String Route[],CheckStatus="ONLINE";
    int images[]={R.drawable.exampleimg1_foreground,R.drawable.exampleimg1_foreground,
            R.drawable.exampleimg1_foreground,R.drawable.exampleimg1_foreground,
            R.drawable.exampleimg1_foreground,R.drawable.exampleimg1_foreground,
            R.drawable.exampleimg1_foreground,R.drawable.exampleimg1_foreground,
            R.drawable.exampleimg1_foreground,R.drawable.exampleimg1_foreground,
            R.drawable.busicon,R.drawable.busicon,R.drawable.busicon,R.drawable.busicon,
            R.drawable.busicon,R.drawable.busicon,R.drawable.busicon};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        recyclerView=findViewById(R.id.recyclerview);
        Route=getResources().getStringArray(R.array.RouteNames);
        MyAdapter myAdapter=new MyAdapter(this,Route,images);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}