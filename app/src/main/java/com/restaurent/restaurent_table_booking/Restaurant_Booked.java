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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

public class Restaurant_Booked extends AppCompatActivity {
    CardView rest_cus_book_card_one,rest_cus_book_card_two,rest_cus_book_card_three;
    RecyclerView res_cus_table_booked_recyclerview,rest_cus_food_booked_recyclerview;
    DatabaseReference customer_booked_time_database,rest_cus_databaseReference,customer_booked_food_database,databaseReference,update_seats,update1,update2;
    FirebaseRecyclerAdapter<Reg, RB1> hotel_firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<Book, RB2> time_firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<Reg, RB3> food_firebaseRecyclerAdapter;
    BottomNavigationView bottomNavigationView;
    String root,cus_mail,book_root,date,time,current_date,current_time,booked_seats,available_seats;
    String[] main_root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_booked);
        FirebaseApp.initializeApp(this);
        root=getIntent().getStringExtra("mail");
        main_root=root.split("\\.");
        rest_cus_book_card_one=findViewById(R.id.rest_cus_book_card_one);
        rest_cus_book_card_two=findViewById(R.id.rest_cus_book_card_two);
        rest_cus_book_card_three=findViewById(R.id.rest_cus_book_card_three);
        bottomNavigationView=findViewById(R.id.restaurant_home_navigat);
        res_cus_table_booked_recyclerview=findViewById(R.id.res_cus_table_booked_recyclerview);
        res_cus_table_booked_recyclerview.setHasFixedSize(true);
        res_cus_table_booked_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        rest_cus_food_booked_recyclerview=findViewById(R.id.rest_cus_food_booked_recyclerview);
        rest_cus_food_booked_recyclerview.setHasFixedSize(true);
        rest_cus_food_booked_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        databaseReference   = FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details").child(main_root[0]);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                available_seats=snapshot.child("_Restaurant_Available_Seats").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        customer_booked_time_database   = FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(main_root[0]);
        rest_cus_databaseReference      = FirebaseDatabase.getInstance().getReference().child("Restaurant Customer Details").child(main_root[0]);
        bottomNavigationView.setSelectedItemId(R.id.rest_menu_booked);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.rest_menu_home:
                        Intent i1=new Intent(getApplicationContext(),Restaurant_Home.class);
                        i1.putExtra("mail",root);
                        startActivity(i1);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rest_menu_booked:
                        return true;
                    case R.id.rest_menu_payment:
                        Intent i2=new Intent(getApplicationContext(),Restaurant_Payment.class);
                        i2.putExtra("mail",root);
                        startActivity(i2);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rest_menu_time_slot:

                        Intent i3=new Intent(getApplicationContext(),Restaurant_Time_Slot.class);
                        i3.putExtra("mail",root);
                        startActivity(i3);
                        finish();
                        overridePendingTransition(0,0);
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
        hotel_firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Reg, RB1>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull RB1 holder1, int position, @NonNull Reg model) {
                holder1.cusname.setText(model.get_Customer_Name());
                cus_mail=model.get_Customer_Mail_Id();
                FirebaseRecyclerOptions<Book> options2 = new FirebaseRecyclerOptions.Builder<Book>().setQuery(customer_booked_time_database.child(model.get_Customer_Mail_Id()), Book.class).build();
                time_firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Book, RB2>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull RB2 holder2, int position, @NonNull final Book model) {
                        holder2.bokeed_time.setText(model.get_Booked_Time());
                        holder2.booked_date.setText(model.get_Booked_Date());
                        holder2.booked_status.setText(model.get_Status());
                        holder2.booked_seats.setText(model.get_Total_Seats());
                        holder2.booked_card_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                book_root=model.get_Current_Date_at_the_Time_of_Booking_Food()+"+"+model.get_Current_Time_at_the_Time_of_Booking_Food()+"+"+model.get_Booked_Date()+"+"+model.get_Booked_Time();
                                date=model.get_Booked_Date();
                                time=model.get_Booked_Time();
                                booked_seats=model.get_Total_Seats();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy kk:mm:ss");
                                String dateString1 = date + " " + time;
                                java.util.Calendar calendar_final1 = java.util.Calendar.getInstance();
                                if(model.get_Status().equals("Finished")||model.get_Status().equals("Cancelled")){
                                    rest_cus_book_card_three.setVisibility(View.INVISIBLE);
                                    rest_cus_book_card_one.setVisibility(View.INVISIBLE);
                                    bottomNavigationView.setVisibility(View.INVISIBLE);
                                    rest_cus_book_card_two.setVisibility(View.VISIBLE);
                                    show();
                                }
                                else if(model.get_Status().equals("Not Finished")){
                                    rest_cus_book_card_three.setVisibility(View.VISIBLE);
                                    rest_cus_book_card_one.setVisibility(View.INVISIBLE);
                                    bottomNavigationView.setVisibility(View.INVISIBLE);
                                    rest_cus_book_card_two.setVisibility(View.VISIBLE);
                                    show();
                                    try {
                                        Date date1 = sdf.parse(dateString1);
                                        calendar_final1.setTime(date1);
                                        if(calendar_final1.getTimeInMillis()<java.util.Calendar.getInstance().getTimeInMillis()){
                                            rest_cus_book_card_three.setVisibility(View.VISIBLE);
                                            rest_cus_book_card_one.setVisibility(View.INVISIBLE);
                                            bottomNavigationView.setVisibility(View.INVISIBLE);
                                            rest_cus_book_card_two.setVisibility(View.VISIBLE);
                                            show();
                                        }
                                        else {
                                            rest_cus_book_card_three.setVisibility(View.INVISIBLE);
                                            rest_cus_book_card_one.setVisibility(View.INVISIBLE);
                                            bottomNavigationView.setVisibility(View.INVISIBLE);
                                            rest_cus_book_card_two.setVisibility(View.VISIBLE);
                                            show();
                                        }
                                    } catch (ParseException e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant_Booked.this);
                                        builder.setTitle("You can't cancel a Table at time!").setMessage(e.getMessage())
                                                .setNegativeButton("Ok", null).setCancelable(false);
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    }
                                }
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public RB2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_booked_cus_time_inside_card, parent, false);
                        return new RB2(view2);
                    }
                };
                time_firebaseRecyclerAdapter.startListening();
                time_firebaseRecyclerAdapter.notifyDataSetChanged();
                holder1.time_recyclerView.setAdapter(time_firebaseRecyclerAdapter);
            }
            @NonNull
            @Override
            public RB1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_booked_cus_time_card, parent, false);
                return new RB1(view1);
            }
        };
        hotel_firebaseRecyclerAdapter.startListening();
        hotel_firebaseRecyclerAdapter.notifyDataSetChanged();
        res_cus_table_booked_recyclerview.setAdapter(hotel_firebaseRecyclerAdapter);
    }
    private void show(){
        customer_booked_food_database=FirebaseDatabase.getInstance().getReference().child("Customer Booked Details").child(main_root[0]).child(cus_mail).child(book_root);
        FirebaseRecyclerOptions<Reg> options3 = new FirebaseRecyclerOptions.Builder<Reg>().setQuery(customer_booked_food_database, Reg.class).build();
        food_firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Reg, RB3>(options3) {
            @Override
            protected void onBindViewHolder(@NonNull RB3 holder, int position, @NonNull Reg model) {
                holder.order_food_id.setText(model.get_Food_Id());
                holder.order_food_name.setText(model.get_Food_Name());
                holder.order_food_price.setText(model.get_Food_Price());
                holder.order_food_qty.setText(model.get_Food_Quantity());
                holder.order_food_cat.setText(model.get_Food_Category());
                Picasso.get().load(model.get_Food_Image_Url()).into(holder.order_food_image);
            }
            @NonNull
            @Override
            public RB3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_booked_cus_food_card, parent, false);
                return new RB3(view3);
            }
        };
        food_firebaseRecyclerAdapter.startListening();
        food_firebaseRecyclerAdapter.notifyDataSetChanged();
        rest_cus_food_booked_recyclerview.setAdapter(food_firebaseRecyclerAdapter);
    }
    public static class RB1 extends RecyclerView.ViewHolder {
        public TextView cusname;
        public RecyclerView time_recyclerView;
        public RecyclerView.LayoutManager manager;
        public RB1(@NonNull View itemView) {
            super(itemView);
            time_recyclerView   =itemView.findViewById(R.id.rest_booked_cus_recyclerview);
            cusname             =itemView.findViewById(R.id.rest_booked_cus_name);
            manager=new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
            time_recyclerView.setLayoutManager(manager);
        }
    }
    public static class RB2 extends RecyclerView.ViewHolder {
        public TextView bokeed_time,booked_date,booked_status,booked_seats;
        public CardView booked_card_view;
        public RB2(@NonNull View itemView) {
            super(itemView);
            bokeed_time     =itemView.findViewById(R.id.res_booked_cus_time_inside_time);
            booked_date     =itemView.findViewById(R.id.res_booked_cus_time_inside_date);
            booked_status   =itemView.findViewById(R.id.res_booked_cus_time_inside_status);
            booked_seats    =itemView.findViewById(R.id.res_booked_cus_time_inside_seats);
            booked_card_view=itemView.findViewById(R.id.res_booked_cus_time_inside_card_view);
        }
    }
    public static class RB3 extends RecyclerView.ViewHolder {

        public TextView order_food_id, order_food_name, order_food_price, order_food_qty, order_food_cat;
        public ImageView order_food_image;
        public RB3(@NonNull View itemView) {
            super(itemView);
            order_food_id       = itemView.findViewById(R.id.res_booked_cus_food_id);
            order_food_name     = itemView.findViewById(R.id.res_booked_cus_food_name);
            order_food_price    = itemView.findViewById(R.id.res_booked_cus_food_price);
            order_food_qty      = itemView.findViewById(R.id.res_booked_cus_food_qty);
            order_food_cat      = itemView.findViewById(R.id.res_booked_cus_food_category);
            order_food_image    = itemView.findViewById(R.id.res_booked_cus_food_image);
        }
    }
    public void rest_booked_back_press(View view){
        if(rest_cus_book_card_two.isShown()){
            AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant_Booked.this);
            builder.setTitle("Are you really going back?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    rest_cus_book_card_one.setVisibility(View.VISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                    rest_cus_book_card_two.setVisibility(View.INVISIBLE);
                                    rest_cus_book_card_three.setVisibility(View.INVISIBLE);
                                }
                            }
                    )
                    .setNegativeButton("Cancel", null).setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant_Booked.this);
            builder.setTitle("Really Logout?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (true) {
                                        Paper.book().destroy();
                                        Intent intent = new Intent(Restaurant_Booked.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(Restaurant_Booked.this, MainActivity.class);
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

    @Override
    public void onBackPressed() {
        if(rest_cus_book_card_two.isShown()){
            AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant_Booked.this);
            builder.setTitle("Are you really going back?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    rest_cus_book_card_one.setVisibility(View.VISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                    rest_cus_book_card_two.setVisibility(View.INVISIBLE);
                                    rest_cus_book_card_three.setVisibility(View.INVISIBLE);
                                }
                            }
                    )
                    .setNegativeButton("Cancel", null).setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant_Booked.this);
            builder.setTitle("Really Logout?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (true) {
                                        Paper.book().destroy();
                                        Intent intent = new Intent(Restaurant_Booked.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(Restaurant_Booked.this, MainActivity.class);
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

    public void rest_booked_cancel(View view){
        current_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        current_time = new SimpleDateFormat("kk:mm:ss", Locale.getDefault()).format(new Date());
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy kk:mm:ss");
            String dateInString=date+" "+time;
            try {
                Date  date = sdf.parse(dateInString);
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTime(date);
                if((calendar.getTimeInMillis())<(java.util.Calendar.getInstance().getTimeInMillis() + (60 * 60 * 1000))){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant_Booked.this);
                    builder.setTitle("Are you really finishing this table?").setMessage("Are you sure?")
                            .setPositiveButton(
                                    "Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int c=Integer.parseInt(available_seats);
                                            int d=Integer.parseInt(booked_seats);
                                            int a=c+d;
                                            update_seats = FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details").child(main_root[0]);
                                            update1=FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(main_root[0]).child(cus_mail).child(book_root);
                                            update2=FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(cus_mail).child(main_root[0]).child(book_root);
                                            update_seats.child("_Restaurant_Available_Seats").setValue(String.valueOf(a));
                                            update1.child("_Status").setValue("Finished");
                                            update2.child("_Status").setValue("Finished");
                                            rest_cus_book_card_one.setVisibility(View.VISIBLE);
                                            bottomNavigationView.setVisibility(View.VISIBLE);
                                            rest_cus_book_card_two.setVisibility(View.INVISIBLE);
                                            rest_cus_book_card_three.setVisibility(View.INVISIBLE);
                                        }
                                    }
                            )
                            .setNegativeButton("Cancel", null).setCancelable(false);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant_Booked.this);
                    builder.setTitle("You can't cancel a Table at time!")
                            .setNegativeButton("Ok", null).setCancelable(false);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
            catch (ParseException e){
                Toasty.error(getApplicationContext(), e.getMessage(), Toasty.LENGTH_SHORT).show();
            }

    }
}