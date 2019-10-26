package com.example.ussdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class InfoActivity extends AppCompatActivity {
    ImageView img_start, img_start_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        img_start = findViewById(R.id.imgStart);
        img_start_button = findViewById(R.id.imgStartButton);


        img_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent get_started = new Intent(getApplicationContext(),Signup.class);
                        startActivity(get_started);
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                        finish();
                    }
                },100);
            }
        });
    }
}
