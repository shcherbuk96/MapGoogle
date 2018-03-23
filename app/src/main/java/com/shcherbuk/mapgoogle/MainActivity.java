package com.shcherbuk.mapgoogle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shcherbuk.mapgoogle.pojo.Coordinates;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements Constants{
    Button start;
    Button finish;
    Button map;

    EditText ed_start_lati;
    EditText ed_start_longi;

    EditText ed_finish_lati;
    EditText ed_finish_longi;

    static final int REQUEST_LOCATION = 1;
    double start_lati;
    double start_longi;

    double finish_lati;
    double finish_longi;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start=findViewById(R.id.start);
        finish=findViewById(R.id.end);

        ed_start_lati=findViewById(R.id.start_lati);
        ed_start_longi=findViewById(R.id.start_longi);

        ed_finish_lati=findViewById(R.id.finish_lati);
        ed_finish_longi=findViewById(R.id.finish_longi);

        map=findViewById(R.id.map);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationStart();
                /*Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("lati",String.valueOf(lati));
                intent.putExtra("longi",String.valueOf(longi));
                startActivity(intent);*/
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationFinish();
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //writeDB();
                Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getLocationStart() {
        LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location!=null) {
            start_lati = location.getLatitude();
            start_longi = location.getLongitude();

            ed_start_lati.setText(String.valueOf(start_lati));
            ed_start_longi.setText(String.valueOf(start_longi));
        }

    }

    public void getLocationFinish() {
        LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location!=null) {
            finish_lati = location.getLatitude();
            finish_longi = location.getLongitude();

            ed_finish_lati.setText(String.valueOf(finish_lati));
            ed_finish_longi.setText(String.valueOf(finish_longi));
        }

    }
    private void writeDB(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Coordinates coordinates=new Coordinates(start_lati,start_longi,finish_lati,finish_longi);
        mDatabase.child("coordinates").child(String.valueOf(coordinates.hashCode())).setValue(coordinates);
    }
}
