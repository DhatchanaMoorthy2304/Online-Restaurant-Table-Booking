package com.restaurent.restaurent_table_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class Customer_Register extends AppCompatActivity {

    EditText cus_reg_cus_name,cus_reg_cus_doorno,cus_reg_cus_street,
            cus_reg_cus_village,cus_reg_cus_city,cus_reg_cus_district,
            cus_reg_cus_state,cus_reg_cus_pincode,cus_reg_cus_upi,cus_reg_cus_email,cus_reg_login_password;

    String cus_name,cus_add,cus_upi,cus_email,cus_psw,cus_phone;

    DatabaseReference databaseReference;

    int i=1,j=1,k=1,l=1;
    String verificationCodeSys,add,vphone;
    FirebaseAuth firebaseAuth;
    EditText phone_Number,otp_Code;
    TextInputLayout cus_reg_vis;
    Button nextBtn,reg,verify;
    ProgressBar progressBar;
    ProgressDialog progressDialog1,progressDialog2,progressDialog3;
    TextView state;
    CountryCodePicker ccp;
    CardView cardView;
Boolean verificationInProgress=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);
        FirebaseApp.initializeApp(this);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Customer Registration Details");

        cus_reg_cus_name        =findViewById(R.id.cus_reg_cus_name);
        cus_reg_cus_doorno      =findViewById(R.id.cus_reg_cus_doorno);
        cus_reg_cus_street      =findViewById(R.id.cus_reg_cus_street);
        cus_reg_cus_village     =findViewById(R.id.cus_reg_cus_village);
        cus_reg_cus_city        =findViewById(R.id.cus_reg_cus_city);
        cus_reg_cus_district    =findViewById(R.id.cus_reg_cus_district);
        cus_reg_cus_state       =findViewById(R.id.cus_reg_cus_state);
        cus_reg_cus_pincode     =findViewById(R.id.cus_reg_cus_pincode);
        cus_reg_cus_upi         =findViewById(R.id.cus_reg_cus_upi);
        cus_reg_cus_email       =findViewById(R.id.cus_reg_cus_email);
        cus_reg_login_password  =findViewById(R.id.cus_reg_login_password);

        firebaseAuth=FirebaseAuth.getInstance();
        phone_Number=findViewById(R.id.cus_reg_cus_phone);
        otp_Code    =findViewById(R.id.cus_reg_otp);
        progressBar =findViewById(R.id.cus_reg_otp_progressBar);
        nextBtn     =findViewById(R.id.cus_reg_next);
        state       =findViewById(R.id.cus_reg_states);
        reg         =findViewById(R.id.cus_reg_reg);
        cus_reg_vis =findViewById(R.id.cus_reg_vis);
        ccp         =findViewById(R.id.cus_reg_ccp);
        verify      =findViewById(R.id.cus_reg_ver);
        cardView    =findViewById(R.id.cus_reg_cardview);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatePhone()|!validateCusName()|!validateCusDoor()|!validateCusStreet()|!validateCusVillage()
                    |!validateCusCity()|!validateCusDistrict()|!validateCusState()|!validateCusPincode()
                    |!validateCusUPI()|!validateCusEmail()|!validateCusPsw()){
                    return ;
                }
                if(!phone_Number.getText().toString().trim().isEmpty()&&phone_Number.getText().toString().length()==10){
                    progressDialog3=new ProgressDialog(Customer_Register.this);
                    progressDialog3.setTitle("Please Wait...");
                    progressDialog3.setMessage("Verifying the Email,Phone,Address,UPI...");
                    progressDialog3.setCanceledOnTouchOutside(false);
                    progressDialog3.show();
                    vphone="+"+ccp.getSelectedCountryCode()+"-"+phone_Number.getText().toString().trim();
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
                                            add=cus_reg_cus_doorno.getText().toString().trim()+", "+cus_reg_cus_street.getText().toString().trim()+", "+
                                                    cus_reg_cus_village.getText().toString().trim()+", "+cus_reg_cus_city.getText().toString().trim()+", "+
                                                    cus_reg_cus_district.getText().toString().trim()+", "+cus_reg_cus_state.getText().toString().trim()+", "+
                                                    cus_reg_cus_pincode.getText().toString().trim();
                                            DatabaseReference databaseReference3=FirebaseDatabase.getInstance().getReference().child("Customer Registration Details");
                                            databaseReference3.orderByChild("_Customer_Address").equalTo(add).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()){
                                                        progressDialog3.dismiss();
                                                        cus_reg_cus_doorno.requestFocus();
                                                        cus_reg_cus_doorno.setError("Address already Exists");
                                                        cus_reg_cus_street.setError("Address already Exists");
                                                        cus_reg_cus_village.setError("Address already Exists");
                                                        cus_reg_cus_city.setError("Address already Exists");
                                                        cus_reg_cus_district.setError("Address already Exists");
                                                        cus_reg_cus_state.setError("Address already Exists");
                                                        cus_reg_cus_pincode.setError("Address already Exists");
                                                        cus_reg_cus_doorno.setText("");
                                                        cus_reg_cus_street.setText("");
                                                        cus_reg_cus_village.setText("");
                                                        cus_reg_cus_city.setText("");
                                                        cus_reg_cus_district.setText("");
                                                        cus_reg_cus_state.setText("");
                                                        cus_reg_cus_pincode.setText("");
                                                    }
                                                    else{
                                                        DatabaseReference databaseReference4=FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details");
                                                        databaseReference4.orderByChild("_Restaurant_Address").equalTo(add).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if(dataSnapshot.exists()){
                                                                    progressDialog3.dismiss();
                                                                    cus_reg_cus_doorno.requestFocus();
                                                                    cus_reg_cus_doorno.setError("Address already Exists");
                                                                    cus_reg_cus_street.setError("Address already Exists");
                                                                    cus_reg_cus_village.setError("Address already Exists");
                                                                    cus_reg_cus_city.setError("Address already Exists");
                                                                    cus_reg_cus_district.setError("Address already Exists");
                                                                    cus_reg_cus_state.setError("Address already Exists");
                                                                    cus_reg_cus_pincode.setError("Address already Exists");
                                                                    cus_reg_cus_doorno.setText("");
                                                                    cus_reg_cus_street.setText("");
                                                                    cus_reg_cus_village.setText("");
                                                                    cus_reg_cus_city.setText("");
                                                                    cus_reg_cus_district.setText("");
                                                                    cus_reg_cus_state.setText("");
                                                                    cus_reg_cus_pincode.setText("");
                                                                }
                                                                else {
                                                                    DatabaseReference databaseReference5=FirebaseDatabase.getInstance().getReference().child("Customer Registration Details");
                                                                    databaseReference5.orderByChild("_Customer_UPI").equalTo(cus_reg_cus_upi.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            if(dataSnapshot.exists()){
                                                                                progressDialog3.dismiss();
                                                                                cus_reg_cus_upi.requestFocus();
                                                                                cus_reg_cus_upi.setError("UPI already Exists");
                                                                                cus_reg_cus_upi.setText("");
                                                                            }
                                                                            else{
                                                                                DatabaseReference databaseReference6=FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details");
                                                                                databaseReference6.orderByChild("_Restaurant_UPI").equalTo(cus_reg_cus_upi.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                        if(dataSnapshot.exists()){
                                                                                            progressDialog3.dismiss();
                                                                                            cus_reg_cus_upi.requestFocus();
                                                                                            cus_reg_cus_upi.setError("UPI already Exists");
                                                                                            cus_reg_cus_upi.setText("");
                                                                                        }
                                                                                        else{
                                                                                            DatabaseReference databaseReference7=FirebaseDatabase.getInstance().getReference().child("Customer Registration Details");
                                                                                            databaseReference7.orderByChild("_Customer_Mail_Id").equalTo(cus_reg_cus_email.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                    if(dataSnapshot.exists()){
                                                                                                        progressDialog3.dismiss();
                                                                                                        cus_reg_cus_email.requestFocus();
                                                                                                        cus_reg_cus_email.setError("Email already Exists");
                                                                                                        cus_reg_cus_email.setText("");
                                                                                                    }
                                                                                                    else{
                                                                                                        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference().child("Restaurant Registration Details");
                                                                                                        databaseReference2.orderByChild("_Restaurant_Mail_Id").equalTo(cus_reg_cus_email.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                if(dataSnapshot.exists()){
                                                                                                                    progressDialog3.dismiss();
                                                                                                                    cus_reg_cus_email.requestFocus();
                                                                                                                    cus_reg_cus_email.setError("Email already Exists");
                                                                                                                    cus_reg_cus_email.setText("");
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
                if(!validateOTP())
                    return;
                String code=otp_Code.getText().toString().trim();
                if(code.isEmpty()||code.length()<6){
                    otp_Code.setError("Wrong OTP");
                    otp_Code.requestFocus();
                }
                else {
                    progressDialog1=new ProgressDialog(Customer_Register.this);
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
                progressDialog2=new ProgressDialog(Customer_Register.this);
                progressDialog2.setTitle("Please Wait...");
                progressDialog2.setMessage("Account is Creating...");
                progressDialog2.setCanceledOnTouchOutside(false);
                progressDialog2.show();
                cus_name=cus_reg_cus_name.getText().toString().trim();
                cus_add=cus_reg_cus_doorno.getText().toString().trim()+", "+cus_reg_cus_street.getText().toString().trim()+", "+
                        cus_reg_cus_village.getText().toString().trim()+", "+cus_reg_cus_city.getText().toString().trim()+", "+
                        cus_reg_cus_district.getText().toString().trim()+", "+cus_reg_cus_state.getText().toString().trim()+", "+
                        cus_reg_cus_pincode.getText().toString().trim();
                cus_upi=cus_reg_cus_upi.getText().toString().trim();
                cus_email=cus_reg_cus_email.getText().toString().trim();
                cus_psw=cus_reg_login_password.getText().toString().trim();
                cus_phone=phone_Number.getText().toString().trim();
                FirebaseAuth firebaseAuth1=FirebaseAuth.getInstance();
                firebaseAuth1.createUserWithEmailAndPassword(cus_email,cus_psw).addOnCompleteListener(Customer_Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Reg reg=new Reg(cus_name,cus_add,cus_upi,cus_email,vphone);
                            String[] k=cus_email.split("\\.");
                            databaseReference.child(k[0]).setValue(reg);
                            AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Register.this);
                            builder.setTitle("Registered Successfully").setMessage("From now on, you are the new customer!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Customer_Register.this,MainActivity.class));
                                    progressDialog2.dismiss();
                                    finish();
                                }
                            });
                            AlertDialog alertDialog=builder.create();
                            alertDialog.show();
                        }
                        else {
                            Toasty.success(Customer_Register.this,task.getException().getMessage(),Toasty.LENGTH_SHORT).show();
                            progressDialog2.dismiss();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Register.this);
        builder.setTitle("You will not be able to create an account!").setMessage("if you want to go back Click 'Yes' Else Click 'No'")
                .setPositiveButton(
                        "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Customer_Register.this,MainActivity.class));
                                finish();
                            }
                        }
                ).setNegativeButton("Cancel",null).setCancelable(false);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progressBar.setVisibility(View.GONE);
            state.setVisibility(View.INVISIBLE);
            nextBtn.setVisibility(View.INVISIBLE);
            verificationCodeSys=s;
            cus_reg_vis.setVisibility(View.VISIBLE);
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
            Toasty.error(Customer_Register.this,e.getMessage(),Toasty.LENGTH_SHORT).show();
        }
    };

    private void verifycode(String codeByUser){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCodeSys,codeByUser);
        signInTheUserByCredentials(credential);
    }
    private void signInTheUserByCredentials(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(Customer_Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Toasty.success(Customer_Register.this,"Success",Toasty.LENGTH_SHORT).show();
                   progressDialog1.dismiss();
                   verify.setVisibility(View.INVISIBLE);
                   reg.setVisibility(View.VISIBLE);
               }
               else {
                   Toasty.success(Customer_Register.this,task.getException().getMessage(),Toasty.LENGTH_SHORT).show();
               }
            }
        });
    }


    private Boolean validateOTP() {
        String val = otp_Code.getText().toString().trim();
        if (val.isEmpty()) {
            otp_Code.setError("Fields cannot be empty");
            return false;
        } else {
            otp_Code.setError(null);
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
    private Boolean validateCusName() {
        String val = cus_reg_cus_name.getText().toString().trim();
        if (val.isEmpty()) {
            cus_reg_cus_name.setError("Fields cannot be empty");
            return false;
        } else {
            cus_reg_cus_name.setError(null);
            return true;
        }
    }
    private Boolean validateCusDoor() {
        String val = cus_reg_cus_doorno.getText().toString().trim();
        if (val.isEmpty()) {
            cus_reg_cus_doorno.setError("Fields cannot be empty");
            return false;
        } else {
            cus_reg_cus_doorno.setError(null);
            return true;
        }
    }
    private Boolean validateCusStreet() {
        String val = cus_reg_cus_street.getText().toString().trim();
        if (val.isEmpty()) {
            cus_reg_cus_street.setError("Fields cannot be empty");
            return false;
        } else {
            cus_reg_cus_street.setError(null);
            return true;
        }
    }
    private Boolean validateCusVillage() {
        String val = cus_reg_cus_village.getText().toString().trim();
        if (val.isEmpty()) {
            cus_reg_cus_village.setError("Fields cannot be empty");
            return false;
        } else {
            cus_reg_cus_village.setError(null);
            return true;
        }
    }
    private Boolean validateCusCity() {
        String val = cus_reg_cus_city.getText().toString().trim();
        if (val.isEmpty()) {
            cus_reg_cus_city.setError("Fields cannot be empty");
            return false;
        } else {
            cus_reg_cus_city.setError(null);
            return true;
        }
    }
    private Boolean validateCusDistrict() {
        String val = cus_reg_cus_district.getText().toString().trim();
        if (val.isEmpty()) {
            cus_reg_cus_district.setError("Fields cannot be empty");
            return false;
        } else {
            cus_reg_cus_district.setError(null);
            return true;
        }
    }
    private Boolean validateCusState() {
        String val = cus_reg_cus_state.getText().toString().trim();
        if (val.isEmpty()) {
            cus_reg_cus_state.setError("Fields cannot be empty");
            return false;
        } else {
            cus_reg_cus_state.setError(null);
            return true;
        }
    }
    private Boolean validateCusPincode() {
        String val = cus_reg_cus_pincode.getText().toString().trim();
        if (val.isEmpty()) {
            cus_reg_cus_pincode.setError("Fields cannot be empty");
            return false;
        } else {
            cus_reg_cus_pincode.setError(null);
            return true;
        }
    }
    private Boolean validateCusUPI() {
        String val = cus_reg_cus_upi.getText().toString().trim();
        if (val.isEmpty()) {
            cus_reg_cus_upi.setError("Fields cannot be empty");
            return false;
        } else {
            cus_reg_cus_upi.setError(null);
            return true;
        }
    }
    private Boolean validateCusEmail() {
        String val = cus_reg_cus_email.getText().toString().trim();
        if (val.isEmpty()) {
            cus_reg_cus_email.setError("Fields cannot be empty");
            return false;
        } else {
            cus_reg_cus_email.setError(null);
            return true;
        }
    }
    private Boolean validateCusPsw() {
        String val = cus_reg_login_password.getText().toString().trim();
        if (val.isEmpty()) {
            cus_reg_login_password.setError("Fields cannot be empty");
            return false;
        } else {
            cus_reg_login_password.setError(null);
            return true;
        }
    }
    private void sendOTPtoUser(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                Customer_Register.this,
                mcallbacks
        );
    }
}