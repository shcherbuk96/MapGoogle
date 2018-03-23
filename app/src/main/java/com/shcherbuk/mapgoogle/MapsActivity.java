package com.shcherbuk.mapgoogle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shcherbuk.mapgoogle.pojo.Coordinates;
import com.shcherbuk.mapgoogle.pojo.CoordinatesList;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,Constants {
    static final int REQUEST_LOCATION = 1;
/*    double lati;
    double longi;*/
    List<Coordinates> list=new ArrayList<>();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        readDB();

    }

    private void readDB(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CoordinatesList coordinatesList=dataSnapshot.getValue(CoordinatesList.class);
                if (coordinatesList!=null){
                    Log.i("coordinatesList", String.valueOf(coordinatesList));
                    List<Coordinates> coordinates=coordinatesList.getList();
                    Log.i("coordinates", String.valueOf(coordinates.size()));
                    if(coordinates!=null){
                        Log.i("coordinatesList","2");
                        for(Coordinates coordinate: coordinatesList.getList()){
                            Log.i("coordinatesList",coordinate.toString());
                            PolylineOptions line=
                                    new PolylineOptions().add(new LatLng(coordinate.getStart_lati(),coordinate.getStart_longi()),
                                            new LatLng(coordinate.getFinish_lati(),coordinate.getFinish_longi()))
                                            .width(5).color(Color.RED);

                            mMap.addPolyline(line);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        rootRef.addListenerForSingleValueEvent(postListener);

/*        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53,53),15));
    }
}
