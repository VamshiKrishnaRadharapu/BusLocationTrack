package com.example.acebustrap.Model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acebustrap.GMapActivity;
import com.example.acebustrap.R;

import java.util.ArrayList;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.NewViewHolder> {
    Context context;
    ArrayList<LocationCoordinates> list;

    public NewAdapter(Context context, ArrayList<LocationCoordinates> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.route_item, parent, false);
        return new NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder holder, int position) {
        LocationCoordinates coordinates = list.get(position);
        holder.RouteTv.setText(coordinates.getName());
        if (coordinates.getBusStatus().equals("ONLINE")) {
            holder.StatusTv.setImageResource(R.drawable.busicongreen);
        } else {
            holder.StatusTv.setImageResource(R.drawable.busiconred);
        }
        holder.newLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(coordinates.getBusStatus().equals("ONLINE")) {
                    Intent intent = new Intent(context, GMapActivity.class);
                    intent.putExtra("Vamshi", coordinates.getName());
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, coordinates.getName()+":- Not Activated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NewViewHolder extends RecyclerView.ViewHolder {
        TextView RouteTv;
        ImageView StatusTv;
        ConstraintLayout newLayout;

        public NewViewHolder(@NonNull View itemView) {
            super(itemView);
            newLayout = itemView.findViewById(R.id.NewDataLayout);
            RouteTv = itemView.findViewById(R.id.RouteName);
            StatusTv = itemView.findViewById(R.id.RouteImageView);
        }
    }
}