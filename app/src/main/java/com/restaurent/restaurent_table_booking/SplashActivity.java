package com.restaurent.restaurent_table_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    TextView textView;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo=findViewById(R.id.logo);
        textView=findViewById(R.id.app_name);
        lottieAnimationView=findViewById(R.id.lottie);
        logo.animate().translationY(1400).setDuration(2500).setStartDelay(2500);
        textView.animate().translationY(1400).setDuration(2500).setStartDelay(2500);
        lottieAnimationView.animate().translationY(1400).setDuration(2000).setStartDelay(2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },4000);


    }
}