package com.yenimobile.quitcigbro.someServices;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yenimobile.quitcigbro.broadcastReceivers.MyReceiver;

import java.util.Calendar;

public class MyIntentService extends IntentService {

    private SharedPreferences mPrefNumCigDaily, mPrefIsFirstCig;

    public MyIntentService() {
        super("intentServiceStartDay");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent.hasExtra("hours")
                && intent.hasExtra("minutes")){

            int hour = intent.getIntExtra("hours", 0);
            int minute = intent.getIntExtra("minutes", 0);

            mPrefNumCigDaily = getSharedPreferences("PrefNumDailyCig", MODE_PRIVATE);
            mPrefIsFirstCig = getSharedPreferences("PrefIsFirstCig", MODE_PRIVATE);
            int numberCigarettes = mPrefNumCigDaily.getInt("numDailyCig", 0);
            boolean isFirstCig = mPrefIsFirstCig.getBoolean("isFirstCig", true);
            numberCigarettes = numberCigarettes - 1;
            mPrefNumCigDaily.edit().putInt("numDailyCig", numberCigarettes).apply();

            Log.e("myintentservice", "is it first cigarette ? " + isFirstCig);

            Log.e("myintentservice",
                    "the extras are " + hour + ":" + minute + "numb cig : "+ numberCigarettes);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            if(isFirstCig){
                mPrefIsFirstCig.edit().putBoolean("isFirstCig", true).apply();
                Intent myIntent = new Intent(this, MyReceiver.class);
                PendingIntent pendingIntent =
                        PendingIntent.getBroadcast(this, 0, myIntent,0);

                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

            }else {
                mPrefIsFirstCig.edit().putBoolean("isFirstCig", false).apply();
                Log.e("myintentservice", "false part" + isFirstCig);
                Intent myIntent = new Intent(this, MyReceiver.class);
                PendingIntent pendingIntent =
                        PendingIntent.getBroadcast(this, 0, myIntent,0);

                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
            }


        }else {
            stopSelf();
        }




    }
}
