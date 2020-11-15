package com.restaurent.restaurent_table_booking;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

public class Customer_Booked extends AppCompatActivity {
    RecyclerView cus_table_booked_recyclerview,cus_food_booked_recyclerview;
    CardView cus_book_card_one,cus_book_card_two,cus_book_card_three;
    String root,food_mail,book_root,date,time,current_date,current_time,available_seats,booked_seats;
    String amount_paid,amount_status,charge_status,status,charged_amount,refunded_amount;
    String[] main_root;
    TextView mail;
    DatabaseReference cus_rest_databaseReference,customer_booked_time_database,customer_booked_food_database,databaseReference,update_seats,update1,update2;
    FirebaseRecyclerAdapter<Book, CB1> hotel_firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<Book, CB2> time_firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<Reg, CB3> food_firebaseRecyclerAdapter;
    ArrayList<String> cus_rest_root;
    BottomNavigationView bottomNavigationView;
    int k=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_booked);
        FirebaseApp.initializeApp(this);
        root=getIntent().getStringExtra("mail");
        main_root=root.split("\\.");
        cus_rest_root=new ArrayList<>();

        cus_book_card_one       =findViewById(R.id.cus_book_card_one);
        cus_book_card_two       =findViewById(R.id.cus_book_card_two);
        cus_book_card_three     =findViewById(R.id.cus_book_card_three);
        cus_table_booked_recyclerview=findViewById(R.id.cus_table_booked_recyclerview);
        cus_table_booked_recyclerview.setHasFixedSize(true);
        cus_table_booked_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        cus_food_booked_recyclerview=findViewById(R.id.cus_food_booked_recyclerview);
        cus_food_booked_recyclerview.setHasFixedSize(true);
        cus_food_booked_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        customer_booked_time_database   = FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(main_root[0]);
        cus_rest_databaseReference      = FirebaseDatabase.getInstance().getReference().child("Customer Restaurant Details").child(main_root[0]);
        bottomNavigationView=findViewById(R.id.cus_home_navigat);
        bottomNavigationView.setSelectedItemId(R.id.cus_menu_booked);
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
                        return true;
                    case R.id.cus_menu_payment:
                        Intent i2=new Intent(getApplicationContext(),Customer_Payment.class);
                        i2.putExtra("mail",root);
                        startActivity(i2);
                        finish();
                        overridePendingTransition(0,0);
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
        hotel_firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Book, CB1>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull CB1 holder1, int position, @NonNull Book model) {
                holder1.shopname.setText(model.get_Restaurant_Name());
                food_mail=model.get_Restaurant_Mail_Id();
                FirebaseRecyclerOptions<Book> options2 = new FirebaseRecyclerOptions.Builder<Book>().setQuery(customer_booked_time_database.child(model.get_Restaurant_Mail_Id()), Book.class).build();
                time_firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Book, CB2>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull CB2 holder2, int position, @NonNull final Book model) {
                        holder2.bokeed_time.setText(model.get_Booked_Time());
                        holder2.booke_date.setText(model.get_Booked_Date());
                        holder2.booked_status.setText(model.get_Status());
                        holder2.booked_seats.setText(model.get_Total_Seats());
                        holder2.booked_card_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                    book_root=model.get_Current_Date_at_the_Time_of_Booking_Food()+"+"+model.get_Current_Time_at_the_Time_of_Booking_Food()+"+"+model.get_Booked_Date()+"+"+model.get_Booked_Time();
                                    date=model.get_Booked_Date();
                                    time=model.get_Booked_Time();
                                    booked_seats=model.get_Total_Seats();
                                    amount_paid=model.get_Amount_Paid();
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy kk:mm:ss");
                                    String dateString1 = date + " " + time;
                                    java.util.Calendar calendar_final1 = java.util.Calendar.getInstance();
                                    if(model.get_Status().equals("Finished")||model.get_Status().equals("Cancelled")){
                                        cus_book_card_three.setVisibility(View.INVISIBLE);
                                        cus_book_card_one.setVisibility(View.INVISIBLE);
                                        bottomNavigationView.setVisibility(View.INVISIBLE);
                                        cus_book_card_two.setVisibility(View.VISIBLE);
                                        show();
                                    }
                                    else if(model.get_Status().equals("Not Finished")){
                                        try {
                                            Date date1 = sdf.parse(dateString1);
                                            calendar_final1.setTime(date1);
                                            if(calendar_final1.getTimeInMillis()>java.util.Calendar.getInstance().getTimeInMillis()){
                                                cus_book_card_one.setVisibility(View.INVISIBLE);
                                                bottomNavigationView.setVisibility(View.INVISIBLE);
                                                cus_book_card_two.setVisibility(View.VISIBLE);
                                                cus_book_card_three.setVisibility(View.VISIBLE);
                                                show();
                                            }
                                            else {
                                                cus_book_card_one.setVisibility(View.INVISIBLE);
                                                bottomNavigationView.setVisibility(View.INVISIBLE);
                                                cus_book_card_two.setVisibility(View.VISIBLE);
                                                cus_book_card_three.setVisibility(View.INVISIBLE);
                                                show();
                                            }
                                        } catch (ParseException e) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Booked.this);
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
                    public CB2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_booked_time_inside_card, parent, false);
                        return new CB2(view2);
                    }
                };
                time_firebaseRecyclerAdapter.startListening();
                time_firebaseRecyclerAdapter.notifyDataSetChanged();
                holder1.time_recyclerView.setAdapter(time_firebaseRecyclerAdapter);
            }

            @NonNull
            @Override
            public CB1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_booked_time_card, parent, false);
                return new CB1(view1);
            }
        };
        hotel_firebaseRecyclerAdapter.startListening();
        hotel_firebaseRecyclerAdapter.notifyDataSetChanged();
        cus_table_booked_recyclerview.setAdapter(hotel_firebaseRecyclerAdapter);
    }
    private void show(){
        customer_booked_food_database=FirebaseDatabase.getInstance().getReference().child("Customer Booked Details").child(main_root[0]).child(food_mail).child(book_root);
        databaseReference   = FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details").child(food_mail);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                available_seats=snapshot.child("_Restaurant_Available_Seats").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseRecyclerOptions<Reg> options3 = new FirebaseRecyclerOptions.Builder<Reg>().setQuery(customer_booked_food_database, Reg.class).build();
        food_firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Reg, CB3>(options3) {
            @Override
            protected void onBindViewHolder(@NonNull CB3 holder, int position, @NonNull Reg model) {
                holder.order_food_id.setText(model.get_Food_Id());
                holder.order_food_name.setText(model.get_Food_Name());
                holder.order_food_price.setText(model.get_Food_Price());
                holder.order_food_qty.setText(model.get_Food_Quantity());
                holder.order_food_cat.setText(model.get_Food_Category());
                Picasso.get().load(model.get_Food_Image_Url()).into(holder.order_food_image);
            }
            @NonNull
            @Override
            public CB3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_booked_food_card, parent, false);
                return new CB3(view3);
            }
        };
        food_firebaseRecyclerAdapter.startListening();
        food_firebaseRecyclerAdapter.notifyDataSetChanged();
        cus_food_booked_recyclerview.setAdapter(food_firebaseRecyclerAdapter);

    }
    public static class CB1 extends RecyclerView.ViewHolder {
        public TextView shopname;
        public RecyclerView time_recyclerView;
        public RecyclerView.LayoutManager manager;
        public CB1(@NonNull View itemView) {
            super(itemView);
            time_recyclerView=itemView.findViewById(R.id.cus_table_booked_recyclerview);
            shopname=itemView.findViewById(R.id.cus_booked_hotel_name);
            manager=new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
            time_recyclerView.setLayoutManager(manager);
        }
    }
    public static class CB2 extends RecyclerView.ViewHolder {
        public TextView bokeed_time,booke_date,booked_status,booked_seats;
        public CardView booked_card_view,cus_booked_time_inside_card_view;
        public CB2(@NonNull View itemView) {
            super(itemView);
            bokeed_time=itemView.findViewById(R.id.cus_booked_time_inside_time);
            booke_date=itemView.findViewById(R.id.cus_booked_time_inside_date);
            booked_status=itemView.findViewById(R.id.cus_booked_time_inside_status);
            booked_seats=itemView.findViewById(R.id.cus_booked_time_inside_seats);
            booked_card_view=itemView.findViewById(R.id.cus_booked_time_inside_card_view);
        }
    }
    public static class CB3 extends RecyclerView.ViewHolder {

        public TextView order_food_id, order_food_name, order_food_price, order_food_qty, order_food_cat;
        public ImageView order_food_image;
        public CB3(@NonNull View itemView) {
            super(itemView);
            order_food_id = itemView.findViewById(R.id.cus_booked_food_id);
            order_food_name = itemView.findViewById(R.id.cus_booked_food_name);
            order_food_price = itemView.findViewById(R.id.cus_booked_food_price);
            order_food_qty = itemView.findViewById(R.id.cus_booked_food_qty);
            order_food_cat = itemView.findViewById(R.id.cus_booked_food_category);
            order_food_image = itemView.findViewById(R.id.cus_booked_food_image);
        }
    }

    public void book_details(View view){

    }
    public void cus_booked_cancel(View view){
        current_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        current_time = new SimpleDateFormat("kk:mm:ss", Locale.getDefault()).format(new Date());

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
        String start_date=date+" "+time;//date+" "+time;//
        String end_date=current_date+" "+current_time;
        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);
            long difference_In_Time = d2.getTime() - d1.getTime();
            long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
            long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));
            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
            if(difference_In_Hours<=-2){
                AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Booked.this);
                builder.setTitle("Do you really cancel bookings and meals?").setMessage("Are you sure?")
                        .setPositiveButton(
                                "Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        k=1;
                                        alert(k);
                                    }
                                }
                        )
                        .setNegativeButton("Cancel", null).setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else if (difference_In_Hours>=-2&&difference_In_Hours<=0){
                AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Booked.this);
                builder.setTitle("Do you really cancel bookings and meals?").setMessage("Are you sure?")
                        .setPositiveButton(
                                "Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toasty.normal(getApplicationContext(), "80%", Toasty.LENGTH_SHORT).show();
                                        k =2;
                                        alert(k);
                                    }
                                }
                        )
                        .setNegativeButton("Cancel", null).setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
        catch (ParseException e) {
            Toasty.normal(getApplicationContext(), e.getMessage(), Toasty.LENGTH_SHORT).show();
        }

    }
    private void alert(int q){
        int c=Integer.parseInt(available_seats);
        int d=Integer.parseInt(booked_seats);
        int a=c+d;
        update_seats = FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details").child(food_mail);
        update_seats.child("_Restaurant_Available_Seats").setValue(String.valueOf(a));
        update1=FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(main_root[0]).child(food_mail).child(book_root);
        update2=FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(food_mail).child(main_root[0]).child(book_root);
        amount_status="Refund";
        status="Cancelled";
        k=0;
        if(q==2){
            charge_status="Yes";
            Double q1=Double.parseDouble(amount_paid);
            Double q2=q1*0.8;
            Double q3=q1*0.2;
            String[] sum1=String.valueOf(q2).split("\\.");
            String[] sum2=String.valueOf(q3).split("\\.");
            charged_amount=sum2[0];
            refunded_amount=sum1[0];
            update1.child("_Amount_Status").setValue(amount_status);
            update1.child("_Charge_Status").setValue(charge_status);
            update1.child("_Charged").setValue(charged_amount);
            update1.child("_Refunded").setValue(refunded_amount);
            update1.child("_Status").setValue(status);
            update1.child("_Request_Date").setValue(current_date);
            update1.child("_Request_Time").setValue(current_time);
            update2.child("_Amount_Status").setValue(amount_status);
            update2.child("_Charge_Status").setValue(charge_status);
            update2.child("_Charged").setValue(charged_amount);
            update2.child("_Refunded").setValue(refunded_amount);
            update2.child("_Status").setValue(status);
            update2.child("_Request_Date").setValue(current_date);
            update2.child("_Request_Time").setValue(current_time);
            AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Booked.this);
            builder.setTitle("Hi User!").setMessage("Your 80% amount refunded shortly")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cus_book_card_one.setVisibility(View.VISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                    cus_book_card_two.setVisibility(View.INVISIBLE);
                                    cus_book_card_three.setVisibility(View.INVISIBLE);
                                }
                            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }else if(q==1) {
            charge_status = "No";
            charged_amount = "Nil";
            refunded_amount = amount_paid;
            update1.child("_Amount_Status").setValue(amount_status);
            update1.child("_Charge_Status").setValue(charge_status);
            update1.child("_Charged").setValue(charged_amount);
            update1.child("_Refunded").setValue(refunded_amount);
            update1.child("_Status").setValue(status);
            update1.child("_Request_Date").setValue(current_date);
            update1.child("_Request_Time").setValue(current_time);
            update2.child("_Amount_Status").setValue(amount_status);
            update2.child("_Charge_Status").setValue(charge_status);
            update2.child("_Charged").setValue(charged_amount);
            update2.child("_Refunded").setValue(refunded_amount);
            update2.child("_Status").setValue(status);
            update2.child("_Request_Date").setValue(current_date);
            update2.child("_Request_Time").setValue(current_time);
            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Booked.this);
            builder.setTitle("Hi User!").setMessage("Your 100% amount refunded shortly")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cus_book_card_one.setVisibility(View.VISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                    cus_book_card_two.setVisibility(View.INVISIBLE);
                                    cus_book_card_three.setVisibility(View.INVISIBLE);
                                }
                            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    @Override
    public void onBackPressed() {
        if(cus_food_booked_recyclerview.isShown()){
            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Booked.this);
            builder.setTitle("Are you really going back?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cus_book_card_one.setVisibility(View.VISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                    cus_book_card_two.setVisibility(View.INVISIBLE);
                                    cus_book_card_three.setVisibility(View.INVISIBLE);
                                }
                            }
                    )
                    .setNegativeButton("Cancel", null).setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Booked.this);
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
    public void cus_booked_back_press(View view){
        if(cus_book_card_two.isShown()){
            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Booked.this);
            builder.setTitle("Are you really going back?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cus_book_card_one.setVisibility(View.VISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                    cus_book_card_two.setVisibility(View.INVISIBLE);
                                    cus_book_card_three.setVisibility(View.INVISIBLE);
                                }
                            }
                    )
                    .setNegativeButton("Cancel", null).setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Booked.this);
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
}
