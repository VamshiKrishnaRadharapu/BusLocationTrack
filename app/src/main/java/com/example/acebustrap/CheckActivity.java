package com.example.acebustrap;

import android.Manifest;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class CheckActivity extends AppCompatActivity {
    Button getCurrLocation,StartSharing,StopSharing;
    private int check1=0,check2=0,check3=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        getCurrLocation=findViewById(R.id.getLocButton);
        StartSharing=findViewById(R.id.UpdateButton);
        StopSharing=findViewById(R.id.StopLocation);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            activityFun();
            Toast.makeText(CheckActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
        }else{
            askPermission();
        }
    }

    private void askPermission() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            }
        }
    }

    private void activityFun() {
        StartSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check1=2000;
                Intent intent1=new Intent(CheckActivity.this,GMapActivity.class);
                intent1.putExtra("Start Location Code",check1);
                startActivity(intent1);
            }
        });
        getCurrLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check2=2000;
                Intent intent2=new Intent(CheckActivity.this,GMapActivity.class);
                intent2.putExtra("Get Location Code",check2);
                startActivity(intent2);
            }
        });
        StopSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check3=2000;
                Intent intent3=new Intent(CheckActivity.this,GMapActivity.class);
                intent3.putExtra("Stop Location Code",check3);
                startActivity(intent3);
            }
        });
    }
}