package com.restaurent.restaurent_table_booking;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

public class Customer_Home extends AppCompatActivity {
    ImageView cus_home_sugg;
    ConstraintLayout cus_constraint;
    LinearLayout cus_home_lin1, cus_home_lin2,cus_home_linear_layout;
    private int LOCATION_PERMISSION_CODE = 1;
    final int UPI_PAYMENT=0;
    TextView cus_home_name, cus_home_food_date, cus_home_food_time, cus_home_food_rate, cus_home_food_seats,cus_payment_amount;
    FirebaseRecyclerAdapter<Reg, CH> firebaseRecyclerAdapter, firebaseRecyclerAdapter1;
    FirebaseRecyclerAdapter<Reg, CHO> order_food_firebaseRecyclerAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference1, order_food_databaseReference,seat;
    TextView call, mail, shopname, showmap;
    RecyclerView cus_home_hotel_recyclerview, cus_home_food_recyclerview;
    SearchView cus_home_hotel_search;
    String root, phone, location, str_mail, hotel_name, hotel_cat, hotel_address;
    String[] main_root, food_root;
    int cYear, cMonth, cDay, sYear, sMonth, sDay;
    public java.util.Calendar calendar1;
    int cHour, cMinute, sHour, sMinute;
    String date, time, current_date = "current", current_time = "current", final_amount = "0.0";
    EditText cus_home_enter_seat,cus_upi_id;
    ArrayList<String> food_id,food_name,food_qty,food_price,food_url,food_order_qty,final_qty,food_cat,update_qty;
    ArrayList<Integer> from_list,to_list;
    String sender_upi,receiver_upi,no_of_seats,customer_name,customer_mail,customer_phone,amount_status="Unpaid",status,charge_status,charged,refunded;
    String request_date,request_time,return_date,return_time,key,amount;
    HashMap<Integer, Integer> food_id_qty;
    ProgressDialog progressDialog;
    BottomNavigationView bottomNavigationView;
    Button cus_home_pay;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        FirebaseApp.initializeApp(this);
        try {
            Picasso.setSingletonInstance(new Picasso.Builder(this).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressDialog=new ProgressDialog(Customer_Home.this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Getting Hotel Information..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        root = getIntent().getStringExtra("mail");
        main_root = root.split("\\.");

        final Calendar calendar = Calendar.getInstance();
        cYear = calendar.get(Calendar.YEAR);
        cMonth = calendar.get(Calendar.MONTH);
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        cHour = calendar.get(Calendar.HOUR_OF_DAY);
        cMinute = calendar.get(Calendar.MINUTE);

        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        time = new SimpleDateFormat("kk:mm:ss", Locale.getDefault()).format(new Date());

        cus_home_lin1 = findViewById(R.id.cus_home_lin1);
        cus_home_lin2 = findViewById(R.id.cus_home_lin2);

        cus_constraint              = findViewById(R.id.cus_constraint);
        cus_payment_amount          = findViewById(R.id.cus_payment_amount);
        cus_upi_id                  = findViewById(R.id.cus_upi_id);
        cus_home_name               = findViewById(R.id.cus_home_name);
        cus_home_food_date          = findViewById(R.id.cus_home_food_date);
        cus_home_food_time          = findViewById(R.id.cus_home_food_time);
        cus_home_food_rate          = findViewById(R.id.cus_home_food_rate);
        cus_home_food_seats         = findViewById(R.id.cus_home_food_seats);
        cus_home_enter_seat         = findViewById(R.id.cus_home_enter_seat);
        cus_home_hotel_search       = findViewById(R.id.cus_home_hotel_search);
        cus_home_linear_layout      = findViewById(R.id.cus_home_linear_layout);

        food_id         = new ArrayList<>();
        food_name       = new ArrayList<>();
        food_price      = new ArrayList<>();
        food_qty        = new ArrayList<>();
        food_url        = new ArrayList<>();
        food_cat        = new ArrayList<>();
        food_order_qty  = new ArrayList<>();
        from_list       = new ArrayList<>();
        to_list         = new ArrayList<>();
        final_qty       = new ArrayList<>();
        update_qty      = new ArrayList<>();
        cus_home_hotel_recyclerview = findViewById(R.id.cus_home_hotel_recyclerview);
        cus_home_hotel_recyclerview.setHasFixedSize(true);
        cus_home_hotel_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        cus_home_food_recyclerview  = findViewById(R.id.cus_home_food_recyclerview);
        cus_home_food_recyclerview.setHasFixedSize(true);
        cus_home_food_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase    = FirebaseDatabase.getInstance();
        databaseReference   = firebaseDatabase.getReference().child("Restaurant Registration Details");
        databaseReference1  = firebaseDatabase.getReference().child("Customer Registration Details").child(main_root[0]);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String n = "Hi, " + snapshot.child("_Customer_Name").getValue().toString() + "!";
                customer_name=snapshot.child("_Customer_Name").getValue().toString();
                customer_mail=snapshot.child("_Customer_Mail_Id").getValue().toString();
                customer_phone=snapshot.child("_Customer_Phone").getValue().toString();
                sender_upi = snapshot.child("_Customer_UPI").getValue().toString();
                cus_home_name.setText(n);
                cus_upi_id.setText(sender_upi);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        food_id_qty=new HashMap<Integer, Integer>();
        bottomNavigationView = findViewById(R.id.cus_home_navigat);
        bottomNavigationView.setSelectedItemId(R.id.cus_menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cus_menu_home:
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

                        Intent i3 = new Intent(getApplicationContext(), Customer_Time_Slot.class);
                        i3.putExtra("mail", root);
                        startActivity(i3);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.cus_menu_profile:
                        Intent i4 = new Intent(getApplicationContext(), Customer_Profile.class);
                        i4.putExtra("mail", root);
                        startActivity(i4);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        cus_home_sugg = findViewById(R.id.cus_home_sugg);
        cus_home_sugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission
                        (Customer_Home.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toasty.success(Customer_Home.this, "Permission GRANTED", Toasty.LENGTH_SHORT).show();
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=restaurants near me");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } else {
                    requestLocationPermission();
                }

            }
        });
        cus_home_food_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Customer_Home.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        sYear = year;
                        sMonth = month;
                        sDay = dayOfMonth;
                        String[] sDate = date.split("-");
                        int ssDay = Integer.parseInt(sDate[0]);
                        if(ssDay==dayOfMonth){
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                            String dateInString = date+" 18:59:00";
                            try {
                                Date  date1 = sdf.parse(dateInString);
                                java.util.Calendar calendar = java.util.Calendar.getInstance();
                                calendar.setTime(date1);
                                if(calendar.getTimeInMillis()>=java.util.Calendar.getInstance().getTimeInMillis()){
                                    if (sDay >= 1 && sDay <= 9) {
                                        String zero = "0" + sDay;
                                        current_date = zero + "-" + (sMonth + 1) + "-" + sYear;
                                        cus_home_food_date.setText(current_date);
                                    } else {
                                        current_date = sDay + "-" + (sMonth + 1) + "-" + sYear;
                                        cus_home_food_date.setText(current_date);
                                    }
                                }
                                else{
                                    Toasty.error(getApplicationContext(), "Time is over! Select Next Day for Booking", Toasty.LENGTH_SHORT).show();
                                }

                            } catch (ParseException e) {
                                Toasty.error(getApplicationContext(),e.getMessage(), Toasty.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            if (sDay >= 1 && sDay <= 9) {
                                String zero = "0" + sDay;
                                current_date = zero + "-" + (sMonth + 1) + "-" + sYear;
                                cus_home_food_date.setText(current_date);
                            } else {
                                current_date = sDay + "-" + (sMonth + 1) + "-" + sYear;
                                cus_home_food_date.setText(current_date);
                            }
                        }

                    }
                }, cYear, cMonth, cDay
                );
                datePickerDialog.updateDate(sYear, sMonth, sDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        cus_home_food_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_date.equals("current"))
                {
                    Toasty.error(getApplicationContext(), "Please Select Date first", Toasty.LENGTH_SHORT).show();
                }
                else {
                    if (current_date.equals(date)) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(Customer_Home.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                sHour = hourOfDay;
                                sMinute = minute;

                                calendar1 = java.util.Calendar.getInstance();
                                String[] sDate = date.split("-");
                                int sDay = Integer.parseInt(sDate[0]);
                                calendar1.set(java.util.Calendar.DAY_OF_MONTH, sDay);
                                calendar1.set(java.util.Calendar.HOUR_OF_DAY, sHour);
                                calendar1.set(java.util.Calendar.MINUTE, sMinute);

                                if (calendar1.getTimeInMillis() > (java.util.Calendar.getInstance().getTimeInMillis() + (60 * 60 * 1000) + (60 * 60 * 1000))) {
                                    if (sHour <= 21) {
                                        if (sMinute >= 0 && sMinute <= 9) {
                                            current_time = sHour + ":0" + sMinute;
                                            cus_home_food_time.setText(current_time);
                                        } else {
                                            current_time = sHour + ":" + sMinute;
                                            cus_home_food_time.setText(current_time);
                                        }
                                    } else {
                                        Toasty.error(getApplicationContext(), "Please Select Time before 9PM", Toasty.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toasty.error(getApplicationContext(), "Please Select Future Time", Toasty.LENGTH_SHORT).show();
                                }
                            }
                        }, cHour, cMinute, false);
                        timePickerDialog.show();
                    }
                    else {
                        TimePickerDialog timePickerDialog2 = new TimePickerDialog(Customer_Home.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                sHour = hourOfDay;
                                sMinute = minute;

                                if (sHour >= 6 && sHour < 21) {
                                    if (sHour >=6 && sHour <= 9 ) {
                                        if(sMinute>=0 && sMinute<=9){
                                            current_time ="0" + sHour + ":0" + sMinute;
                                            cus_home_food_time.setText(current_time);
                                        }
                                        else{
                                            current_time ="0" + sHour + ":" + sMinute;
                                            cus_home_food_time.setText(current_time);
                                        }

                                    }
                                    else if( sHour >=10 && sHour < 21){
                                        if(sMinute>=0 && sMinute<=9){
                                            current_time = sHour + ":0" + sMinute;
                                            cus_home_food_time.setText(current_time);
                                        }
                                        else{
                                            current_time = sHour + ":" + sMinute;
                                            cus_home_food_time.setText(current_time);
                                        }
                                    }
                                    else {
                                        current_time = sHour + ":" + sMinute;
                                        cus_home_food_time.setText(current_time);
                                    }
                                }
                                else if(sMinute==0 && sHour==21){
                                    current_time = sHour + ":0" + sMinute;
                                    cus_home_food_time.setText(current_time);
                                }else {
                                    Toasty.error(getApplicationContext(), "Do not Select Time after 9PM", Toasty.LENGTH_SHORT).show();
                                }

                            }
                        }, cHour, cMinute, false);
                        timePickerDialog2.show();
                    }
                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

        if (cus_home_hotel_search != null)
            cus_home_hotel_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    firebaseSearch(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    firebaseSearch(newText);
                    return false;
                }
            });
        FirebaseRecyclerOptions<Reg> options = new FirebaseRecyclerOptions.Builder<Reg>().setQuery(databaseReference, Reg.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Reg, CH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CH holder, final int position, @NonNull final Reg model) {
                holder.shopname.setText(model.get_Restaurant_Name());
                holder.shopcat.setText(model.get_Restaurant_Category());
                holder.shopphone.setText(model.get_Restaurant_Phone());
                holder.shopemail.setText(model.get_Restaurant_Mail_Id());
                holder.shopadd.setText(model.get_Restaurant_Address());
                holder.shopseats.setText(model.get_Restaurant_Available_Seats());
                holder.totalseats.setText(model.get_Restaurant_Total_Seats());
                phone = model.get_Restaurant_Phone();
                location = model.get_Restaurant_Address();
                holder.shopotion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        call = view.findViewById(R.id.cus_home_rest_phone);
                        mail = view.findViewById(R.id.cus_home_rest_mail);
                        shopname = view.findViewById(R.id.cus_home_rest_name);
                        showmap = view.findViewById(R.id.cus_home_rest_add);
                        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                        popupMenu.getMenu().add("Call").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + phone));//change the number
                                if (ContextCompat.checkSelfPermission(Customer_Home.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    Toasty.error(getApplicationContext(), "No call Permission", Toast.LENGTH_LONG).show();
                                } else {
                                    startActivity(callIntent);
                                }
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("WhatsApp").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                String url = "https://api.whatsapp.com/send?phone=" + "tel:" + phone;
                                try {
                                    PackageManager pm = getPackageManager();
                                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                } catch (PackageManager.NameNotFoundException e) {
                                    Toasty.error(getApplicationContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Show Location").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                try {
                                    //When google map is installed.
                                    //Initialize Uri
                                    Uri uri = Uri.parse("https://www.google.co.in/maps/place/" + location);
                                    //Initialize intent with action view
                                    //https://www.google.co.in/maps/place/12, Raja street, kinathukadavu, kinathukadavu, Coimbatore, Tamil Nadu, 642109
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    //Set package
                                    intent.setPackage("com.google.android.apps.maps");
                                    //Set flag
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //Start Activity
                                    startActivity(intent);

                                } catch (ActivityNotFoundException e) {
                                    //When google map is not installed
                                    //Initialize Uri
                                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                                    //Initialize intent with action view
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    //Set flag
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //Start Activity
                                    startActivity(intent);
                                }
                                return false;

                            }
                        });
                        popupMenu.getMenu().add("Order").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                str_mail = model.get_Restaurant_Mail_Id();
                                food_root = str_mail.split("\\.");
                                hotel_name = model.get_Restaurant_Name();
                                hotel_cat = model.get_Restaurant_Category();
                                hotel_address = model.get_Restaurant_Address();
                                cus_home_food_seats.setText(model.get_Restaurant_Available_Seats());
                                receiver_upi =model.get_Restaurant_UPI();
                                no_of_seats =model.get_Restaurant_Available_Seats();
                                cus_home_lin1.setVisibility(View.INVISIBLE);
                                bottomNavigationView.setVisibility(View.INVISIBLE);
                                cus_home_sugg.setVisibility(View.INVISIBLE);
                                cus_home_lin2.setVisibility(View.VISIBLE);
                                key=getRef(position).getKey();
                                show();
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }
            @NonNull
            @Override
            public CH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_home_rest_card, parent, false);
                return new CH(view);
            }
        };
        progressDialog.dismiss();
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        cus_home_hotel_recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    private void firebaseSearch(String searchText) {
        String one = searchText.toLowerCase();
        String two = searchText.toUpperCase();
        Query q = databaseReference.orderByChild("_Restaurant_Name").startAt(two).endAt(one + "\uf0ff");
        FirebaseRecyclerOptions<Reg> options = new FirebaseRecyclerOptions.Builder<Reg>().setQuery(q, Reg.class).build();
        firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<Reg, CH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CH holder, final int position, @NonNull final Reg model) {
                holder.shopname.setText(model.get_Restaurant_Name());
                holder.shopcat.setText(model.get_Restaurant_Category());
                holder.shopphone.setText(model.get_Restaurant_Phone());
                holder.shopemail.setText(model.get_Restaurant_Mail_Id());
                holder.shopadd.setText(model.get_Restaurant_Address());
                holder.shopotion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        call = view.findViewById(R.id.cus_home_rest_phone);
                        mail = view.findViewById(R.id.cus_home_rest_mail);
                        shopname = view.findViewById(R.id.cus_home_rest_name);
                        showmap = view.findViewById(R.id.cus_home_rest_add);
                        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                        popupMenu.getMenu().add("Call").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Toasty.success(Customer_Home.this, "Call", Toast.LENGTH_SHORT).show();
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse(call.getText().toString()));//change the number
                                if (ContextCompat.checkSelfPermission(Customer_Home.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    Toasty.error(getApplicationContext(), "No call Permission", Toast.LENGTH_LONG).show();
                                } else {
                                    startActivity(callIntent);
                                }
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("WhatsApp").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Toasty.success(Customer_Home.this, "WhatsApp", Toast.LENGTH_SHORT).show();
                                String url = "https://api.whatsapp.com/send?phone=" + call.getText().toString();
                                try {
                                    PackageManager pm = getPackageManager();
                                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
                                } catch (PackageManager.NameNotFoundException e) {
                                    Toasty.error(getApplicationContext(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Show Location").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                try {
                                    //When google map is installed.
                                    //Initialize Uri
                                    Uri uri = Uri.parse("https://www.google.co.in/maps/place/" + showmap.getText().toString());
                                    //Initialize intent with action view
                                    //https://www.google.co.in/maps/place/12, Raja street, kinathukadavu, kinathukadavu, Coimbatore, Tamil Nadu, 642109
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    //Set package
                                    intent.setPackage("com.google.android.apps.maps");
                                    //Set flag
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //Start Activity
                                    startActivity(intent);

                                } catch (ActivityNotFoundException e) {
                                    //When google map is not installed
                                    //Initialize Uri
                                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                                    //Initialize intent with action view
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    //Set flag
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //Start Activity
                                    startActivity(intent);
                                }
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Order").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                str_mail = model.get_Restaurant_Mail_Id();
                                food_root = str_mail.split("\\.");
                                hotel_name = model.get_Restaurant_Name();
                                hotel_cat = model.get_Restaurant_Category();
                                hotel_address = model.get_Restaurant_Address();
                                cus_home_food_seats.setText(model.get_Restaurant_Available_Seats());
                                receiver_upi =model.get_Restaurant_UPI();
                                no_of_seats =model.get_Restaurant_Available_Seats();
                                cus_home_lin1.setVisibility(View.INVISIBLE);
                                bottomNavigationView.setVisibility(View.INVISIBLE);
                                cus_home_sugg.setVisibility(View.INVISIBLE);
                                cus_home_lin2.setVisibility(View.VISIBLE);
                                key=getRef(position).getKey();
                                show();
                                return false;
                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public CH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_home_rest_card, parent, false);
                return new CH(view);
            }
        };
        progressDialog.dismiss();
        firebaseRecyclerAdapter1.startListening();
        firebaseRecyclerAdapter1.notifyDataSetChanged();
        cus_home_hotel_recyclerview.setAdapter(firebaseRecyclerAdapter1);
    }
    private void show() {
        order_food_databaseReference = FirebaseDatabase.getInstance().getReference().child("Stock").child(food_root[0]);
        order_food_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double sum = 0.0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String, Object> id = (Map<String, Object>) ds.getValue();
                    Object fid = id.get("_Food_Id");
                    Map<String, Object> name = (Map<String, Object>) ds.getValue();
                    Object fname = name.get("_Food_Name");
                    Map<String, Object> price = (Map<String, Object>) ds.getValue();
                    Object fprice = price.get("_Food_Price");
                    Map<String, Object> qty = (Map<String, Object>) ds.getValue();
                    Object fqty = qty.get("_Food_Quantity");
                    Map<String, Object> url = (Map<String, Object>) ds.getValue();
                    Object furl = url.get("_Food_Image_Url");
                    Map<String, Object> cat = (Map<String, Object>) ds.getValue();
                    Object fcat = cat.get("_Food_Category");
                    Double qt = Double.parseDouble(String.valueOf(fqty));
                    Double cost = Double.parseDouble(String.valueOf(fprice));
                    Double ans = qt * cost;
                    sum += ans;
                    sum = Double.parseDouble(String.format("%f", sum));
                    String k = String.valueOf(sum);
                    String[] s;
                    s = k.split("\\.");
                    final_amount = s[0];
                    cus_home_food_rate.setText(final_amount);
                    food_id.add(String.valueOf(fid));
                    food_name.add(String.valueOf(fname));
                    food_price.add(String.valueOf(fprice));
                    food_qty.add(String.valueOf(fqty));
                    food_url.add(String.valueOf(furl));
                    food_cat.add(String.valueOf(fcat));
                    food_id_qty.put(Integer.parseInt(String.valueOf(fid)),Integer.parseInt(String.valueOf(fqty)));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerOptions<Reg> options = new FirebaseRecyclerOptions.Builder<Reg>().setQuery(order_food_databaseReference, Reg.class).build();
        order_food_firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Reg, CHO>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CHO holder, final int position, @NonNull final Reg model) {
                holder.order_food_id.setText(model.get_Food_Id());
                holder.order_food_name.setText(model.get_Food_Name());
                holder.order_food_price.setText(model.get_Food_Price());
                holder.order_food_qty.setText(model.get_Food_Quantity());
                holder.order_food_cat.setText(model.get_Food_Category());
                Picasso.get().load(model.get_Food_Image_Url()).into(holder.order_food_image);
                holder.order_food_number_button.setNumber(String.valueOf(model.get_Food_Quantity()));
                holder.order_food_number_button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                    @Override
                    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                        if (!(newValue > Integer.parseInt(model.get_Food_Quantity()))) {
                            if (!(oldValue > Integer.parseInt(model.get_Food_Quantity()))) {
                                Double n = Double.valueOf(newValue);
                                Double o = Double.valueOf(oldValue);
                                Double qty = Double.valueOf(model.get_Food_Price());
                                Double res1 = Double.parseDouble(final_amount) - o * qty;
                                Double res2 = res1 + n * qty;
                                final_amount = String.valueOf(res2);
                                cus_home_food_rate.setText(final_amount);
                                Integer up1=Integer.parseInt(model.get_Food_Id());
                                int k=food_id_qty.get(up1);
                                if(newValue>oldValue){
                                    k=k+1;
                                    food_id_qty.put(up1,k);
                                }
                                else if(oldValue>newValue){
                                    k=k-1;
                                    food_id_qty.put(up1,k);
                                }
                            }
                        } else {
                            Toasty.error(getApplicationContext(), "Please do not increase the value", Toasty.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @NonNull
            @Override
            public CHO onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_home_food_card, parent, false);
                return new CHO(view);
            }
        };
        order_food_firebaseRecyclerAdapter.startListening();
        order_food_firebaseRecyclerAdapter.notifyDataSetChanged();
        cus_home_food_recyclerview.setAdapter(order_food_firebaseRecyclerAdapter);
    }

    public static class CH extends RecyclerView.ViewHolder {
        public TextView shopname, shopemail, shopphone, shopadd, shopcat, shopseats, totalseats;
        public ImageView shopotion;

        public CH(@NonNull View itemView) {
            super(itemView);
            shopname = itemView.findViewById(R.id.cus_home_rest_name);
            shopemail = itemView.findViewById(R.id.cus_home_rest_mail);
            shopphone = itemView.findViewById(R.id.cus_home_rest_phone);
            shopadd = itemView.findViewById(R.id.cus_home_rest_add);
            shopcat = itemView.findViewById(R.id.cus_home_rest_cat);
            shopotion = itemView.findViewById(R.id.cus_home_rest_option);
            shopseats = itemView.findViewById(R.id.cus_home_rest_avail_seats);
            totalseats = itemView.findViewById(R.id.cus_home_rest_total_seats);
        }
    }

    public static class CHO extends RecyclerView.ViewHolder {

        public TextView order_food_id, order_food_name, order_food_price, order_food_qty, order_food_cat;
        public ImageView order_food_image;
        public ElegantNumberButton order_food_number_button;

        public CHO(@NonNull View itemView) {
            super(itemView);
            order_food_id = itemView.findViewById(R.id.cus_home_order_food_id);
            order_food_name = itemView.findViewById(R.id.cus_home_order_food_name);
            order_food_price = itemView.findViewById(R.id.cus_home_order_food_price);
            order_food_qty = itemView.findViewById(R.id.cus_home_order_food_qty);
            order_food_cat = itemView.findViewById(R.id.cus_home_order_food_category);
            order_food_image = itemView.findViewById(R.id.cus_home_order_food_image);
            order_food_number_button = itemView.findViewById(R.id.cus_home_order_food_elegant);
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Customer_Home.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(Customer_Home.this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed for suggests the nearest restaurant for you!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Customer_Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();

        } else {
            ActivityCompat.requestPermissions(Customer_Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toasty.success(Customer_Home.this, "Permission GRANTED", Toasty.LENGTH_SHORT).show();
            } else {
                Toasty.error(Customer_Home.this, "Permission DENIED", Toasty.LENGTH_SHORT).show();
            }
        }
    }
    //add to calendar,
    public void cus_home_pay(View view){
        String amount1,supi,rupi,name;
        if(isUpiChecked()){
             amount1 = amount;
             supi = cus_upi_id.getEditableText().toString().trim();
             name = customer_name;
             rupi = receiver_upi;
            payUsingUpi(amount1, rupi, name, supi);
        }
        else if(!isUpiChecked()){
             amount1= amount;
             supi = cus_upi_id.getEditableText().toString().trim();
             name = customer_name;
             rupi = receiver_upi;
            payUsingUpi(amount1, rupi, name, supi);
        }


    }
    public void add_to_calendar(View view) {
        if(!validateSeat())
            return;
        if (!current_date.equals("current") && !current_time.equals("current")) {

        if(Integer.parseInt(cus_home_food_seats.getText().toString())>=Integer.parseInt(cus_home_enter_seat.getText().toString())) {
            String k = cus_home_food_rate.getText().toString();
            String[] fin = k.split("\\.");
            amount = fin[0];
            cus_payment_amount.setText(amount);
            cus_home_lin1.setVisibility(View.INVISIBLE);
            bottomNavigationView.setVisibility(View.INVISIBLE);
            cus_home_sugg.setVisibility(View.INVISIBLE);
            cus_home_lin2.setVisibility(View.INVISIBLE);
            cus_constraint.setVisibility(View.VISIBLE);
        }
        else {
            Toasty.error(getApplicationContext(),"Please enter Correct Value!",Toasty.LENGTH_SHORT).show();
            cus_home_enter_seat.setError("Please enter Correct Value!");
        }

        }
        else {
            Toasty.error(getApplicationContext(), "Select date and time first", Toasty.LENGTH_SHORT).show();

        }

    }
    private boolean isUpiChecked() {
        if(!sender_upi.equals(cus_upi_id.getEditableText().toString().trim())){
            databaseReference1.child("_Customer_UPI").setValue(cus_upi_id.getEditableText().toString().trim());
            return true;
        }
        else{
            return false;
        }
    }
    private Boolean validateSeat() {
        String val1 = cus_home_enter_seat.getText().toString().trim();
        if (val1.isEmpty()) {
            cus_home_enter_seat.setError("Fields cannot be empty");
            return false;
        } else if(Integer.parseInt(val1)<=0){
            cus_home_enter_seat.setError("Fields cannot be <= 0");
            return false;
        }
        else  {
            cus_home_enter_seat.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(cus_home_lin2.getVisibility()==View.VISIBLE){
            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Home.this);
            builder.setTitle("Are you really going back?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cus_home_lin1.setVisibility(View.VISIBLE);
                                    cus_home_lin2.setVisibility(View.INVISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                }
                            }
                    )
                    .setNegativeButton("Cancel", null).setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(cus_constraint.getVisibility()==View.VISIBLE){
            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Home.this);
            builder.setTitle("Are you really going back?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cus_home_food_date.setText("Select Date");
                                    cus_home_food_time.setText("Select Time");
                                    cus_home_food_seats.setText("Amount");
                                    cus_home_food_rate.setText("");;
                                    cus_home_enter_seat.setText("");
                                    cus_upi_id.setText("");
                                    cus_home_lin1.setVisibility(View.VISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                    cus_home_sugg.setVisibility(View.VISIBLE);
                                    cus_home_lin2.setVisibility(View.INVISIBLE);
                                    cus_constraint.setVisibility(View.INVISIBLE);
                                }
                            }
                    )
                    .setNegativeButton("Cancel", null).setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Home.this);
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
    public void backpress(View view) {
        if(cus_home_lin2.getVisibility()==View.VISIBLE){
            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Home.this);
            builder.setTitle("Are you really going back?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cus_home_lin1.setVisibility(View.VISIBLE);
                                    cus_home_lin2.setVisibility(View.INVISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                }
                            }
                    )
                    .setNegativeButton("Cancel", null).setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else if(cus_constraint.getVisibility()==View.VISIBLE){
            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Home.this);
            builder.setTitle("Are you really going back?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cus_home_food_date.setText("Select Date");
                                    cus_home_food_time.setText("Select Time");
                                    cus_home_food_seats.setText("Amount");
                                    cus_home_food_rate.setText("");;
                                    cus_home_enter_seat.setText("");
                                    cus_upi_id.setText("");
                                    cus_home_lin1.setVisibility(View.VISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                    cus_home_sugg.setVisibility(View.VISIBLE);
                                    cus_home_lin2.setVisibility(View.INVISIBLE);
                                    cus_constraint.setVisibility(View.INVISIBLE);
                                }
                            }
                    )
                    .setNegativeButton("Cancel", null).setCancelable(false);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(Customer_Home.this);
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
    public void payemnt_success(){
        int update_seat=Integer.parseInt(cus_home_food_seats.getText().toString())-Integer.parseInt(cus_home_enter_seat.getText().toString());
        String fin[]=final_amount.split("\\.");
        amount_status="Paid";
        final_qty.clear();
        status="Not Finished";
        charge_status="No";
        charged="0";
        refunded="0";
        request_date="Nil";
        request_time="Nil";
        return_date="Nil";
        return_time="Nil";
        StringBuilder finq= new StringBuilder();
        for(int f = 0; f<food_qty.size(); f++){
            int s=Integer.parseInt(food_id.get(f));
            final_qty.add(String.valueOf(Integer.parseInt(food_qty.get(f))-Integer.parseInt(String.valueOf(food_id_qty.get(s)))));
            finq.append(final_qty.get(f)).append(" ");
        }
        StringBuilder foq= new StringBuilder();
        for (int k=0;k<food_qty.size();k++){
            int s=Integer.parseInt(food_id.get(k));
            foq.append(food_id_qty.get(s)).append(" ");
            update_qty.add(String.valueOf(food_id_qty.get(s)));
        }
        String k=date+"+"+time+"+"+current_date+"+"+current_time+":00";
        DatabaseReference update_database,booking_time_database,booking_details_database,update_seats;
        DatabaseReference customer_booking_time_database,restaurant_booking_time_database;
        DatabaseReference customer_booking_details_database,restaurant_booking_details_database;
        DatabaseReference customer_restaurant_details,resaturant_customer_details;

        customer_restaurant_details=FirebaseDatabase.getInstance().getReference().child("Customer Restaurant Details").child(main_root[0]).child(food_root[0]);
        resaturant_customer_details=FirebaseDatabase.getInstance().getReference().child("Restaurant Customer Details").child(food_root[0]).child(main_root[0]);

        Book bk=new Book(hotel_name,food_root[0]);
        customer_restaurant_details.setValue(bk);
        Reg rg=new Reg(customer_name,main_root[0]);
        resaturant_customer_details.setValue(rg);

        update_seats=FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details").child(food_root[0]);
        update_database=FirebaseDatabase.getInstance().getReference().child("Stock").child(food_root[0]);

        customer_booking_time_database=FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(main_root[0]).child(food_root[0]);
        restaurant_booking_time_database=FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(food_root[0]).child(main_root[0]);

        customer_booking_details_database=FirebaseDatabase.getInstance().getReference().child("Customer Booked Details").child(main_root[0]).child(food_root[0]).child(k);
        restaurant_booking_details_database=FirebaseDatabase.getInstance().getReference().child("Customer Booked Details").child(food_root[0]).child(main_root[0]).child(k);

        Book book=new Book(hotel_name,str_mail,customer_name,customer_mail,customer_phone,cus_home_enter_seat.getText().toString(),
                current_date,current_time+":00",fin[0],amount_status,date,time,status,charge_status,charged,refunded,
                request_date,request_time,return_date,return_time);

        customer_booking_time_database.child(date+"+"+time+"+"+current_date+"+"+current_time+":00").setValue(book);
        restaurant_booking_time_database.child(date+"+"+time+"+"+current_date+"+"+current_time+":00").setValue(book);

        update_seats.child("_Restaurant_Available_Seats").setValue(String.valueOf(update_seat));

        for(int i=0;i<food_id.size();i++){
            int food=Integer.parseInt(food_id.get(i));
            if(Integer.parseInt(String.valueOf(food_id_qty.get(food)))!=0){
                update_database.child(food_id.get(i)).child("_Food_Quantity").setValue(final_qty.get(i));
                Reg reg=new Reg(food_id.get(i),food_name.get(i),food_cat.get(i),food_price.get(i),String.valueOf(food_id_qty.get(food)),food_url.get(i));
                customer_booking_details_database.child(food_id.get(i)).setValue(reg);
                restaurant_booking_details_database.child(food_id.get(i)).setValue(reg);
            }
        }

        String[] startTime = current_time.split(":");
        int startHourint = Integer.parseInt(startTime[0].trim());
        int startMinint = Integer.parseInt(startTime[0].trim());
        int plusone = Integer.parseInt((startTime[0].trim()));
        int endHourint = plusone + 1;
        int endMinint = Integer.parseInt(startTime[0].trim());
        java.util.Calendar startEvent = java.util.Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String dateString1 = current_date + " " + current_time+":00";
        String dateString2 = current_date + " " + String.valueOf(endHourint)+":"+endMinint+":00";
        java.util.Calendar calendar_final1 = java.util.Calendar.getInstance();
        java.util.Calendar calendar_final2 = java.util.Calendar.getInstance();
        try {
            //formatting the dateString to convert it into a Date
            Date date1 = sdf.parse(dateString1);
            Date date2 = sdf.parse(dateString2);
            //Setting the Calendar date and time to the given date and time
            calendar_final1.setTime(date1);
            calendar_final2.setTime(date2);
            startEvent.setTimeInMillis(calendar_final1.getTimeInMillis());
            startEvent.set(java.util.Calendar.HOUR_OF_DAY, startHourint);
            startEvent.set(java.util.Calendar.MINUTE, startMinint);
            java.util.Calendar endEvent = java.util.Calendar.getInstance();
            endEvent.setTimeInMillis(calendar_final2.getTimeInMillis());
            endEvent.set(java.util.Calendar.HOUR_OF_DAY, endHourint);
            endEvent.set(java.util.Calendar.MINUTE, endMinint);

            SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String startEventime = calendarDateFormat.format(startEvent.getTime());
            String endEventime = calendarDateFormat.format(endEvent.getTime());

            Date start = calendarDateFormat.parse(startEventime);
            Date end = calendarDateFormat.parse(endEventime);
            ContentResolver cr = getContentResolver();
            ContentValues event = new ContentValues();
            event.put(CalendarContract.Events.CALENDAR_ID, 1);
            event.put(CalendarContract.Events.TITLE, hotel_name);
            event.put(CalendarContract.Events.DESCRIPTION, hotel_cat);
            event.put(CalendarContract.Events.EVENT_LOCATION, hotel_address);
            event.put(CalendarContract.Events.DTSTART,start.getTime());
            event.put(CalendarContract.Events.DTEND,end.getTime());
            event.put(CalendarContract.Events.ALL_DAY, 0);
            event.put(CalendarContract.Events.HAS_ALARM, 1);

            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, event);
            long eventID = Long.parseLong(uri.getLastPathSegment());

            ContentValues reminder = new ContentValues();
            reminder.put(CalendarContract.Reminders.EVENT_ID, eventID);
            reminder.put(CalendarContract.Reminders.MINUTES, 100);
            reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder);
            AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Home.this);
            builder.setTitle("Hi "+customer_name+"!").setMessage("Successfully booked the table in this "+hotel_name+"!")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cus_home_food_date.setText("Select Date");
                                    cus_home_food_time.setText("Select Time");
                                    cus_home_food_seats.setText("Amount");
                                    cus_home_food_rate.setText("");;
                                    cus_home_enter_seat.setText("");
                                    cus_upi_id.setText("");
                                    cus_home_lin1.setVisibility(View.VISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                    cus_home_sugg.setVisibility(View.VISIBLE);
                                    cus_home_lin2.setVisibility(View.INVISIBLE);
                                    cus_constraint.setVisibility(View.INVISIBLE);
                                }
                            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
        catch (ParseException e){
            e.printStackTrace();
            Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_SHORT).show();
        }
    }
    public void payUsingUpi(String amount, String rupi, String name, String supi) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", rupi)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", supi)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toasty.error(Customer_Home.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(Customer_Home.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toasty.success(Customer_Home.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
                payemnt_success();

            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toasty.info(Customer_Home.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toasty.warning(Customer_Home.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toasty.warning(Customer_Home.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Customer_Home context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Customer_Home.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}