package com.restaurent.restaurent_table_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class Customer_Time_Slot extends AppCompatActivity {
    RecyclerView cus_time_slot_details_recyclerview;
    DatabaseReference cus_rest_databaseReference,customer_booked_time_database;
    FirebaseRecyclerAdapter<Book, CTS1> hotel_firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<Book, CTS2> time_firebaseRecyclerAdapter;
    String root;
    String[] main_root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_time_slot);
        FirebaseApp.initializeApp(this);
        root=getIntent().getStringExtra("mail");
        main_root=root.split("\\.");
        cus_time_slot_details_recyclerview=findViewById(R.id.cus_time_slot_details_recyclerview);
        cus_time_slot_details_recyclerview.setHasFixedSize(true);
        cus_time_slot_details_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        customer_booked_time_database   = FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(main_root[0]);
        cus_rest_databaseReference      = FirebaseDatabase.getInstance().getReference().child("Customer Restaurant Details").child(main_root[0]);
        BottomNavigationView bottomNavigationView=findViewById(R.id.cus_home_navigat);
        bottomNavigationView.setSelectedItemId(R.id.cus_menu_time_slot);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cus_menu_home:
                        Intent i3=new Intent(getApplicationContext(),Customer_Home.class);
                        i3.putExtra("mail",root);
                        startActivity(i3);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cus_menu_booked:
                        Intent i1=new Intent(getApplicationContext(),Customer_Booked.class);
                        i1.putExtra("mail",root);
                        startActivity(i1);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cus_menu_payment:
                        Intent i2=new Intent(getApplicationContext(),Customer_Payment.class);
                        i2.putExtra("mail",root);
                        startActivity(i2);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cus_menu_time_slot:
                        return true;
                    case R.id.cus_menu_profile:
                        Intent i4=new Intent(getApplicationContext(),Customer_Profile.class);
                        i4.putExtra("mail",root);
                        startActivity(i4);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        FirebaseRecyclerOptions<Book> options1 = new FirebaseRecyclerOptions.Builder<Book>().setQuery(cus_rest_databaseReference, Book.class).build();
        hotel_firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Book, CTS1>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull CTS1 holder1, int position, @NonNull Book model) {
                holder1.shopname.setText(model.get_Restaurant_Name());
                FirebaseRecyclerOptions<Book> options2 = new FirebaseRecyclerOptions.Builder<Book>().setQuery(customer_booked_time_database.child(model.get_Restaurant_Mail_Id()), Book.class).build();
                time_firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Book, CTS2>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull CTS2 holder2, int position, @NonNull Book model) {
                        holder2.bokeed_time.setText(model.get_Booked_Time());
                        holder2.booked_date.setText(model.get_Booked_Date());
                        holder2.booked_status.setText(model.get_Status());
                        holder2.booked_seats.setText(model.get_Total_Seats());
                    }

                    @NonNull
                    @Override
                    public CTS2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_time_slot_inside_card, parent, false);
                        return new CTS2(view2);
                    }
                };
                time_firebaseRecyclerAdapter.startListening();
                time_firebaseRecyclerAdapter.notifyDataSetChanged();
                holder1.time_recyclerView.setAdapter(time_firebaseRecyclerAdapter);
            }

            @NonNull
            @Override
            public CTS1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_time_slot_card, parent, false);
                return new CTS1(view1);
            }
        };
        hotel_firebaseRecyclerAdapter.startListening();
        hotel_firebaseRecyclerAdapter.notifyDataSetChanged();
        cus_time_slot_details_recyclerview.setAdapter(hotel_firebaseRecyclerAdapter);
    }
    public static class CTS1 extends RecyclerView.ViewHolder {
        public TextView shopname;
        public RecyclerView time_recyclerView;
        public RecyclerView.LayoutManager manager;
        public CTS1(@NonNull View itemView) {
            super(itemView);
            time_recyclerView=itemView.findViewById(R.id.cus_table_time_slot_recyclerview);
            shopname=itemView.findViewById(R.id.cus_time_slot_hotel_name);
            manager=new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
            time_recyclerView.setLayoutManager(manager);
        }
    }
    public static class CTS2 extends RecyclerView.ViewHolder {
        public TextView bokeed_time,booked_date,booked_status,booked_seats;
        public CTS2(@NonNull View itemView) {
            super(itemView);
            booked_date=itemView.findViewById(R.id.cus_rest_time_inside_date);
            bokeed_time=itemView.findViewById(R.id.cus_rest_time_inside_time);
            booked_status=itemView.findViewById(R.id.cus_rest_time_inside_status);
            booked_seats=itemView.findViewById(R.id.cus_rest_time_inside_seats);
        }
    }
    public void cus_time_slot_back_press(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Time_Slot.this);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Time_Slot.this);
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