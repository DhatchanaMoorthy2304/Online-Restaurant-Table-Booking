package com.restaurent.restaurent_table_booking;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.AccountsException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.datatransport.runtime.backends.BackendResponse;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.FirebaseApp;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class Current_Location extends AppCompatActivity implements LocationListener {

    Button cl_button1,cl_button2;
    TextView cl_Value,cl_Address;
    LocationManager cl_locationManager;
    ProgressBar cl_progressBar;
    EditText editText1,editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);
        FirebaseApp.initializeApp(this);
        cl_button1=findViewById(R.id.cl_button1);
        cl_button2=findViewById(R.id.cl_button2);
        cl_Value=findViewById(R.id.cl_Value);
        cl_Address=findViewById(R.id.cl_Address);
        cl_progressBar=findViewById(R.id.cl_progressBar);

        editText1=findViewById(R.id.cl_add1);
        editText2=findViewById(R.id.cl_add2);
/*
        //Initialize places
        Places.initialize(getApplicationContext(),"AIzaSyCYd9DNtP8fAnic_H5XwgCef7dmqj_7vB0");//AIzaSyBPMpiw78SetJi-B3U2uzCPTuihj2GmrBU
        //Set EditText non Focusable
        editText1.setFocusable(false);
        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initial Place field list
                List<Place.Field> fieldList1= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                //Create Intent
                Intent intent1=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList1).build(Current_Location.this);
                //Start Activity
                startActivityForResult(intent1,100);

            }
        });
        editText2.setFocusable(false);
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initial Place field list
                List<Place.Field> fieldList2= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                //Create Intent
                Intent intent2=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList2).build(Current_Location.this);
                //Start Activity
                startActivityForResult(intent2,150);

            }
        });

*/
        if(ContextCompat.checkSelfPermission(Current_Location.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Current_Location.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },100);
        }
        cl_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cl_progressBar.setVisibility(View.VISIBLE);
                getlocation();

            }
        });
        cl_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first=editText1.getText().toString().trim();
                String second=editText2.getText().toString().trim();
                if(first.equals("")&&second.equals("")){
                    Toasty.error(getApplicationContext(),"Enter both Location",Toasty.LENGTH_SHORT).show();
                }
                else {
                    DisplayTrack(first,second);
                }
            }
        });
    }

    private void DisplayTrack(String first, String second) {
        try{
            //When google map is installed.
            //Initialize Uri
            Uri uri=Uri.parse("https://www.google.co.in/maps/dir/"+first+"/"+second);
            //Initialize intent with action view
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            //Set package
            intent.setPackage("com.google.android.apps.maps");
            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Start Activity
            startActivity(intent);

        }catch (ActivityNotFoundException e){
            //When google map is not installed
            //Initialize Uri
            Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            //Initialize intent with action view
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            //Set flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Start Activity
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent data1,data2;
        data1=data;
        data2=data;
        if(requestCode==100&&resultCode==RESULT_OK){
            Place place1=Autocomplete.getPlaceFromIntent(data1);
            editText1.setText(place1.getAddress());
        }
        else if(requestCode==150&&resultCode==RESULT_OK){
            Place place2=Autocomplete.getPlaceFromIntent(data2);
            editText2.setText(place2.getAddress());
        }
        else if(requestCode== AutocompleteActivity.RESULT_ERROR){
            Status status1=Autocomplete.getStatusFromIntent(data1);
            Status status2=Autocomplete.getStatusFromIntent(data2);
            Toasty.error(getApplicationContext(),status1.getStatusMessage(),Toasty.LENGTH_SHORT).show();
            Toasty.error(getApplicationContext(),status2.getStatusMessage(),Toasty.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("MissingPermission")
    private void getlocation() {
        try{
            cl_locationManager=(LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            cl_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,Current_Location.this);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onLocationChanged(Location location) {
            cl_progressBar.setVisibility(View.GONE);
            cl_Value.setVisibility(View.VISIBLE);
            cl_Address.setVisibility(View.VISIBLE);
            String loc=location.getLatitude()+" "+location.getLongitude();
            cl_Value.setText(loc);
            try{
                Geocoder geocoder = new Geocoder(Current_Location.this, Locale.getDefault());
                List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                String add=addresses.get(0).getAddressLine(0);
                cl_Address.setText(add);
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        int PLACE_PICKER_REQUEST = 1;


    }
}
