package com.shcherbuk.mapgoogle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shcherbuk.mapgoogle.gps.GpsTracker;
import com.shcherbuk.mapgoogle.pojo.Coordinates;

public class MainActivity extends AppCompatActivity implements Constants {
    Button start;
    Button finish;
    Button map;
    Button save;

    EditText ed_start_lati;
    EditText ed_start_longi;

    EditText ed_finish_lati;
    EditText ed_finish_longi;

    double start_lati;
    double start_longi;

    double finish_lati;
    double finish_longi;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isConnected(getApplication())) {
            setContentView(R.layout.error);
        }
        start = findViewById(R.id.start);
        finish = findViewById(R.id.end);

        ed_start_lati = findViewById(R.id.start_lati);
        ed_start_longi = findViewById(R.id.start_longi);

        ed_finish_lati = findViewById(R.id.finish_lati);
        ed_finish_longi = findViewById(R.id.finish_longi);

        map = findViewById(R.id.map);
        save = findViewById(R.id.save);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GpsTracker gt = new GpsTracker(getApplicationContext());
                Location l = gt.getLocation();
                if( l == null){
                    Toast.makeText(getApplicationContext(),"GPS unable to get Value",Toast.LENGTH_SHORT).show();
                }else {
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    start_lati=lat;
                    start_longi=lon;
                    ed_start_lati.setText(String.valueOf(lat));
                    ed_start_longi.setText(String.valueOf(lon));
                    //Toast.makeText(getApplicationContext(),"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_SHORT).show();
                }
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GpsTracker gt = new GpsTracker(getApplicationContext());
                Location l = gt.getLocation();
                if( l == null){
                    Toast.makeText(getApplicationContext(),"GPS unable to get Value",Toast.LENGTH_SHORT).show();
                }else {
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();
                    finish_lati=lat;
                    finish_longi=lon;
                    ed_finish_lati.setText(String.valueOf(lat));
                    ed_finish_longi.setText(String.valueOf(lon));
                    //Toast.makeText(getApplicationContext(),"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_SHORT).show();
                }
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_finish_lati.getText().toString().isEmpty() && !ed_finish_longi.getText().toString().isEmpty()
                        && !ed_start_lati.getText().toString().isEmpty() && !ed_start_longi.getText().toString().isEmpty()) {
                    writeDB();
                } else {
                    Toast.makeText(getApplication(), "Заполни поля координатами", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void writeDB() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Coordinates coordinates = new Coordinates(start_lati, start_longi, finish_lati, finish_longi);
        mDatabase.child("coordinates").child(String.valueOf(coordinates.hashCode())).setValue(coordinates);
    }

    public static boolean isConnected(Context context){

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connection = manager.getActiveNetworkInfo();
        if (connection != null && connection.isConnectedOrConnecting()){
            return true;
        }
        return false;
    }
}
