package com.example.ussdapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.admin.SystemUpdatePolicy;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Signup extends AppCompatActivity {
    private ImageView img_signup, img_login;
    private EditText edt_name, edt_password, edt_phone;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpUIViews();

        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);


        img_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login_page = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(login_page);
                finish();
            }
        });

        img_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });

    }

    private void setUpUIViews() {
        edt_name = findViewById(R.id.editUserName);
        edt_phone = findViewById(R.id.editMobile);
        edt_password = findViewById(R.id.editPass);
        img_signup = findViewById(R.id.imageViewSignUp);
        img_login = findViewById(R.id.imageViewLogin);
    }

    private void CreateAccount() {
        String name = edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        String password = edt_password.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            dialog.setMessage("Registering\nPlease wait while we're checking credentials...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            ValidatePhoneNumber(name, phone, password);
        }
    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Signup.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(Signup.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(Signup.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Toast.makeText(Signup.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    //startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
