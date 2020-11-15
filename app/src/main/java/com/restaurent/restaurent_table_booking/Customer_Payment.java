package com.restaurent.restaurent_table_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CpuUsageInfo;
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

public class Customer_Payment extends AppCompatActivity {
    RecyclerView cus_payment_details_recyclerview;
    DatabaseReference customer_booked_time_database,cus_rest_databaseReference;
    FirebaseRecyclerAdapter<Book, CP1> hotel_firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<Book, CP2> payment_firebaseRecyclerAdapter;
    String root;
    String[] main_root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);
        FirebaseApp.initializeApp(this);
        cus_payment_details_recyclerview=findViewById(R.id.cus_payment_details_recyclerview);
        cus_payment_details_recyclerview.setHasFixedSize(true);
        cus_payment_details_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        root=getIntent().getStringExtra("mail");
        main_root=root.split("\\.");

        customer_booked_time_database   = FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(main_root[0]);
        cus_rest_databaseReference      = FirebaseDatabase.getInstance().getReference().child("Customer Restaurant Details").child(main_root[0]);
        BottomNavigationView bottomNavigationView=findViewById(R.id.cus_home_navigat);
        bottomNavigationView.setSelectedItemId(R.id.cus_menu_payment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.cus_menu_home:
                        Intent i1=new Intent(getApplicationContext(),Customer_Home.class);
                        i1.putExtra("mail",root);
                        startActivity(i1);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cus_menu_booked:
                        Intent i2=new Intent(getApplicationContext(),Customer_Booked.class);
                        i2.putExtra("mail",root);
                        startActivity(i2);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.cus_menu_payment:
                        return true;
                    case R.id.cus_menu_time_slot:

                        Intent i3=new Intent(getApplicationContext(),Customer_Time_Slot.class);
                        i3.putExtra("mail",root);
                        startActivity(i3);
                        finish();
                        overridePendingTransition(0,0);
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
        hotel_firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Book, CP1>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull final CP1 holder1, int position, @NonNull Book model) {
                holder1.shopname.setText(model.get_Restaurant_Name());
                FirebaseRecyclerOptions<Book> options2 = new FirebaseRecyclerOptions.Builder<Book>().setQuery(customer_booked_time_database.child(model.get_Restaurant_Mail_Id()), Book.class).build();
                payment_firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, CP2>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull CP2 holder2, int position, @NonNull Book model) {
                        //amount,status,charged,refunded,request_date,request_time,return_date,return_time
                        if(model.get_Amount_Status().equals("Paid")){
                            holder2.status.setTextColor(getResources().getColor(R.color.quantum_vanillagreen800));
                            holder2.amount.setText(model.get_Amount_Paid());
                            holder2.status.setText(model.get_Amount_Status());
                            holder2.charged.setText(model.get_Charged());
                            holder2.refunded.setText(model.get_Refunded());
                            holder2.request_date.setText(model.get_Request_Date());
                            holder2.request_time.setText(model.get_Request_Time());
                            holder2.return_date.setText(model.get_Return_Date());
                            holder2.return_time.setText(model.get_Return_Time());
                        }
                        else {
                            holder2.amount.setText(model.get_Amount_Paid());
                            holder2.status.setText(model.get_Amount_Status());
                            holder2.charged.setText(model.get_Charged());
                            holder2.refunded.setText(model.get_Refunded());
                            holder2.request_date.setText(model.get_Request_Date());
                            holder2.request_time.setText(model.get_Request_Time());
                            holder2.return_date.setText(model.get_Return_Date());
                            holder2.return_time.setText(model.get_Return_Time());
                        }
                    }

                    @NonNull
                    @Override
                    public CP2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_payment_status_inside_card, parent, false);
                        return new CP2(view2);
                    }
                };
                payment_firebaseRecyclerAdapter.startListening();
                payment_firebaseRecyclerAdapter.notifyDataSetChanged();
                holder1.payment_recyclerView.setAdapter(payment_firebaseRecyclerAdapter);
            }

            @NonNull
            @Override
            public CP1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_payment_status_card, parent, false);
                return new CP1(view2);
            }
        };
        hotel_firebaseRecyclerAdapter.startListening();
        hotel_firebaseRecyclerAdapter.notifyDataSetChanged();
        cus_payment_details_recyclerview.setAdapter(hotel_firebaseRecyclerAdapter);
    }
    public static class CP1 extends RecyclerView.ViewHolder {
        public TextView shopname;
        public RecyclerView payment_recyclerView;
        public RecyclerView.LayoutManager manager;
        public CP1(@NonNull View itemView) {
            super(itemView);
            payment_recyclerView=itemView.findViewById(R.id.cus_payment_status_card_recyclerview);
            shopname=itemView.findViewById(R.id.cus_payment_status_card_hotel_name);
            manager=new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
            payment_recyclerView.setLayoutManager(manager);
        }
    }
    public static class CP2 extends RecyclerView.ViewHolder {
        public TextView amount,status,charged,refunded,request_date,request_time,return_date,return_time;
        public CP2(@NonNull View itemView) {
            super(itemView);
            amount      =itemView.findViewById(R.id.cus_payment_status_inside_card_amount);
            status      =itemView.findViewById(R.id.cus_payment_status_inside_card_status);
            charged     =itemView.findViewById(R.id.cus_payment_status_inside_card_charged);
            refunded    =itemView.findViewById(R.id.cus_payment_status_inside_card_refunded);
            request_date=itemView.findViewById(R.id.cus_payment_status_inside_card_request_date);
            request_time=itemView.findViewById(R.id.cus_payment_status_inside_card_request_time);
            return_date =itemView.findViewById(R.id.cus_payment_status_inside_card_return_date);
            return_time =itemView.findViewById(R.id.cus_payment_status_inside_card_return_time);
        }
    }
    public void cus_payment_back_press(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Payment.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Payment.this);
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