package com.yenimobile.quitcigbro.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class CigReferenceReceiver extends BroadcastReceiver {

    public CigReferenceReceiver(){}


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("alarmReceiver", "the alarm receiver is receiving");

        if (intent.hasExtra("numCigreference")){
            int numberCigarettesReference = intent.getIntExtra("numCigreference", 10);
            numberCigarettesReference = numberCigarettesReference - 1;
            SharedPreferences mPrefNumCigReference =
                    context.getSharedPreferences("PrefNumCigReference", context.MODE_PRIVATE);
            mPrefNumCigReference.edit().putInt("numCigreference", numberCigarettesReference).apply();
        }

    }
}
