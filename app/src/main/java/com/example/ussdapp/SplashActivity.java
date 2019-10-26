package com.example.ussdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    ImageView img_ussd, img_advert, img_button;
    private static int WELCOME_TIMEOUT= 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        img_ussd = findViewById(R.id.imgUssd);
        img_advert = findViewById(R.id.imgAdvert);
        img_button = findViewById(R.id.imgButton);

        //Setting animation to run
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_in_two);
        img_ussd.startAnimation(animation);
        img_advert.startAnimation(animation);
        final Intent intent = new Intent(SplashActivity.this,InfoActivity.class);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent button = new Intent(getApplicationContext(),InfoActivity.class);
                        startActivity(button);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        finish();

                    }
                },WELCOME_TIMEOUT);
            }
        });
    }
}