package com.restaurent.restaurent_table_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

public class Customer_Login extends AppCompatActivity {

    FirebaseAuth firebaseAuth,firebaseAuth1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText cus_login_email,cus_login_password;
    String mail,psw;
    CheckBox cus_login_check;
    Button cus_login_login;
    TextView cus_login_forgot;
    ProgressDialog progressDialog,progressDialog1,progressDialog2,progressDialog3;
    FirebaseApp firebaseApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        firebaseApp=FirebaseApp.initializeApp(this);
        firebaseAuth=FirebaseAuth.getInstance(firebaseApp);
        firebaseAuth1=FirebaseAuth.getInstance(firebaseApp);
        firebaseDatabase= FirebaseDatabase.getInstance(firebaseApp);
        databaseReference= firebaseDatabase.getReference().child("Customer Registration Details");
        cus_login_email=findViewById(R.id.cus_login_email);
        cus_login_password=findViewById(R.id.cus_login_password);
        cus_login_check=findViewById(R.id.cus_login_check);
        cus_login_login=findViewById(R.id.cus_login_login);
        cus_login_forgot=findViewById(R.id.cus_login_forgot);
        progressDialog=new ProgressDialog(Customer_Login.this);
        progressDialog1=new ProgressDialog(Customer_Login.this);
        progressDialog2=new ProgressDialog(Customer_Login.this);
        progressDialog3=new ProgressDialog(Customer_Login.this);
        Paper.init(this);

        cus_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateCusEmail()|!validateCusPsw())
                    return;
                progressDialog.setTitle("Please Wait...");
                progressDialog.setMessage("Email is Verifying...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                mail=cus_login_email.getText().toString().trim();
                psw=cus_login_password.getText().toString().trim();
                databaseReference.orderByChild("_Customer_Mail_Id").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            progressDialog.dismiss();
                            progressDialog1.setTitle("Please Wait...");
                            progressDialog1.setMessage("Logging In...");
                            progressDialog1.setCanceledOnTouchOutside(false);
                            progressDialog1.show();
                            if(cus_login_check.isChecked()) {
                                Paper.book().write(Reg.CusUserEmailkey, mail);
                                Paper.book().write(Reg.CusUserPasswordkey, psw);
                            }
                            firebaseAuth.signInWithEmailAndPassword(mail, psw).addOnCompleteListener(Customer_Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        progressDialog1.dismiss();
                                        AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Login.this);
                                        builder.setTitle("Not Successful!").setMessage(task.getException().getMessage())
                                                .setNegativeButton("Ok",null).setCancelable(false);
                                        AlertDialog alertDialog=builder.create();
                                        alertDialog.show();
                                    } else {
                                        progressDialog1.dismiss();
                                        AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Login.this);
                                        builder.setTitle("Successful").setMessage("Successfully Logged into "+mail+" Id!")
                                                .setPositiveButton(
                                                        "Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                Intent intent=new Intent(getApplicationContext(),Customer_Home.class);
                                                                intent.putExtra("mail",cus_login_email.getText().toString());
                                                                startActivity(intent);
                                                                cus_login_email.setText("");
                                                                cus_login_password.setText("");
                                                                finish();
                                                            }
                                                        });
                                        AlertDialog alertDialog=builder.create();
                                        alertDialog.show();
                                    }
                                }
                            });

                        }
                        else{
                            progressDialog.dismiss();
                            AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Login.this);
                            builder.setTitle("Enter Correct Email id!").setMessage("This "+mail+"Email is not in the Customer Database!")
                                    .setNegativeButton("Ok",null).setCancelable(false);
                            AlertDialog alertDialog=builder.create();
                            alertDialog.show();
                            cus_login_email.setText("");
                            cus_login_password.setText("");
                            cus_login_email.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        cus_login_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateCusEmail())
                    return;
                mail=cus_login_email.getText().toString();
                progressDialog3.setTitle("Please Wait...");
                progressDialog3.setMessage("Forgot Password link Sending to"+mail+" Email...");
                progressDialog3.setCanceledOnTouchOutside(false);
                progressDialog3.show();
                databaseReference.orderByChild("_Customer_Mail_Id").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            firebaseAuth1.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog3.dismiss();
                                        AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Login.this);
                                        builder.setTitle("Successful!").setMessage("Forgot password link Sent to "+mail+" mail id. Click \"OK\" to open the Gmail app else Click \"Cancel\"")
                                                .setPositiveButton(
                                                        "Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                try{
                                                                    //When google map is installed.
                                                                    //Initialize Uri
                                                                    Uri uri=Uri.parse("https://mail.google.com");
                                                                    //Initialize intent with action view
                                                                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                                                                    //Set package
                                                                    intent.setPackage("com.google.android.apps.gm");
                                                                    //Set flag
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    //Start Activity
                                                                    startActivity(intent);

                                                                }catch (ActivityNotFoundException e){
                                                                    //When google map is not installed
                                                                    //Initialize Uri
                                                                    Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gm");
                                                                    //Initialize intent with action view
                                                                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                                                                    //Set flag
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    //Start Activity
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        }
                                                ).setNegativeButton("Cancel",null).setCancelable(false);
                                        AlertDialog alertDialog=builder.create();
                                        alertDialog.show();
                                    }
                                    else{
                                        progressDialog3.dismiss();
                                        AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Login.this);
                                        builder.setTitle("Not Successful!").setMessage(mail+" "+task.getException().getMessage())
                                                .setNegativeButton("Ok",null).setCancelable(false);
                                        AlertDialog alertDialog=builder.create();
                                        alertDialog.show();
                                        // Toasty.error(bsforgotpass.this,,Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            progressDialog3.dismiss();
                            AlertDialog.Builder builder=new AlertDialog.Builder(Customer_Login.this);
                            builder.setTitle("Enter Correct Email id!").setMessage("This "+mail+"Email is not in the Customer Database!")
                                    .setNegativeButton("Ok",null).setCancelable(false);
                            AlertDialog alertDialog=builder.create();
                            alertDialog.show();
                            cus_login_email.setText("");
                            cus_login_email.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private Boolean validateCusEmail() {
        String val = cus_login_email.getText().toString().trim();
        if (val.isEmpty()) {
            cus_login_email.setError("Fields cannot be empty");
            cus_login_email.requestFocus();
            return false;
        } else {
            cus_login_email.setError(null);
            return true;
        }
    }
    private Boolean validateCusPsw() {
        String val = cus_login_password.getText().toString().trim();
        if (val.isEmpty()) {
            cus_login_password.setError("Fields cannot be empty");
            cus_login_password.requestFocus();
            return false;
        } else {
            cus_login_password.setError(null);
            return true;
        }
    }
}