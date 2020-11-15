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

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

public class Restaurant_Time_Slot extends AppCompatActivity {
    DatabaseReference rest_cus_databaseReference,customer_booked_time_database;
    FirebaseRecyclerAdapter<Reg, RTS1> hotel_firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<Book, RTS2> time_firebaseRecyclerAdapter;
    RecyclerView rest_time_slot_details_recyclerview;
    String root;
    String[] main_root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_time_slot);
        FirebaseApp.initializeApp(this);
        root=getIntent().getStringExtra("mail");
        main_root=root.split("\\.");
        rest_time_slot_details_recyclerview=findViewById(R.id.rest_time_slot_details_recyclerview);
        rest_time_slot_details_recyclerview.setHasFixedSize(true);
        rest_time_slot_details_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        customer_booked_time_database   = FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(main_root[0]);
        rest_cus_databaseReference      = FirebaseDatabase.getInstance().getReference().child("Restaurant Customer Details").child(main_root[0]);
        BottomNavigationView bottomNavigationView=findViewById(R.id.restaurant_home_navigat);
        bottomNavigationView.setSelectedItemId(R.id.rest_menu_time_slot);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.rest_menu_home:
                        Intent i3=new Intent(getApplicationContext(),Restaurant_Home.class);
                        i3.putExtra("mail",root);
                        startActivity(i3);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rest_menu_booked:
                        Intent i1=new Intent(getApplicationContext(),Restaurant_Booked.class);
                        i1.putExtra("mail",root);
                        startActivity(i1);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rest_menu_payment:
                        Intent i2=new Intent(getApplicationContext(),Restaurant_Payment.class);
                        i2.putExtra("mail",root);
                        startActivity(i2);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rest_menu_time_slot:
                        return true;
                    case R.id.rest_menu_profile:
                        Intent i4=new Intent(getApplicationContext(),Restaurant_Profile.class);
                        i4.putExtra("mail",root);
                        startActivity(i4);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        FirebaseRecyclerOptions<Reg> options1 = new FirebaseRecyclerOptions.Builder<Reg>().setQuery(rest_cus_databaseReference, Reg.class).build();
        hotel_firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Reg, RTS1>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull RTS1 holder1, int position, @NonNull Reg model) {
                holder1.cusname.setText(model.get_Customer_Name());
                FirebaseRecyclerOptions<Book> options2 = new FirebaseRecyclerOptions.Builder<Book>().setQuery(customer_booked_time_database.child(model.get_Customer_Mail_Id()), Book.class).build();
                time_firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Book, RTS2>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull RTS2 holder2, int position, @NonNull Book model) {
                        holder2.bokeed_time.setText(model.get_Booked_Time());
                        holder2.booked_date.setText(model.get_Booked_Date());
                        holder2.booked_status.setText(model.get_Status());
                        holder2.booked_seats.setText(model.get_Total_Seats());
                    }

                    @NonNull
                    @Override
                    public RTS2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_cus_time_slot_inside_card, parent, false);
                        return new RTS2(view2);
                    }
                };
                time_firebaseRecyclerAdapter.startListening();
                time_firebaseRecyclerAdapter.notifyDataSetChanged();
                holder1.time_recyclerView.setAdapter(time_firebaseRecyclerAdapter);
            }

            @NonNull
            @Override
            public RTS1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_cus_time_slot_card, parent, false);
                return new RTS1(view1);
            }
        };
        hotel_firebaseRecyclerAdapter.startListening();
        hotel_firebaseRecyclerAdapter.notifyDataSetChanged();
        rest_time_slot_details_recyclerview.setAdapter(hotel_firebaseRecyclerAdapter);
    }
    public static class RTS1 extends RecyclerView.ViewHolder {
        public TextView cusname;
        public RecyclerView time_recyclerView;
        public RecyclerView.LayoutManager manager;
        public RTS1(@NonNull View itemView) {
            super(itemView);
            time_recyclerView=itemView.findViewById(R.id.rest_cus_time_slot_recyclerview);
            cusname=itemView.findViewById(R.id.rest_cus_time_slot_cus_name);
            manager=new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
            time_recyclerView.setLayoutManager(manager);
        }
    }
    public static class RTS2 extends RecyclerView.ViewHolder {
        public TextView bokeed_time,booked_date,booked_status,booked_seats;
        public RTS2(@NonNull View itemView) {
            super(itemView);
            booked_date     =itemView.findViewById(R.id.rest_cus_time_slot_inside_date);
            bokeed_time     =itemView.findViewById(R.id.rest_cus_time_slot_inside_time);
            booked_status   =itemView.findViewById(R.id.rest_cus_time_slot_inside_status);
            booked_seats    =itemView.findViewById(R.id.rest_cus_time_slot_inside_seats);
        }
    }
    public void rest_time_slot_back_press(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant_Time_Slot.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant_Time_Slot.this);
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