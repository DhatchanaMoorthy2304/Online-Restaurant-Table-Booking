package com.restaurent.restaurent_table_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

public class Customer_Profile extends AppCompatActivity implements LocationListener {
    LocationManager cus_locationManager;
    TextView cus_pro_edit,cus_pro_update,cus_pro_edit_add;
    RelativeLayout cus_pro_rl1,cus_pro_rl5,cus_pro_edit_rl1,cus_pro_edit_rl2;
    EditText cus_pro_edit_name;
    String root,cus_name;
    CardView cus_pro_update_address;
    String[] main_root;
    TextView cus_pro_name, cus_pro_phone, cus_pro_mail, cus_pro_upi, cus_pro_add,cus_pro_cur_add;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, update_address;
    ProgressDialog progressDialog;
    LocationManager cl_locationManager;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        FirebaseApp.initializeApp(this);
        root = getIntent().getStringExtra("mail");
        main_root = root.split("\\.");
        progressDialog = new ProgressDialog(Customer_Profile.this);
        cus_pro_name = findViewById(R.id.cus_pro_name);
        cus_pro_phone = findViewById(R.id.cus_pro_phone);
        cus_pro_mail = findViewById(R.id.cus_pro_mail);
        cus_pro_upi = findViewById(R.id.cus_pro_upi);
        cus_pro_add = findViewById(R.id.cus_pro_add);
        cus_pro_edit=findViewById(R.id.cus_pro_edit);
        cus_pro_update=findViewById(R.id.cus_pro_update);
        cus_pro_rl1=findViewById(R.id.cus_pro_rl1);
        cus_pro_rl5=findViewById(R.id.cus_pro_rl5);
        cus_pro_edit_rl1=findViewById(R.id.cus_pro_edit_rl1);
        cus_pro_edit_rl2=findViewById(R.id.cus_pro_edit_rl2);
        cus_pro_edit_name=findViewById(R.id.cus_pro_edit_name);
        cus_pro_edit_add=findViewById(R.id.cus_pro_edit_add);
        progressDialog = new ProgressDialog(Customer_Profile.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Customer Registration Details").child(main_root[0]);
        update_address = firebaseDatabase.getReference().child("Customer Registration Details").child(main_root[0]);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cus_name=snapshot.child("_Customer_Name").getValue().toString();
                cus_pro_edit_name.setText(snapshot.child("_Customer_Name").getValue().toString());
                cus_pro_name.setText(snapshot.child("_Customer_Name").getValue().toString());
                cus_pro_phone.setText(snapshot.child("_Customer_Phone").getValue().toString());
                cus_pro_mail.setText(snapshot.child("_Customer_Mail_Id").getValue().toString());
                cus_pro_upi.setText(snapshot.child("_Customer_UPI").getValue().toString());
                cus_pro_add.setText(snapshot.child("_Customer_Address").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.cus_home_navigat);
        bottomNavigationView.setSelectedItemId(R.id.cus_menu_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cus_menu_home:
                        Intent i3 = new Intent(getApplicationContext(), Customer_Home.class);
                        i3.putExtra("mail", root);
                        startActivity(i3);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.cus_menu_booked:
                        Intent i1 = new Intent(getApplicationContext(), Customer_Booked.class);
                        i1.putExtra("mail", root);
                        startActivity(i1);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.cus_menu_payment:
                        Intent i2 = new Intent(getApplicationContext(), Customer_Payment.class);
                        i2.putExtra("mail", root);
                        startActivity(i2);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.cus_menu_time_slot:
                        Intent i4 = new Intent(getApplicationContext(), Customer_Time_Slot.class);
                        i4.putExtra("mail", root);
                        startActivity(i4);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.cus_menu_profile:
                        return true;
                }
                return false;
            }
        });

    }

    public void add_update(View view){
        update_address.child("_Customer_Address").setValue(cus_pro_cur_add.getText().toString().trim());
        AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Profile.this);
        builder.setTitle("Successfully Updated!").setMessage("Successfully Data has been Updated")
                .setPositiveButton(
                        "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cus_pro_add.setVisibility(View.VISIBLE);
                                cus_pro_cur_add.setVisibility(View.INVISIBLE);
                                cus_pro_update_address.setVisibility(View.INVISIBLE);
                            }
                        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public void get_curret_location(View view) {
        if (ContextCompat.checkSelfPermission(Customer_Profile.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Customer_Profile.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Getting Current Location Address...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        getlocation();
    }

    private void getlocation() {
        try {
            cus_locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
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
            cus_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5, (LocationListener) Customer_Profile.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cus_pro_edit(View view){
        cus_pro_rl1.setVisibility(View.INVISIBLE);
        cus_pro_rl5.setVisibility(View.INVISIBLE);
        cus_pro_edit.setVisibility(View.INVISIBLE);
        cus_pro_edit_rl1.setVisibility(View.VISIBLE);
        cus_pro_edit_rl2.setVisibility(View.VISIBLE);
        cus_pro_update.setVisibility(View.VISIBLE);
        if (ContextCompat.checkSelfPermission(Customer_Profile.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Customer_Profile.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Getting Current Location Address...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        getlocation();
    }
    public void cus_pro_update(View view){
        if(isUpiChecked()){
            databaseReference.child("_Restaurant_Address").setValue(cus_pro_edit_add.getText().toString().trim());
            AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Profile.this);
            builder.setTitle("Successfully Updated!").setMessage("Successfully Data has been Updated")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cus_pro_rl1.setVisibility(View.VISIBLE);
                                    cus_pro_rl5.setVisibility(View.VISIBLE);
                                    cus_pro_edit.setVisibility(View.VISIBLE);
                                    cus_pro_edit_rl1.setVisibility(View.INVISIBLE);
                                    cus_pro_edit_rl2.setVisibility(View.INVISIBLE);
                                    cus_pro_update.setVisibility(View.INVISIBLE);
                                }
                            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }else{
            databaseReference.child("_Restaurant_Address").setValue(cus_pro_edit_add.getText().toString().trim());
            AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Profile.this);
            builder.setTitle("Successfully Updated!").setMessage("Successfully Data has been Updated")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cus_pro_rl1.setVisibility(View.VISIBLE);
                                    cus_pro_rl5.setVisibility(View.VISIBLE);
                                    cus_pro_edit.setVisibility(View.VISIBLE);
                                    cus_pro_edit_rl1.setVisibility(View.INVISIBLE);
                                    cus_pro_edit_rl2.setVisibility(View.INVISIBLE);
                                    cus_pro_update.setVisibility(View.INVISIBLE);
                                }
                            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
    }
    private boolean isUpiChecked() {
        if(!cus_name.equals(cus_pro_edit_name.getEditableText().toString().trim())){
            databaseReference.child("_Restaurant_Name").setValue(cus_pro_edit_name.getEditableText().toString().trim());
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        progressDialog.dismiss();
        cus_pro_rl5.setVisibility(View.INVISIBLE);
        cus_pro_edit_rl2.setVisibility(View.VISIBLE);
        try{
            Geocoder geocoder = new Geocoder(Customer_Profile.this, Locale.getDefault());
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String add=addresses.get(0).getAddressLine(0);
            cus_pro_edit_add.setText(add);

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
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Profile.this);
        builder.setTitle("Really Logout?").setMessage("Are you sure?")
                .setPositiveButton(
                        "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (true) {
                                    Paper.book().destroy();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }
                )
                .setNegativeButton("Cancel", null).setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}