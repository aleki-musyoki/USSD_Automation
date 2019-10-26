package com.example.ussdapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import java.util.HashMap;

public class TransactionReceiver extends BroadcastReceiver {
    public TransactionReceiver(){}
    @Override
    public void onReceive(Context context, Intent intent) {
        String uuid = intent.getStringExtra("uuid");
        String confirmationCode, balance;
        if (intent.hasExtra("transaction_extras")) {
            HashMap<String, String> t_extras = (HashMap<String, String>) intent.getSerializableExtra("transaction_extras");
            if (t_extras.containsKey("confirmCode"))
                confirmationCode = t_extras.get("confirmCode");
            if (t_extras.containsKey("balance"))
                balance = t_extras.get("balance");
        }
        
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Toast.makeText(context, "Boot Completed", Toast.LENGTH_SHORT).show();
        }
        
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            Toast.makeText(context, "connectivity changed", Toast.LENGTH_SHORT).show();
        }
    }
}
