package com.yenimobile.quitcigbro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.yenimobile.quitcigbro.someServices.MyIntentService;

import java.util.Calendar;

public class DayToDayActivity extends AppCompatActivity {

    private Button mNextCIgButton, mStopService;
    private SharedPreferences mPrefIsFirstCig, mPrefNumCigDaily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_to_day);

        mPrefIsFirstCig = getSharedPreferences("PrefIsFirstCig", MODE_PRIVATE);
        mPrefNumCigDaily = getSharedPreferences("PrefNumDailyCig", MODE_PRIVATE);

        mNextCIgButton = findViewById(R.id.nextCig_Button);
        mStopService = findViewById(R.id.stopDailyService);

        mNextCIgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ReminderUtils.scheduleReminder(StartDayActivity.this);
                Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);

                int nextHour = hour + 0;
                int nextMinutes = minutes + 1;

                int numDailyCig = mPrefNumCigDaily.getInt("numDailyCig", 0);
                numDailyCig = numDailyCig - 1;


                Intent nextCigIntent = new Intent(DayToDayActivity.this, MyIntentService.class);
                Bundle extra = new Bundle();
                extra.putInt("hours", nextHour);
                extra.putInt("minutes", nextMinutes);
                mPrefIsFirstCig.edit().putBoolean("isFirstCig", false).apply();
                mPrefNumCigDaily.edit().putInt("numDailyCig", numDailyCig);
                nextCigIntent.putExtras(extra);
                startService(nextCigIntent);
                Log.e("daytoday startButton", "nextcig  is launched");

                finishAffinity();
                System.exit(0);
            }
        });

        mStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseJobDispatcher jobDispatcher =
                        new FirebaseJobDispatcher(new GooglePlayDriver(DayToDayActivity.this));
                jobDispatcher.cancelAll();
            }
        });
    }
}
