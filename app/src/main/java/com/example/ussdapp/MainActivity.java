package com.example.ussdapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hover.sdk.api.Hover;
import com.hover.sdk.api.HoverParameters;
import com.hover.sdk.permissions.PermissionActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    int action_variable;
    private String TAG;
    private Double action_id;
    private String phoneNumber;
    private String amount;
    //Code for getting or asking for permission
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 0;
    private static final String READ_SMS = "android.developer.Telephony.SMS_READ";
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TOG = "smsBroadcastReceiver";
    public String msg, phoneNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Hover.initialize(this);


        //check if permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
            //If the permission is not granted then check if the user has denied the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_SMS)){
                //do nothing
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);




            }
        }

        Button permissionsButton = findViewById(R.id.btnPermissions);
        permissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PermissionActivity.class);
                startActivityForResult(i, 0);
            }
        });

        Button button = (Button) findViewById(R.id.action_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvMoney = findViewById(R.id.tvSendMoney);
                Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null,null);
                cursor.moveToFirst();
                String set_text = cursor.getString(13);
                tvMoney.setText(set_text);

                Pattern p = Pattern.compile("Congratulations!\\sYou\\shave\\sreceived\\s[0-9\\.0-9+]\\sKSH\\s.*");
                Matcher m = p.matcher(set_text);
                while (m.find()){
                    Toast.makeText(getApplicationContext(), "Found: "+m.group(), Toast.LENGTH_LONG).show();
                }
            }
        });
        Button trial_button = findViewById(R.id.btnTrialAction);
        trial_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText phone_number = findViewById(R.id.edtNumber);
                EditText edt_amount = findViewById(R.id.edtAmount);
                EditText edt_package = findViewById(R.id.edtPackage);
                String mobileNumber = phone_number.getText().toString();
                String amount = edt_amount.getText().toString();
                String package_name = edt_package.getText().toString();
                Intent intent = new HoverParameters.Builder(MainActivity.this)
                        .request("20dbf1b2")
                        .extra("phoneNumber", mobileNumber)
                        .extra("amount",amount)
                        .extra("package", package_name)
                        .buildIntent();
                startActivity(intent);
                startActivityForResult(intent, 0);
            }
        });






    }
    //after getting the result of the permission requests will be passed through this method
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        // will check the requestCode
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_RECEIVE_SMS:{
                //check whether the length of grantResults is greater than 0 and is equal to PERMISSIONS_GRANTED
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //Now broadcast reciever will work in background
                    Toast.makeText(this, "Thankyou for permitting", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this, "Well I can't do anything until you permit me", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            String[] sessionTextArr = data.getStringArrayExtra("ussd_messa" +
                    "ges");
            String uuid = data.getStringExtra("uuid");
        } else if (requestCode == 0 && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Error: " + data.getStringExtra("error"), Toast.LENGTH_LONG).show();
        }
    }



//[a-zA-Z0-9]+\\sConfirmed\\.on(0-9/0-9/0-9)+\\sat\\s(0-9+:0-9+\\sAM|PM)\\sKsh0-9+\\.{0}+\\sreceived\\sfrom\\s"+phoneNo+"A-Z\\sA-Z\\sA-Z*
}

