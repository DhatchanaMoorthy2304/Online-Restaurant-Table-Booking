package com.restaurent.restaurent_table_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

public class Restaurant_Payment extends AppCompatActivity {
    ConstraintLayout rest_constraint;
    CardView rest_pay_card1;
    TextView rest_payment_amount;
    EditText rest_upi_id;
    final int UPI_PAYMENT=0;
    RecyclerView rest_payment_details_recyclerview;
    DatabaseReference customer_booked_time_database,cus_rest_databaseReference,upi_databaseReference1,upi_databaseReference2,update1,update2;
    FirebaseRecyclerAdapter<Reg, RP1> hotel_firebaseRecyclerAdapter;
    FirebaseRecyclerAdapter<Book, RP2> payment_firebaseRecyclerAdapter;
    String root,refund_amount,key,upi_Id,book_root,food_mail,amount_status,current_date,current_time,cus_upi,res_name;
    String[] main_root;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_payment);
        FirebaseApp.initializeApp(this);

        rest_payment_details_recyclerview=findViewById(R.id.rest_payment_details_recyclerview);
        rest_payment_details_recyclerview.setHasFixedSize(true);
        rest_payment_details_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        rest_constraint=findViewById(R.id.rest_constraint);
        rest_pay_card1=findViewById(R.id.rest_pay_card1);
        rest_payment_amount=findViewById(R.id.rest_payment_amount);
        rest_upi_id=findViewById(R.id.rest_upi_id);

        root=getIntent().getStringExtra("mail");
        main_root=root.split("\\.");
         bottomNavigationView=findViewById(R.id.restaurant_home_navigat);
        upi_databaseReference1           = FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details").child(main_root[0]);
        upi_databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                upi_Id=snapshot.child("_Restaurant_UPI").getValue().toString();
                rest_upi_id.setText(snapshot.child("_Restaurant_UPI").getValue().toString());
                res_name=snapshot.child("_Restaurant_Name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        customer_booked_time_database   = FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(main_root[0]);
        cus_rest_databaseReference      = FirebaseDatabase.getInstance().getReference().child("Restaurant Customer Details").child(main_root[0]);
        bottomNavigationView.setSelectedItemId(R.id.rest_menu_payment);
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
                        Intent i2=new Intent(getApplicationContext(),Restaurant_Booked.class);
                        i2.putExtra("mail",root);
                        startActivity(i2);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.rest_menu_payment:
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
        FirebaseRecyclerOptions<Reg> options1 = new FirebaseRecyclerOptions.Builder<Reg>().setQuery(cus_rest_databaseReference, Reg.class).build();
        hotel_firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Reg, RP1>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull RP1 holder1, int position, @NonNull final Reg model1) {
                holder1.shopname.setText(model1.get_Customer_Name());

                FirebaseRecyclerOptions<Book> options2 = new FirebaseRecyclerOptions.Builder<Book>().setQuery(customer_booked_time_database.child(model1.get_Customer_Mail_Id()), Book.class).build();
                payment_firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Book, RP2>(options2) {
                    @Override
                    protected void onBindViewHolder(@NonNull RP2 holder2, final int position, @NonNull final Book model) {
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
                        else if(model.get_Amount_Status().equals("Refund")){
                            holder2.amount.setText(model.get_Amount_Paid());
                            holder2.status.setText(model.get_Amount_Status());
                            holder2.charged.setText(model.get_Charged());
                            holder2.refunded.setText(model.get_Refunded());
                            holder2.request_date.setText(model.get_Request_Date());
                            holder2.request_time.setText(model.get_Request_Time());
                            holder2.return_date.setText(model.get_Return_Date());
                            holder2.return_time.setText(model.get_Return_Time());
                            holder2.card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    book_root=model.get_Current_Date_at_the_Time_of_Booking_Food()+"+"+model.get_Current_Time_at_the_Time_of_Booking_Food()+"+"+model.get_Booked_Date()+"+"+model.get_Booked_Time();
                                    refund_amount=model.get_Refunded();
                                    rest_payment_amount.setText(model.get_Refunded());
                                    food_mail=model1.get_Customer_Mail_Id();
                                    upi_databaseReference2=FirebaseDatabase.getInstance().getReference().child("Customer Registration Details").child(food_mail);
                                    upi_databaseReference2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            cus_upi=snapshot.child("_Customer_UPI").getValue().toString();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    rest_pay_card1.setVisibility(View.INVISIBLE);
                                    bottomNavigationView.setVisibility(View.INVISIBLE);
                                    rest_constraint.setVisibility(View.VISIBLE);
                                    key=getRef(position).getKey();
                                }
                            });
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
                    public RP2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_payment_status_inside_card, parent, false);
                        return new RP2(view2);
                    }
                };
                payment_firebaseRecyclerAdapter.startListening();
                payment_firebaseRecyclerAdapter.notifyDataSetChanged();
                holder1.payment_recyclerView.setAdapter(payment_firebaseRecyclerAdapter);
            }

            @NonNull
            @Override
            public RP1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.rest_payment_status_card, parent, false);
                return new RP1(view2);
            }
        };
        hotel_firebaseRecyclerAdapter.startListening();
        hotel_firebaseRecyclerAdapter.notifyDataSetChanged();
        rest_payment_details_recyclerview.setAdapter(hotel_firebaseRecyclerAdapter);
    }
    private boolean isUpiChecked() {
        if(!upi_Id.equals(rest_upi_id.getEditableText().toString().trim())){
            upi_databaseReference1.child(key).child("_Restaurant_UPI").setValue(rest_upi_id.getEditableText().toString().trim());
            return true;
        }
        else{
            return false;
        }
    }
    public static class RP1 extends RecyclerView.ViewHolder {
        public TextView shopname;
        public RecyclerView payment_recyclerView;
        public RecyclerView.LayoutManager manager;
        public RP1(@NonNull View itemView) {
            super(itemView);
            payment_recyclerView=itemView.findViewById(R.id.rest_payment_status_card_recyclerview);
            shopname=itemView.findViewById(R.id.rest_payment_status_card_cus_name);
            manager=new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
            payment_recyclerView.setLayoutManager(manager);
        }
    }
    public static class RP2 extends RecyclerView.ViewHolder {
        public TextView amount,status,charged,refunded,request_date,request_time,return_date,return_time;
        public CardView card;
        public RP2(@NonNull View itemView) {
            super(itemView);
            amount      =itemView.findViewById(R.id.rest_payment_status_inside_card_amount);
            status      =itemView.findViewById(R.id.rest_payment_status_inside_card_status);
            charged     =itemView.findViewById(R.id.rest_payment_status_inside_card_charged);
            refunded    =itemView.findViewById(R.id.rest_payment_status_inside_card_refunded);
            request_date=itemView.findViewById(R.id.rest_payment_status_inside_card_request_date);
            request_time=itemView.findViewById(R.id.rest_payment_status_inside_card_request_time);
            return_date =itemView.findViewById(R.id.rest_payment_status_inside_card_return_date);
            return_time =itemView.findViewById(R.id.rest_payment_status_inside_card_return_time);
            card =itemView.findViewById(R.id.rest_payment_status_inside_card_card);
        }
    }
    public void rest_payment_back_press(View view){
        if(rest_constraint.getVisibility()==View.VISIBLE){
            AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant_Payment.this);
            builder.setTitle("Are you really going back?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    rest_pay_card1.setVisibility(View.VISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                    rest_constraint.setVisibility(View.INVISIBLE);
                                }
                            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant_Payment.this);
            builder.setTitle("Really Logout?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (true) {
                                        Paper.book().destroy();
                                        Intent intent = new Intent(Restaurant_Payment.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(Restaurant_Payment.this, MainActivity.class);
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
        if(rest_constraint.getVisibility()==View.VISIBLE){
            AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant_Payment.this);
            builder.setTitle("Are you really going back?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    rest_pay_card1.setVisibility(View.VISIBLE);
                                    bottomNavigationView.setVisibility(View.VISIBLE);
                                    rest_constraint.setVisibility(View.INVISIBLE);
                                }
                            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Restaurant_Payment.this);
            builder.setTitle("Really Logout?").setMessage("Are you sure?")
                    .setPositiveButton(
                            "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (true) {
                                        Paper.book().destroy();
                                        Intent intent = new Intent(Restaurant_Payment.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(Restaurant_Payment.this, MainActivity.class);
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

    public void rest_payment(View view){

        String amount1,supi,rupi,name;
        if(isUpiChecked()){
            amount1 = refund_amount;
            supi = rest_upi_id.getEditableText().toString().trim();
            name = res_name;
            rupi = cus_upi;
            payUsingUpi(amount1, rupi, name, supi);
        }
        else {
            amount1 = refund_amount;
            supi = rest_upi_id.getEditableText().toString().trim();
            name = res_name;
            rupi = cus_upi;
            payUsingUpi(amount1, rupi, name, supi);
        }
       //
    }
    public void payment_success(){
        current_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        current_time = new SimpleDateFormat("kk:mm:ss", Locale.getDefault()).format(new Date());
        update1=FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(main_root[0]).child(food_mail).child(book_root);
        update2=FirebaseDatabase.getInstance().getReference().child("Seat Booked Timings").child(food_mail).child(main_root[0]).child(book_root);
        amount_status="Refunded";
        update1.child("_Amount_Status").setValue(amount_status);
        update1.child("_Return_Date").setValue(current_date);
        update1.child("_Return_Time").setValue(current_time);
        update2.child("_Amount_Status").setValue(amount_status);
        update2.child("_Return_Date").setValue(current_date);
        update2.child("_Return_Time").setValue(current_time);
        AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant_Payment.this);
        builder.setTitle("Refunded Successfully!").setMessage("")
                .setPositiveButton(
                        "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                rest_pay_card1.setVisibility(View.VISIBLE);
                                bottomNavigationView.setVisibility(View.VISIBLE);
                                rest_constraint.setVisibility(View.INVISIBLE);
                            }
                        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
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
            Toasty.error(Restaurant_Payment.this,"No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
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
        if (isConnectionAvailable(Restaurant_Payment.this)) {
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
                Toasty.success(Restaurant_Payment.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
                payment_success();

            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toasty.info(Restaurant_Payment.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toasty.warning(Restaurant_Payment.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toasty.warning(Restaurant_Payment.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Restaurant_Payment context) {
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
