package com.example.ussdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ussdapp.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_phone,edit_password;
    private ImageView img_sign_up, imgLogin;
    private ProgressDialog progressDialog;
    private String parentDbName = "Users";
    private int counter = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUIViews();

        progressDialog = new ProgressDialog(this);

        img_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(login);
                finish();
            }
        });

        imgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edit_phone.getText().toString();
                String password = edit_password.getText().toString();

                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(LoginActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Please fill in your password", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setMessage("Please wait while we're checking the credentials");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    AlllowAccessToAccount(phone, password);

                }
            }
        });
    }

    private void AlllowAccessToAccount(final String phone,final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()){
                    Users usersData =dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)){
                        if (usersData.getPassword().equals(password)){
                            if (parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent login_intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(login_intent);
                                finish();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();


                            }
                        }
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Account with this number: " +phone+ " does not exist", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void setupUIViews() {
        edit_phone = findViewById(R.id.editPhone);
        edit_password = findViewById(R.id.editPassword);
        imgLogin = findViewById(R.id.img_login);
        img_sign_up = findViewById(R.id.imageSignUp);
    }
}
