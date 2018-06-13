package com.yenimobile.quitcigbro.syncPackage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.yenimobile.quitcigbro.someServices.CigaretteFirebaseJobService;
import com.yenimobile.quitcigbro.someServices.DailyFirebaseJobservice;
import com.yenimobile.quitcigbro.someServices.ReminderFirebaseJobservice;

import java.util.concurrent.TimeUnit;

public class ReminderUtils {

    private static final int REMINDER_INTERVAL_MINUTES = 1;
    private static final int REMINDER_INTERVAL_SECONDS =
            (int) TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES);
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;


    private static final String REMINDER_JOB_TAG = "reminder_tag";
    private static boolean sInitialized;


    private static boolean sCigScheduleInitialized;
    private static final String CIG_JOB_TAB = "cig_tag";

    private static final int INTERVALHOURS = 24;
    private static final int INTERVALSECONDS = (int) TimeUnit.HOURS.toSeconds(INTERVALHOURS);
    private static final int SYNCFLEXSECONDS = INTERVALSECONDS;




    //synchronized : we don't want this method to be executed more than once at a time;
    synchronized public static void scheduleReminder(Context ctx){
        if (sInitialized) return;
        Log.e("sheduleReminder", "insideScheduleReminder");
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(ctx));
        Job constraintedReminderJob = jobDispatcher.newJobBuilder()
                .setService(ReminderFirebaseJobservice.class)
                .setTag(REMINDER_JOB_TAG)
                //.setConstraints(Constraint.DEVICE_CHARGING)
                //.setLifetime(1000*60*60)
                .setRecurring(true)
                //.setTrigger(Trigger.NOW)
                .setTrigger(Trigger.executionWindow(
                        10,
                        10 + 10))
                .setReplaceCurrent(false)
                .build();

        jobDispatcher.schedule(constraintedReminderJob);
        sInitialized = true;
    }


    synchronized public static void scheduleDailyDecrement(Context ctx){
        if(sInitialized) return;
        SharedPreferences mPrefCigRef =
                ctx.getSharedPreferences("PrefNumCigReference", ctx.MODE_PRIVATE);
        int mCigarettesReference = mPrefCigRef.getInt("numCigreference", 0);

        FirebaseJobDispatcher dailyDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(ctx));
        Job dailyJob = dailyDispatcher.newJobBuilder()
                .setService(DailyFirebaseJobservice.class)
                .setTag("dailyNotification")
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        INTERVALSECONDS,
                        INTERVALSECONDS + SYNCFLEXSECONDS
                ))
                .setReplaceCurrent(false)
                .build();
        if (mCigarettesReference <= 0){
            dailyDispatcher.cancelAll();
        }else {
            dailyDispatcher.schedule(dailyJob);
            Log.e("scheduleDaily", "dailyDispatcher schedule started");
            sInitialized = true;
        }
    }


    synchronized public static void scheduleCigaretteReminder(Context ctx, int intervalle, int syncFlex){
        if (sCigScheduleInitialized) return;
        Log.e("cigRemindUtils", "the schedule cigarette reminder is launched");
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(ctx));
        Job constrainedCigJob = jobDispatcher.newJobBuilder()
                .setService(CigaretteFirebaseJobService.class)
                .setTag(CIG_JOB_TAB)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        intervalle,
                        intervalle + syncFlex))
                .setReplaceCurrent(false)
                .build();
        jobDispatcher.schedule(constrainedCigJob);
        sCigScheduleInitialized = true;
        Log.e("cigremindUtils", "the schedule cigarette reminder is finnished");
    }








}
