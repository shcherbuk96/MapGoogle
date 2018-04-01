package com.shcherbuk.mapgoogle;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

    Spinner spinner;

    double start_lati;
    double start_longi;

    double finish_lati;
    double finish_longi;

    GpsTracker gt;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isConnected(getApplication())) {
            setContentView(R.layout.error);
        }

        getPermission();
        gt = new GpsTracker(getApplicationContext());
        start = findViewById(R.id.start);
        finish = findViewById(R.id.end);

        ed_start_lati = findViewById(R.id.start_lati);
        ed_start_longi = findViewById(R.id.start_longi);

        ed_finish_lati = findViewById(R.id.finish_lati);
        ed_finish_longi = findViewById(R.id.finish_longi);

        spinner=findViewById(R.id.spinner);

        map = findViewById(R.id.map);
        save = findViewById(R.id.save);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //GpsTracker gt = new GpsTracker(getApplicationContext());
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

                Location l = gt.getLocation();
                if( l == null){
                    Toast.makeText(getApplicationContext(),"GPS unable to get Value 2",Toast.LENGTH_SHORT).show();
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
                String s1=ed_start_lati.getText().toString();
                String s2=ed_start_longi.getText().toString();
                String f1=ed_finish_lati.getText().toString();
                String f2=ed_finish_longi.getText().toString();

                if (!f1.isEmpty() && !f2.isEmpty()
                        && !s1.isEmpty() && !s2.isEmpty()) {
                    if(!s1.equals(f1) && !s2.equals(f2)){
                        writeDB();
                        Toast.makeText(getApplicationContext(),"Координаты сохранены",Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getApplication(), "Координаты start и finish равны", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplication(), "Заполни поля координатами", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void writeDB() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String selected = spinner.getSelectedItem().toString();
        Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
        Coordinates coordinates = new Coordinates(start_lati, start_longi, finish_lati, finish_longi,selected);
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

    public void getPermission(){
        if (ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission not granted / Разрешение не получено", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            //return null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ed_start_lati",ed_start_lati.getText().toString());
        outState.putString("ed_start_longi",ed_start_longi.getText().toString());

        outState.putString("ed_finish_lati",ed_finish_lati.getText().toString());
        outState.putString("ed_finish_longi",ed_finish_longi.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ed_start_lati.setText(savedInstanceState.getString("ed_start_lati"));
        ed_start_longi.setText(savedInstanceState.getString("ed_start_longi"));

        ed_finish_lati.setText(savedInstanceState.getString("ed_finish_lati"));
        ed_finish_longi.setText(savedInstanceState.getString("ed_finish_longi"));
    }
}
