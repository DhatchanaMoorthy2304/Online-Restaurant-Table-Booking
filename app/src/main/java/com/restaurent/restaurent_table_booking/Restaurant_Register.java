package com.restaurent.restaurent_table_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class Restaurant_Register extends AppCompatActivity {

    EditText rest_reg_rest_name,rest_reg_rest_owner,rest_reg_rest_seats,rest_reg_rest_doorno,rest_reg_rest_street,
            rest_reg_rest_village,rest_reg_rest_city,rest_reg_rest_district,
            rest_reg_rest_state,rest_reg_rest_pincode,rest_reg_rest_upi,rest_reg_rest_email,rest_reg_login_password;
    String rest_name,rest_own,rest_add,rest_upi,rest_email,rest_psw,rest_phone,rest_seats;
    DatabaseReference databaseReference;

    String hotel_category="Select Category",add;
    ArrayList<String> hotel_cat;
    Spinner hotel_spinner;
    CountryCodePicker rest_cpp;

    int i=1,j=1,k=1,l=1;
    String verificationCodeSys,vphone;
    FirebaseAuth firebaseAuth;
    EditText phone_Number,otp_Code;
    TextInputLayout rest_reg_vis;
    Button nextBtn,reg,verify;
    ProgressBar progressBar;
    ProgressDialog progressDialog1,progressDialog2;
    TextView state;
    CountryCodePicker ccp;
    CardView cardView;
    ProgressDialog progressDialog3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_register);
        FirebaseApp.initializeApp(this);
        hotel_cat=new ArrayList<>();
        hotel_cat.add("Select Category");
        hotel_cat.add("Vegetarian");
        hotel_cat.add("Non-Vegetarian");
        hotel_cat.add("Both");
        hotel_spinner=findViewById(R.id.rest_reg_hotel_spinner);
        rest_cpp=findViewById(R.id.rest_reg_ccp);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details");

        rest_reg_rest_name        =findViewById(R.id.rest_reg_rest_name);
        rest_reg_rest_owner       =findViewById(R.id.rest_reg_rest_owner);
        rest_reg_rest_seats       =findViewById(R.id.rest_reg_rest_seats);
        rest_reg_rest_doorno      =findViewById(R.id.rest_reg_rest_doorno);
        rest_reg_rest_street      =findViewById(R.id.rest_reg_rest_street);
        rest_reg_rest_village     =findViewById(R.id.rest_reg_rest_village);
        rest_reg_rest_city        =findViewById(R.id.rest_reg_rest_city);
        rest_reg_rest_district    =findViewById(R.id.rest_reg_rest_district);
        rest_reg_rest_state       =findViewById(R.id.rest_reg_rest_state);
        rest_reg_rest_pincode     =findViewById(R.id.rest_reg_rest_pincode);
        rest_reg_rest_upi         =findViewById(R.id.rest_reg_rest_upi);
        rest_reg_rest_email       =findViewById(R.id.rest_reg_rest_email);
        rest_reg_login_password  =findViewById(R.id.rest_reg_login_password);

        firebaseAuth=FirebaseAuth.getInstance();
        phone_Number=findViewById(R.id.rest_reg_rest_phone);
        otp_Code    =findViewById(R.id.rest_reg_rest_otp);
        progressBar =findViewById(R.id.rest_reg_otp_progressBar);
        nextBtn     =findViewById(R.id.rest_reg_next);
        state       =findViewById(R.id.rest_reg_states);
        reg         =findViewById(R.id.rest_reg_reg);
        rest_reg_vis=findViewById(R.id.rest_reg_vis);
        verify      =findViewById(R.id.rest_reg_ver);
        cardView    =findViewById(R.id.rest_reg_cardview);

        hotel_spinner.setAdapter(new ArrayAdapter<>(Restaurant_Register.this,android.R.layout.simple_spinner_dropdown_item,hotel_cat));
        hotel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Toasty.success(getApplicationContext(),"Select Category",Toasty.LENGTH_SHORT).show();

                }
                else
                {
                    hotel_category=parent.getItemAtPosition(position).toString();
                    Toasty.success(getApplicationContext(),hotel_category,Toasty.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatePhone()|!validateRestName()|!validateRestDoor()|!validateRestStreet()|!validateRestVillage()
                        |!validateRestCity()|!validateRestDistrict()|!validateRestState()|!validateRestPincode()
                        |!validateRestUPI()|!validateRestEmail()|!validateRestPsw()||!validateSeats()|!validateCategory()|!validateRestOwnerName()){
                    return ;
                }
                if(!phone_Number.getText().toString().trim().isEmpty()&&phone_Number.getText().toString().length()==10){
                    progressDialog3=new ProgressDialog(Restaurant_Register.this);
                    progressDialog3.setTitle("Please Wait...");
                    progressDialog3.setMessage("Verifying the Email,Phone,Address,UPI...");
                    progressDialog3.setCanceledOnTouchOutside(false);
                    progressDialog3.show();
                    vphone="+"+rest_cpp.getSelectedCountryCode()+"-"+phone_Number.getText().toString().trim();
                    DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference().child("Customer Registration Details");
                    databaseReference1.orderByChild("_Customer_Phone").equalTo(vphone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                progressDialog3.dismiss();
                                phone_Number.requestFocus();
                                phone_Number.setError("Phone already Exists");
                                phone_Number.setText("");
                            }
                            else{
                                DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details");
                                databaseReference2.orderByChild("_Restaurant_Phone").equalTo(vphone).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            progressDialog3.dismiss();
                                            phone_Number.requestFocus();
                                            phone_Number.setError("Phone already Exists");
                                            phone_Number.setText("");
                                        }
                                        else{
                                            add=rest_reg_rest_doorno.getText().toString().trim()+", "+rest_reg_rest_street.getText().toString().trim()+", "+
                                                    rest_reg_rest_village.getText().toString().trim()+", "+rest_reg_rest_city.getText().toString().trim()+", "+
                                                    rest_reg_rest_district.getText().toString().trim()+", "+rest_reg_rest_state.getText().toString().trim()+", "+
                                                    rest_reg_rest_pincode.getText().toString().trim();
                                            DatabaseReference databaseReference3=FirebaseDatabase.getInstance().getReference().child("Customer Registration Details");
                                            databaseReference3.orderByChild("_Customer_Address").equalTo(add).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()){
                                                        progressDialog3.dismiss();
                                                        rest_reg_rest_doorno.requestFocus();
                                                        rest_reg_rest_doorno.setError("Address already Exists");
                                                        rest_reg_rest_street.setError("Address already Exists");
                                                        rest_reg_rest_village.setError("Address already Exists");
                                                        rest_reg_rest_city.setError("Address already Exists");
                                                        rest_reg_rest_district.setError("Address already Exists");
                                                        rest_reg_rest_state.setError("Address already Exists");
                                                        rest_reg_rest_pincode.setError("Address already Exists");
                                                        rest_reg_rest_doorno.setText("");
                                                        rest_reg_rest_street.setText("");
                                                        rest_reg_rest_village.setText("");
                                                        rest_reg_rest_city.setText("");
                                                        rest_reg_rest_district.setText("");
                                                        rest_reg_rest_state.setText("");
                                                        rest_reg_rest_pincode.setText("");
                                                    }
                                                    else{
                                                        DatabaseReference databaseReference4=FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details");
                                                        databaseReference4.orderByChild("_Restaurant_Address").equalTo(add).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if(dataSnapshot.exists()){
                                                                    progressDialog3.dismiss();
                                                                    rest_reg_rest_doorno.requestFocus();
                                                                    rest_reg_rest_doorno.setError("Address already Exists");
                                                                    rest_reg_rest_street.setError("Address already Exists");
                                                                    rest_reg_rest_village.setError("Address already Exists");
                                                                    rest_reg_rest_city.setError("Address already Exists");
                                                                    rest_reg_rest_district.setError("Address already Exists");
                                                                    rest_reg_rest_state.setError("Address already Exists");
                                                                    rest_reg_rest_pincode.setError("Address already Exists");
                                                                    rest_reg_rest_doorno.setText("");
                                                                    rest_reg_rest_street.setText("");
                                                                    rest_reg_rest_village.setText("");
                                                                    rest_reg_rest_city.setText("");
                                                                    rest_reg_rest_district.setText("");
                                                                    rest_reg_rest_state.setText("");
                                                                    rest_reg_rest_pincode.setText("");
                                                                }
                                                                else {
                                                                    DatabaseReference databaseReference5=FirebaseDatabase.getInstance().getReference().child("Customer Registration Details");
                                                                    databaseReference5.orderByChild("_Customer_UPI").equalTo(rest_reg_rest_upi.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            if(dataSnapshot.exists()){
                                                                                progressDialog3.dismiss();
                                                                                rest_reg_rest_upi.requestFocus();
                                                                                rest_reg_rest_upi.setError("UPI already Exists");
                                                                                rest_reg_rest_upi.setText("");
                                                                            }
                                                                            else{
                                                                                DatabaseReference databaseReference6=FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details");
                                                                                databaseReference6.orderByChild("_Restaurant_UPI").equalTo(rest_reg_rest_upi.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                        if(dataSnapshot.exists()){
                                                                                            progressDialog3.dismiss();
                                                                                            rest_reg_rest_upi.requestFocus();
                                                                                            rest_reg_rest_upi.setError("UPI already Exists");
                                                                                            rest_reg_rest_upi.setText("");
                                                                                        }
                                                                                        else{
                                                                                            DatabaseReference databaseReference7=FirebaseDatabase.getInstance().getReference().child("Customer Registration Details");
                                                                                            databaseReference7.orderByChild("_Customer_Mail_Id").equalTo(rest_reg_rest_email.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                    if(dataSnapshot.exists()){
                                                                                                        progressDialog3.dismiss();
                                                                                                        rest_reg_rest_email.requestFocus();
                                                                                                        rest_reg_rest_email.setError("Email already Exists");
                                                                                                        rest_reg_rest_email.setText("");
                                                                                                    }
                                                                                                    else{
                                                                                                        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details");
                                                                                                        databaseReference2.orderByChild("_Restaurant_Mail_Id").equalTo(rest_reg_rest_email.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                if(dataSnapshot.exists()){
                                                                                                                    progressDialog3.dismiss();
                                                                                                                    rest_reg_rest_email.requestFocus();
                                                                                                                    rest_reg_rest_email.setError("Email already Exists");
                                                                                                                    rest_reg_rest_email.setText("");
                                                                                                                }
                                                                                                                else{
                                                                                                                    progressDialog3.dismiss();
                                                                                                                    cardView.setVisibility(View.VISIBLE);
                                                                                                                    progressBar.setVisibility(View.VISIBLE);
                                                                                                                    state.setText("Sending OTP...");
                                                                                                                    state.setVisibility(View.VISIBLE);
                                                                                                                    sendOTPtoUser(vphone);
                                                                                                                }
                                                                                                            }
                                                                                                            @Override
                                                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                }
                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    }
                                                                                    @Override
                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                            }
                                                        });
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    phone_Number.setError("Phone Number is Not Valid");
                }

            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatePhone())
                    return;
                String code=otp_Code.getText().toString().trim();
                if(code.isEmpty()||code.length()<6){
                    otp_Code.setError("Wrong OTP");
                    otp_Code.requestFocus();
                }
                else {
                    progressDialog1=new ProgressDialog(Restaurant_Register.this);
                    progressDialog1.setTitle("Please Wait...");
                    progressDialog1.setMessage("OTP is Verifying...");
                    progressDialog1.setCanceledOnTouchOutside(false);
                    progressDialog1.show();
                    verifycode(code);
                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog2=new ProgressDialog(Restaurant_Register.this);
                progressDialog2.setTitle("Please Wait...");
                progressDialog2.setMessage("Account is Creating...");
                progressDialog2.setCanceledOnTouchOutside(false);
                progressDialog2.show();
                rest_name=rest_reg_rest_name.getText().toString().trim();
                rest_own=rest_reg_rest_owner.getText().toString().trim();
                rest_add=rest_reg_rest_doorno.getText().toString().trim()+", "+rest_reg_rest_street.getText().toString().trim()+", "+
                        rest_reg_rest_village.getText().toString().trim()+", "+rest_reg_rest_city.getText().toString().trim()+", "+
                        rest_reg_rest_district.getText().toString().trim()+", "+rest_reg_rest_state.getText().toString().trim()+", "+
                        rest_reg_rest_pincode.getText().toString().trim();
                rest_upi=rest_reg_rest_upi.getText().toString().trim();
                rest_email=rest_reg_rest_email.getText().toString().trim();
                rest_psw=rest_reg_login_password.getText().toString().trim();
                rest_phone=phone_Number.getText().toString().trim();
                rest_seats=rest_reg_rest_seats.getText().toString().trim();
                FirebaseAuth firebaseAuth1=FirebaseAuth.getInstance();
                firebaseAuth1.createUserWithEmailAndPassword(rest_email,rest_psw).addOnCompleteListener(Restaurant_Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Reg reg=new Reg(rest_name,rest_own,rest_seats,rest_seats,hotel_category,rest_add,rest_upi,rest_email,vphone);
                            String[] k=rest_email.split("\\.");
                            databaseReference.child(k[0]).setValue(reg);
                            AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant_Register.this);
                            builder.setTitle("Registered Successfully").setMessage("From now on, you are the new customer!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Restaurant_Register.this,MainActivity.class));
                                    progressDialog2.dismiss();
                                    finish();
                                }
                            });
                            AlertDialog alertDialog=builder.create();
                            alertDialog.show();
                        }
                        else {
                            Toasty.success(Restaurant_Register.this,task.getException().getMessage(),Toasty.LENGTH_SHORT).show();
                            progressDialog2.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(Restaurant_Register.this);
        builder.setTitle("You will not be able to create an account!").setMessage("if you want to go back Click 'Yes' Else Click 'No'")
                .setPositiveButton(
                        "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Restaurant_Register.this,MainActivity.class));
                                finish();
                            }
                        }
                ).setNegativeButton("Cancel",null).setCancelable(false);
        androidx.appcompat.app.AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void sendOTPtoUser(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                Restaurant_Register.this,
                mcallbacks
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progressBar.setVisibility(View.GONE);
            state.setVisibility(View.INVISIBLE);
            nextBtn.setVisibility(View.INVISIBLE);
            verificationCodeSys=s;
            rest_reg_vis.setVisibility(View.VISIBLE);
            verify.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code==null){
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toasty.error(Restaurant_Register.this,e.getMessage(),Toasty.LENGTH_SHORT).show();
        }
    };

    private void verifycode(String codeByUser){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCodeSys,codeByUser);
        signInTheUserByCredentials(credential);
    }
    private void signInTheUserByCredentials(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(Restaurant_Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toasty.success(Restaurant_Register.this,"Success",Toasty.LENGTH_SHORT).show();
                    progressDialog1.dismiss();
                    verify.setVisibility(View.INVISIBLE);
                    reg.setVisibility(View.VISIBLE);
                }
                else {
                    Toasty.success(Restaurant_Register.this,task.getException().getMessage(),Toasty.LENGTH_SHORT).show();
                }
            }
        });
    }
    private Boolean validateCategory() {
        String val = rest_reg_rest_seats.getText().toString().trim();
        if (hotel_category.equals("Select Category")) {
            Toasty.error(getApplicationContext(),"Select Category",Toasty.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
    private Boolean validateSeats() {
        String val = rest_reg_rest_seats.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_seats.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_seats.setError(null);
            return true;
        }
    }
    private Boolean validatePhone() {
        String val = phone_Number.getText().toString().trim();
        if (val.isEmpty()) {
            phone_Number.setError("Fields cannot be empty");
            return false;
        } else {
            phone_Number.setError(null);
            return true;
        }
    }
    private Boolean validateRestName() {
        String val = rest_reg_rest_name.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_name.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_name.setError(null);
            return true;
        }
    }
    private Boolean validateRestDoor() {
        String val = rest_reg_rest_doorno.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_doorno.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_doorno.setError(null);
            return true;
        }
    }
    private Boolean validateRestStreet() {
        String val = rest_reg_rest_street.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_street.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_street.setError(null);
            return true;
        }
    }
    private Boolean validateRestVillage() {
        String val = rest_reg_rest_village.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_village.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_village.setError(null);
            return true;
        }
    }
    private Boolean validateRestCity() {
        String val = rest_reg_rest_city.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_city.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_city.setError(null);
            return true;
        }
    }
    private Boolean validateRestDistrict() {
        String val = rest_reg_rest_district.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_district.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_district.setError(null);
            return true;
        }
    }
    private Boolean validateRestState() {
        String val = rest_reg_rest_state.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_state.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_state.setError(null);
            return true;
        }
    }
    private Boolean validateRestPincode() {
        String val = rest_reg_rest_pincode.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_pincode.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_pincode.setError(null);
            return true;
        }
    }
    private Boolean validateRestUPI() {
        String val = rest_reg_rest_upi.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_upi.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_upi.setError(null);
            return true;
        }
    }
    private Boolean validateRestEmail() {
        String val = rest_reg_rest_email.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_email.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_email.setError(null);
            return true;
        }
    }
    private Boolean validateRestPsw() {
        String val = rest_reg_login_password.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_login_password.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_login_password.setError(null);
            return true;
        }
    }
    private Boolean validateRestOwnerName() {
        String val = rest_reg_rest_owner.getText().toString().trim();
        if (val.isEmpty()) {
            rest_reg_rest_owner.setError("Fields cannot be empty");
            return false;
        } else {
            rest_reg_rest_owner.setError(null);
            return true;
        }
    }
}
