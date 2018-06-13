package com.yenimobile.quitcigbro;

import android.annotation.TargetApi;
import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yenimobile.quitcigbro.someListeners.CountdownListener;
import com.yenimobile.quitcigbro.someServices.CountdownTimerService;
import com.yenimobile.quitcigbro.someUtilsPackage.CountdownUtils;
import com.yenimobile.quitcigbro.someUtilsPackage.NotificationHelper;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {




    //ui countdown
    private Button btnStart;
    private Button btnStop;
    private TextView tvTime;

    //service countdown
    private Button btnServiceStart;
    private Button btnServiceStop;
    private TextView tvServiceTime;

    private long timer_unit = 1000;
    private long distination_total = timer_unit*10;
    private long service_distination_total = timer_unit*200;
    private long timer_couting;


    private int timerStatus = CountdownUtils.PREPARE;

    private Timer timer;
    private TimerTask timerTask;

    private CountdownTimerService countDownTimerService;

    private NotificationHelper mNoti;


    //handler to handle messages
    private Handler mHandler = new Handler(){

        @TargetApi(Build.VERSION_CODES.O)
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(timer_couting==distination_total){
                        btnStart.setText("START");
                    }
                    tvTime.setText(formateTimer(timer_couting));
                    break;
                case 2:
                    tvServiceTime.setText(formateTimer(countDownTimerService.getCountingTime()));
                    Notification.Builder nb =
                            mNoti.getNotification1("title", formateTimer(countDownTimerService.getCountingTime()));
                    mNoti.notify(1100, nb);
                    if(countDownTimerService.getTimerStatus()== CountdownUtils.PREPARE){
                        btnServiceStart.setText("START");
                    }
                    break;
            }
        }
    };


    //the listener that implements listener interface
    private class MyCountDownLisener implements CountdownListener {

        @Override
        public void onChange() {
            mHandler.sendEmptyMessage(2);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mNoti = new NotificationHelper(this);


        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        tvTime = (TextView) findViewById(R.id.tv_time);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        initTimerStatus();


        btnServiceStart = (Button) findViewById(R.id.btn_start2);
        btnServiceStop = (Button) findViewById(R.id.btn_stop2);
        tvServiceTime = (TextView) findViewById(R.id.tv_time2);

        btnServiceStart.setOnClickListener(this);
        btnServiceStop.setOnClickListener(this);

        tvTime.setText(formateTimer(timer_couting));

        countDownTimerService = CountdownTimerService.getInstance(new MyCountDownLisener()
                ,service_distination_total);
        initServiceCountDownTimerStatus();




    }//end of onCreate


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -- - -

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                switch (timerStatus){
                    case CountdownUtils.PREPARE:
                        startTimer();
                        timerStatus = CountdownUtils.START;
                        btnStart.setText("PAUSE");
                        break;
                    case CountdownUtils.START:
                        timer.cancel();
                        timerStatus = CountdownUtils.PASUSE;
                        btnStart.setText("RESUME");
                        break;
                    case CountdownUtils.PASUSE:
                        startTimer();
                        timerStatus = CountdownUtils.START;
                        btnStart.setText("PAUSE");
                        break;
                }
                break;
            case R.id.btn_stop:
                if(timer!=null){
                    timer.cancel();
                    initTimerStatus();
                    mHandler.sendEmptyMessage(1);
                }
                break;
            case R.id.btn_start2:
                switch (countDownTimerService.getTimerStatus()){
                    case CountdownUtils.PREPARE:
                        countDownTimerService.startCountDown();
                        btnServiceStart.setText("PAUSE");
                        break;
                    case CountdownUtils.START:
                        countDownTimerService.pauseCountDown();
                        btnServiceStart.setText("RESUME");
                        break;
                    case CountdownUtils.PASUSE:
                        countDownTimerService.startCountDown();
                        btnServiceStart.setText("PAUSE");
                        break;
                }
                break;
            case R.id.btn_stop2:
                btnServiceStart.setText("START");
                countDownTimerService.stopCountDown();
                break;
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -- - -

    private void initTimerStatus(){
        timerStatus = CountdownUtils.PREPARE;
        timer_couting = distination_total;
    }

    /**
     * start count down
     */
    private void startTimer(){
        timer = new Timer();
        timerTask = new MyTimerTask();
        timer.scheduleAtFixedRate(timerTask, 0, timer_unit);
    }

    /**
     * formate timer shown in textview
     * @param time
     * @return
     */
    private String formateTimer(long time){
        String str = "00:00:00";
        int hour = 0;
        if(time>=1000*3600){
            hour = (int)(time/(1000*3600));
            time -= hour*1000*3600;
        }
        int minute = 0;
        if(time>=1000*60){
            minute = (int)(time/(1000*60));
            time -= minute*1000*60;
        }
        int sec = (int)(time/1000);
        str = formateNumber(hour)+":"+formateNumber(minute)+":"+formateNumber(sec);
        return str;
    }

    /**
     * formate time number with two numbers auto add 0
     * @param time
     * @return
     */
    private String formateNumber(int time){
        return String.format("%02d", time);
    }


    /**
     * count down task
     */
    private class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            timer_couting -=timer_unit;
            if(timer_couting==0){
                cancel();
                initTimerStatus();
            }
            mHandler.sendEmptyMessage(1);
        }
    }

    /**
     * init countdowntimer buttons status for servce
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initServiceCountDownTimerStatus(){
        switch (countDownTimerService.getTimerStatus()) {
            case CountdownUtils.PREPARE:
                btnServiceStart.setText("START");
                break;
            case CountdownUtils.START:
                btnServiceStart.setText("PAUSE");
                break;
            case CountdownUtils.PASUSE:
                btnServiceStart.setText("RESUME");
                break;
        }
        tvServiceTime.setText(formateTimer(countDownTimerService.getCountingTime()));

    }





    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -- - -

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -- - -
}//end of activity
