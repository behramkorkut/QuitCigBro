package com.yenimobile.quitcigbro.someUtilsPackage;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.yenimobile.quitcigbro.QuitcigMainActivity;
import com.yenimobile.quitcigbro.R;
import com.yenimobile.quitcigbro.someServices.CigProcessFirebaseJobService;
import com.yenimobile.quitcigbro.someServices.CigProcessIntentService;

public class CigProcessUtils {

    public static final String TAG = CigProcessUtils.class.getSimpleName();


    public static boolean sProcessInitialized;

    synchronized public static void cigaretteSchedule(Context ctx){
        if (sProcessInitialized) return;
        Log.e("sheduleReminder", "insideScheduleReminder");
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(ctx));
        Job constraintedReminderJob = jobDispatcher.newJobBuilder()
                .setService(CigProcessFirebaseJobService.class)
                .setTag("cigaretteJobTag")
                //.setConstraints(Constraint.DEVICE_CHARGING)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        10,
                        10 + 10))
                .setReplaceCurrent(true)
                .build();

        jobDispatcher.mustSchedule(constraintedReminderJob);
        sProcessInitialized = true;
    }


    public static final String ACTION_VALIDATE = "actionValide";
    public static final String ACTION_DISMISS = "actionDismiss";
    public static final String ACTION_SCHEDULED = "actionScheduled";


    public static void executeTask(Context ctx, String action){
        if (ACTION_VALIDATE.equals(action)) {
            validateSmokingProcess(ctx);
            clearAllNotifs(ctx);
        }else if (ACTION_DISMISS.equals(action)){
            clearAllNotifs(ctx);
        }else if (ACTION_SCHEDULED.equals(action)) {
            scheduledSmokingProcess(ctx);
        }
    }

    private static void validateSmokingProcess(Context c){
        Toast.makeText(c, "la cigarette a été fumée", Toast.LENGTH_LONG).show();
    }

    private static void clearAllNotifs(Context ctx){
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();
    }


    private static void scheduledSmokingProcess(Context ctx){
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        //--------
        Intent startCigActivity = new Intent(ctx, QuitcigMainActivity.class);
        PendingIntent scheduledPI =  PendingIntent.getActivity(
                ctx,
                123,
                startCigActivity,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        //--------
        Intent ignoreIntent = new Intent(ctx, CigProcessIntentService.class);
        ignoreIntent.setAction(ACTION_DISMISS);
        PendingIntent ignorePendingIntent =
                PendingIntent.getService(
                        ctx,
                        456,
                        ignoreIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Action ignoreActionCompat = new NotificationCompat.Action(
                R.drawable.ic_local_drink_grey_120px,
                "no cigarette for me ",
                ignorePendingIntent
        );
        //-------

        Intent proceedIntent  = new Intent(ctx, CigProcessIntentService.class);
        proceedIntent.setAction(ACTION_VALIDATE);
        PendingIntent proceedPendingIntent =
                PendingIntent.getService(
                        ctx,
                        7890,
                        proceedIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Action proceedActionCompat = new NotificationCompat.Action(
                R.drawable.ic_cancel_black_24px,
                "yess i complete the cig smoooooooking",
                proceedPendingIntent
        );
        //--------
        NotificationCompat.Builder notifBuilder =
                new NotificationCompat.Builder(ctx, "ScheduledCigaretteChannel")
                        .setColor(ContextCompat.getColor(ctx, R.color.colorAccent))
                        .setSmallIcon(R.drawable.ic_power_pink_80px)
                        .setContentTitle("Scheduled newwwwwwwww  mofo cigarette notification broooo ")
                        //.setStyle(new NotificationCompat.BigTextStyle().bigText("biiiiiig text brooo"))
                        .setContentText("This notification is new and is also a scheduled notification for smoking cigarette")
                        .setContentIntent(scheduledPI)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .addAction(ignoreActionCompat)
                        .addAction(proceedActionCompat);

        nm.notify(1111, notifBuilder.build());
        Log.e("scheduledSmokingProcess", "the notification is notified broooo");
    }



}
