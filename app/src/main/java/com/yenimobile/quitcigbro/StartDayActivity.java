package com.yenimobile.quitcigbro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.yenimobile.quitcigbro.someServices.MyIntentService;
import com.yenimobile.quitcigbro.syncPackage.ReminderUtils;

import java.util.Calendar;

public class StartDayActivity extends AppCompatActivity {


    private Button mStartTheDayButton, mStopService;
    private SharedPreferences mPrefIsFirstCig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_day);
        mStartTheDayButton = findViewById(R.id.startTheday_startButton);
        mPrefIsFirstCig = getSharedPreferences("PrefIsFirstCig", MODE_PRIVATE);


        mStartTheDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ReminderUtils.scheduleReminder(StartDayActivity.this);
                Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);

                int nextHour = hour + 0;
                int nextMinutes = minutes + 1;
                Toast.makeText(StartDayActivity.this,
                        "nextHour : "+ nextHour + " nextMinute :" + nextMinutes,
                        Toast.LENGTH_LONG).show();

                Intent nextCigIntent = new Intent(StartDayActivity.this, MyIntentService.class);
                Bundle extra = new Bundle();
                extra.putInt("hours", nextHour);
                extra.putInt("minutes", nextMinutes);
                mPrefIsFirstCig.edit().putBoolean("isFirstCig", false).apply();
                nextCigIntent.putExtras(extra);
                startService(nextCigIntent);
                Log.e("startDay startButton", "nextcig  is launched");


                //this service is to make a new notification in 24 hours, to start another day
                ReminderUtils.scheduleDailyDecrement(StartDayActivity.this);

                finishAffinity();
                System.exit(0);

            }
        });

        mStopService = findViewById(R.id.stoptheservice);
        mStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseJobDispatcher jobDispatcher =
                        new FirebaseJobDispatcher(new GooglePlayDriver(StartDayActivity.this));
                jobDispatcher.cancelAll();
            }
        });



    }// end of onCreate






}
