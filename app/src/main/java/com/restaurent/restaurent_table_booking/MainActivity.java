package com.restaurent.restaurent_table_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;
import io.paperdb.Paper;

import static androidx.core.os.LocaleListCompat.create;

public class MainActivity extends AppCompatActivity {

    TextView main_cus,main_res;
    String CusUserEmailkey,CusUserPasswordkey,RestUserEmailkey,RestUserPasswordkey;
    ProgressDialog loading,loading1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        main_cus=findViewById(R.id.main_cus);
        main_res=findViewById(R.id.main_res);
        loading=new ProgressDialog(this);
        loading1=new ProgressDialog(this);
        Paper.init(this);
        main_cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialAlertDialogBuilder materialAlertDialogBuilder=new MaterialAlertDialogBuilder(MainActivity.this,R.style.AlertDialogThemec);
                materialAlertDialogBuilder.setTitle("Welcome Customer!").setMessage("Already have an Account Click 'Login', else 'Register'")
                        .setNegativeButton(
                                "Login", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(MainActivity.this,Customer_Login.class));
                                    }
                                }
                        )
                        .setPositiveButton(
                                "Register", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(MainActivity.this,Customer_Register.class));
                                    }
                                }

                        ).create();
                ;
                materialAlertDialogBuilder.show();
            }
        });
        main_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder=new MaterialAlertDialogBuilder(MainActivity.this,R.style.AlertDialogTheme);
                materialAlertDialogBuilder.setTitle("Welcome Restaurant!").setMessage("Already have an Account Click 'Login', else 'Register'")
                        .setNegativeButton(
                                "Login", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(MainActivity.this,Restaurant_Login.class));
                                    }
                                }
                        )
                        .setPositiveButton(
                                "Register", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(MainActivity.this,Restaurant_Register.class));
                                    }
                                }

                        ).create();
                ;
                materialAlertDialogBuilder.show();
            }
        });

        CusUserEmailkey= Paper.book().read(Reg.CusUserEmailkey);
        CusUserPasswordkey= Paper.book().read(Reg.CusUserPasswordkey);
        RestUserEmailkey= Paper.book().read(Reg.RestUserEmailkey);
        RestUserPasswordkey= Paper.book().read(Reg.RestUserPasswordkey);
        if(CusUserEmailkey !="" && CusUserPasswordkey!=""){

            if(!TextUtils.isEmpty(CusUserEmailkey)&&!TextUtils.isEmpty(CusUserPasswordkey)){
                loading.setTitle("Please Wait...");;
                loading.setMessage("Already Logged into Customer Account!");
                loading.setCanceledOnTouchOutside(false);
                loading.show();
                FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
                firebaseAuth.signInWithEmailAndPassword(CusUserEmailkey,CusUserPasswordkey).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            loading.dismiss();
                            Toasty.error(MainActivity.this,"Not Successful",Toast.LENGTH_LONG).show();
                        }else {
                            loading.dismiss();
                            Intent intent=new Intent(getApplicationContext(),Customer_Home.class);
                            intent.putExtra("mail",CusUserEmailkey);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            }

        }


        if(RestUserEmailkey !="" && RestUserPasswordkey!=""){
            if(!TextUtils.isEmpty(RestUserEmailkey)&&!TextUtils.isEmpty(RestUserPasswordkey)){
                loading1.setTitle("Please Wait...");;
                loading1.setMessage("Already Logged into Restaurant Account!");
                loading1.setCanceledOnTouchOutside(false);
                loading1.show();
                FirebaseAuth fireAuth= FirebaseAuth.getInstance();
                fireAuth.signInWithEmailAndPassword(RestUserEmailkey,RestUserPasswordkey).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()){
                            loading1.dismiss();
                            Toasty.error(MainActivity.this,"Not Successful",Toast.LENGTH_LONG).show();
                        }else {
                            loading1.dismiss();
                            Intent intent=new Intent(getApplicationContext(),Restaurant_Home.class);
                            intent.putExtra("mail",RestUserEmailkey);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            }

        }

    }

}