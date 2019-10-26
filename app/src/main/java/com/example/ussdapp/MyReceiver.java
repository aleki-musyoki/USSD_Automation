package com.example.ussdapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.hover.sdk.api.Hover;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "smsBroadcastReceiver";
    public String msg, phoneNo = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        //retrieve the general action to be performed and display on log
        Log.i(TAG,"Intent Received " +intent.getAction());
        if (intent.getAction()==SMS_RECEIVED){
            //retrieve a map of extended data from the intent
            Bundle databundle = intent.getExtras();
            if (databundle!=null){
                //creating PDU(Protocol Data Unit) object which is a protocol for transferring message
                Object[] mypdu = (Object[])databundle.get("pdus");
                final SmsMessage[] message = new SmsMessage[mypdu.length];

                for (int i=0; i<mypdu.length; i++){
                    //for build versions >= API Level 23
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        String format = databundle.getString("format");
                        //From PDU we get all object and SmsMessage Object using following line of code
                        message[i] = SmsMessage.createFromPdu((byte[])mypdu[i], format);
                    }else{
                        //<API Level 23
                        message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                    }
                    msg = message[i].getMessageBody();
                    phoneNo = message[i].getOriginatingAddress();
//                    Intent send_data = new Intent();
//                    send_data.putExtra("Message", msg);
//                    send_data.putExtra("PhoneNumber", phoneNo);

                }
                Toast.makeText(context,"Message: "+ msg +"\nNumber: "+ phoneNo, Toast.LENGTH_LONG).show();

            }
        }





    }

}