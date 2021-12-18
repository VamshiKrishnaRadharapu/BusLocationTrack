package com.example.acebustrap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.example.acebustrap.Model.LocationCoordinates;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Objects;
import java.util.Timer;

public class GMapActivity extends AppCompatActivity {
    DatabaseReference dbref, retriveDB;
    LocationRequest locationRequest;
    LocationCoordinates coordinates;
    FusedLocationProviderClient client;
    SupportMapFragment supportMapFragment;
    String NewLatitude, NewLongitude, retrieveLat, retrieveLng, currentStatus = "offline",check2;
    public Double retrieveLat1, retrieveLng1;
    public int CheckConditions = 0, CheckConditions1 = 0, CheckConditions2 = 2000, CheckConditions3 = 0;
    GoogleMap gMap;
    Marker newMarker;
    Intent intent1, intent2, intent3;
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                NewLatitude = Double.toString(location.getLatitude());
                NewLongitude = Double.toString(location.getLongitude());
               /* if (CheckConditions1 == 2000) {
                    currentStatus = "ONLINE";
                    coordinates.setBusLatitude(NewLatitude.trim());
                    coordinates.setBusLongitude(NewLongitude.trim());
                    coordinates.setBusStatus(currentStatus);
                    dbref.child("LOCATION").setValue(coordinates);
                }*/
                if (CheckConditions2 == 2000) {
                    retriveDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            retrieveLat = snapshot.child("busLatitude").getValue().toString();
                            retrieveLng = snapshot.child("busLongitude").getValue().toString();
                            retrieveLat1 = Double.parseDouble(retrieveLat);
                            retrieveLng1 = Double.parseDouble(retrieveLng);
                            CheckConditions = 3006;
                            //Toast.makeText(GMapActivity.this, "Data Retrieved", Toast.LENGTH_SHORT).show();
                            GetLocation();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(GMapActivity.this, "DataBaseError", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmap);
        client = LocationServices.getFusedLocationProviderClient(this);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MapGoogle);
        //dbref = FirebaseDatabase.getInstance().getReference().child("BUS ROUTES");
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        coordinates = new LocationCoordinates();
        //intent1 = getIntent();
        intent2 = getIntent();
        //intent3 = getIntent();
        //CheckConditions1 = intent1.getIntExtra("Start Location Code", 0);
        CheckConditions2 = intent2.getIntExtra("Get Location Code", 2000);
        //CheckConditions3 = intent3.getIntExtra("Stop Location Code", 0);
        check2=getIntent().getStringExtra("Vamshi");
        retriveDB = FirebaseDatabase.getInstance().getReference().child("BUS ROUTES").child(check2);
        if (ActivityCompat.checkSelfPermission(GMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            GetLocation();
            CheckSettingsAndRequestUpdates();
        } else {
            AskLocationUpdates();
        }
        /*if (CheckConditions3 == 2000) {
            StopLocationUpdates();
        }*/

    }

    private void StopLocationUpdates() {
        dbref.child("LOCATION").child("busLongitude").setValue("0");
        dbref.child("LOCATION").child("busLatitude").setValue("0");
        dbref.child("LOCATION").child("busStatus").setValue("OFFLINE");
        client.removeLocationUpdates(locationCallback);
    }

    private void AskLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
    }

    private void CheckSettingsAndRequestUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build();
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                StartLocationUpdates();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ResolvableApiException apiException = (ResolvableApiException) e;
                try {
                    apiException.startResolutionForResult(GMapActivity.this, 1001);
                } catch (IntentSender.SendIntentException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void StartLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void GetLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull GoogleMap googleMap) {
                            if (CheckConditions==3006){
                                //Toast.makeText(GMapActivity.this, "Getting Location", Toast.LENGTH_LONG).show();
                                LatLng latLng1=new LatLng(retrieveLat1,retrieveLng1);
                                if (newMarker==null){
                                    //Toast.makeText(GMapActivity.this, "NewMArker", Toast.LENGTH_LONG).show();
                                    MarkerOptions RetrieveOptions = new MarkerOptions().position(latLng1);
                                    RetrieveOptions.title(check2);
                                    RetrieveOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.busiconmarker));
                                    newMarker=googleMap.addMarker(RetrieveOptions);
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1,18));
                                }else{
                                    //Toast.makeText(GMapActivity.this, "Old Marker", Toast.LENGTH_LONG).show();
                                    newMarker.setPosition(latLng1);
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                                }
                            }/*else{
                                Toast.makeText(GMapActivity.this, "Just nowUpdationg", Toast.LENGTH_LONG).show();
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                MarkerOptions options = new MarkerOptions().position(latLng);
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                googleMap.addMarker(options);
                            }
                            NewLatitude=Double.toString(location.getLatitude());
                            NewLongitude=Double.toString(location.getLongitude());*/
                        }
                    });
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GetLocation();
                CheckSettingsAndRequestUpdates();
            }
        }
    }


}