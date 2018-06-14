package com.yenimobile.quitcigbro;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.yenimobile.quitcigbro.someServices.MyIntentService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class QuitcigMainActivity extends AppCompatActivity {

    private EditText mCigPriceET, mNumberCigET;
    private TextView mFirstCigTime, mLastCigTme;
    private TimePickerDialog mPickerDialog;
    private Button mStartButton, mStopButton, mSpecificTimeButton, mGetCurrentTimeButton;
    int mPickedHour, mLastPickehour;
    int mPickedMinute, mLastPickedMinute;
    int mNumberOfCigarettes = 10;
    private Spinner mSpinnerPrice, mSpinnerNumberCig;

    private SharedPreferences mPrefNumCigDaily, mPrefNumCigReference, mPrefIsFirstCig, mPrefIntervalleFirstLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quitcig_main);


        //the sharedPreference for number of cigarettes
        mPrefNumCigDaily = getSharedPreferences("PrefNumDailyCig", MODE_PRIVATE);
        mPrefNumCigReference = getSharedPreferences("PrefNumCigReference", MODE_PRIVATE);
        mPrefIsFirstCig = getSharedPreferences("PrefIsFirstCig", MODE_PRIVATE);
        mPrefIntervalleFirstLast = getSharedPreferences("PrefIntervalleFirstLast", MODE_PRIVATE);
        //-- -- - - - - -- - -- - - -- - -- - - - -- - --- -- - - - - -- - -- - - -- - -- - - - --


        //the edit text for user input
        mCigPriceET = findViewById(R.id.et_cig_price);
        mNumberCigET = findViewById(R.id.et_number_cig_smoked);
        //-- -- - - - - -- - -- - - -- - -- - - - -- - --- -- - - - - -- - -- - - -- - -- - - - --

        //The time picker user defining the first hours cigarette
        mFirstCigTime = findViewById(R.id.et_first_cig_smoked);
        mLastCigTme = findViewById(R.id.et_last_cig_smoked);
        //-- -- - - - - -- - -- - - -- - -- - - - -- - --- -- - - - - -- - -- - - -- - -- - - - --

        mStartButton = findViewById(R.id.button_start);
        mStopButton = findViewById(R.id.button_stop);
        mSpecificTimeButton = findViewById(R.id.button_send_notif_specific_time);
        mStartButton.setVisibility(View.GONE);
        mGetCurrentTimeButton = findViewById(R.id.button_getCurrentTime);
        mGetCurrentTimeButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
        //-- -- - - - - -- - -- - - -- - -- - - - -- - --- -- - - - - -- - -- - - -- - -- - - - --

        mFirstCigTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                mPickerDialog = new TimePickerDialog(QuitcigMainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                mFirstCigTime.setText(hour + ":" + minute);
                                mPickedHour = hour;
                                mPickedMinute = minute;
                                mStartButton.setVisibility(View.VISIBLE);

                            }
                        }, hour, minutes, true);
                mPickerDialog.show();
            }
        });


        mLastCigTme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                mPickerDialog = new TimePickerDialog(QuitcigMainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                mLastCigTme.setText(hour + ":" + minute);
                                mLastPickehour = hour;
                                mLastPickedMinute = minute;
                                mStartButton.setVisibility(View.VISIBLE);

                            }
                        }, hour, minutes, true);
                mPickerDialog.show();
            }
        });
        // - - - - - - - - - - - - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - -

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ReminderUtils.scheduleCigaretteReminder(QuitcigMainActivity.this, 10, 10 );
                //CigProcessUtils.cigaretteSchedule(QuitcigMainActivity.this);
                mNumberOfCigarettes = Integer.valueOf(mNumberCigET.getText().toString());
                mPrefNumCigDaily.edit().putInt("numDailyCig", mNumberOfCigarettes - 1).apply();

                mPrefIsFirstCig.edit().putBoolean("isFirstCig", true).apply();


                Intent firstDayIntent = new Intent(QuitcigMainActivity.this, MyIntentService.class);
                Bundle extra = new Bundle();
                extra.putInt("hours", mPickedHour);
                extra.putInt("minutes", mPickedMinute);
                firstDayIntent.putExtras(extra);
                startService(firstDayIntent);
                Log.e("startButton", "mStartButton is pressed the process is launched");

                mPrefNumCigReference.edit().putInt("numCigreference", mNumberOfCigarettes - 1).apply();

                //the interval between first and last cigarette in seconds
                int firstcigInSeconds = (int) (TimeUnit.HOURS.toSeconds(mPickedHour) + TimeUnit.MINUTES.toSeconds(mPickedMinute));
                int lastcigInSeconds = (int) (TimeUnit.HOURS.toSeconds(mLastPickehour) + TimeUnit.MINUTES.toSeconds(mLastPickedMinute));

                int intervalleFirstLastInSeconds = lastcigInSeconds - firstcigInSeconds ;
                int numberOfCig = Integer.valueOf(mNumberCigET.getText().toString());
                int intervalleBetween2cigsInSeconds = intervalleFirstLastInSeconds / numberOfCig;

                ArrayList<Integer> intervalleArray =
                        getDurationArraylist(intervalleBetween2cigsInSeconds);
                Log.e("quitcigMAin", "intevalle between 2 cigs is :   " + intervalleArray);
                mPrefIntervalleFirstLast.edit().putInt("intervalleFirstLastHour", intervalleArray.get(0)).apply();
                mPrefIntervalleFirstLast.edit().putInt("intervalleFirstLastMinute", intervalleArray.get(1)).apply();
                //- - - -- -  -- - - - -- - - - -- - - - -- - --  --  - - -- - - - -- - - - ----- - - - - - - - --  - -


                startActivity(new Intent(QuitcigMainActivity.this, BeforeStartActivity.class));

            }
        });//end of start button

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseJobDispatcher jobDispatcher =
                        new FirebaseJobDispatcher(new GooglePlayDriver(QuitcigMainActivity.this));
                jobDispatcher.cancelAll();
            }

        });

        mGetCurrentTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                Toast.makeText(QuitcigMainActivity.this,
                        "hours is :" + hour + "minute is :" + minutes,
                        Toast.LENGTH_LONG).show();
            }
        });//end of get currenttime button

        //- - - - - -- - - - - - -- - - - - - -  - -- - - - - - - - - - - - - -- - - - - - -- - - - - - -


        Date currentTime = Calendar.getInstance().getTime();
        Log.e("currentTime", "current time is "+ currentTime);

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String currentFormatedDate = format.format(date);
        Log.e("formattedDate", "current formatted time is "+ currentFormatedDate);





    }// - - - - - - - -- - - - end of onCreate - - - - - - - - -


    private ArrayList<Integer> getDurationArraylist(int seconds) {

        ArrayList<Integer> yoArray = new ArrayList<>();
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        yoArray.add(0, hours); yoArray.add(1, minutes); yoArray.add(2, seconds);

        return yoArray;
    }

    private int twoDigit(int number) {

        if (number == 0) {
            return number;
        }

        if (number / 10 == 0) {
            return 0 + number;
        }

        return number;
    }












}
