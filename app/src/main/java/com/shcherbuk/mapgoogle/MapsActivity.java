package com.shcherbuk.mapgoogle;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shcherbuk.mapgoogle.pojo.Coordinates;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, Constants {

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
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                readDB();
            }
        });
        //readDB();

    }

    private void readDB() {
        if(mMap!=null){
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("coordinates");

            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Coordinates coordinates = ds.getValue(Coordinates.class);
                        if (coordinates != null) {
                                if(coordinates.getColor().contentEquals("Красный")){
                                    PolylineOptions line =
                                            new PolylineOptions().add(new LatLng(coordinates.getStart_lati(), coordinates.getStart_longi()),
                                                    new LatLng(coordinates.getFinish_lati(), coordinates.getFinish_longi()))
                                                    .width(10).color(Color.RED);
                                    mMap.addPolyline(line);
                                }
                                else if(coordinates.getColor().contentEquals("Зелёный")){
                                    PolylineOptions line =
                                            new PolylineOptions().add(new LatLng(coordinates.getStart_lati(), coordinates.getStart_longi()),
                                                    new LatLng(coordinates.getFinish_lati(), coordinates.getFinish_longi()))
                                                    .width(10).color(Color.GREEN);
                                    mMap.addPolyline(line);
                                }
                                else if(coordinates.getColor().contentEquals("Чёрный")){
                                    PolylineOptions line =
                                            new PolylineOptions().add(new LatLng(coordinates.getStart_lati(), coordinates.getStart_longi()),
                                                    new LatLng(coordinates.getFinish_lati(), coordinates.getFinish_longi()))
                                                    .width(10).color(Color.BLACK);
                                    mMap.addPolyline(line);
                                }
                                else if(coordinates.getColor().contentEquals("Синий")){
                                    PolylineOptions line =
                                            new PolylineOptions().add(new LatLng(coordinates.getStart_lati(), coordinates.getStart_longi()),
                                                    new LatLng(coordinates.getFinish_lati(), coordinates.getFinish_longi()))
                                                    .width(10).color(Color.BLUE);
                                    mMap.addPolyline(line);
                                }


                            }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



            rootRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("Change",dataSnapshot.toString());
//                    Log.i("Change",s);

                        Log.i("Change",dataSnapshot.toString());
                        Coordinates coordinates = dataSnapshot.getValue(Coordinates.class);
                        if (coordinates != null) {
                            if(coordinates.getColor().contentEquals("Красный")){
                                PolylineOptions line =
                                        new PolylineOptions().add(new LatLng(coordinates.getStart_lati(), coordinates.getStart_longi()),
                                                new LatLng(coordinates.getFinish_lati(), coordinates.getFinish_longi()))
                                                .width(15).color(Color.RED);
                                mMap.addPolyline(line);
                            }
                            else if(coordinates.getColor().contentEquals("Зелёный")){
                                PolylineOptions line =
                                        new PolylineOptions().add(new LatLng(coordinates.getStart_lati(), coordinates.getStart_longi()),
                                                new LatLng(coordinates.getFinish_lati(), coordinates.getFinish_longi()))
                                                .width(15).color(Color.GREEN);
                                mMap.addPolyline(line);
                            }
                            else if(coordinates.getColor().contentEquals("Чёрный")){
                                PolylineOptions line =
                                        new PolylineOptions().add(new LatLng(coordinates.getStart_lati(), coordinates.getStart_longi()),
                                                new LatLng(coordinates.getFinish_lati(), coordinates.getFinish_longi()))
                                                .width(15).color(Color.BLACK);
                                mMap.addPolyline(line);
                            }
                            else if(coordinates.getColor().contentEquals("Синий")){
                                PolylineOptions line =
                                        new PolylineOptions().add(new LatLng(coordinates.getStart_lati(), coordinates.getStart_longi()),
                                                new LatLng(coordinates.getFinish_lati(), coordinates.getFinish_longi()))
                                                .width(15).color(Color.BLUE);
                                mMap.addPolyline(line);
                            }
                        }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

 /*           ValueEventListener postListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Coordinates coordinates = ds.getValue(Coordinates.class);
                        if (coordinates != null) {
                            PolylineOptions line =
                                    new PolylineOptions().add(new LatLng(coordinates.getStart_lati(), coordinates.getStart_longi()),
                                            new LatLng(coordinates.getFinish_lati(), coordinates.getFinish_longi()))
                                            .width(6).color(Color.RED);

                            mMap.addPolyline(line);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            rootRef.addListenerForSingleValueEvent(postListener);*/

/*        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));*/
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.097622, 23.734051), 12));
        }else Toast.makeText(getApplication(),"Проверь интернет соединение",Toast.LENGTH_SHORT).show();

    }
}
