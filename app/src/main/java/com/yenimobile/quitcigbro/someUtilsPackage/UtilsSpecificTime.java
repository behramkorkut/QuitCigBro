package com.yenimobile.quitcigbro.someUtilsPackage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.yenimobile.quitcigbro.QuitcigMainActivity;
import com.yenimobile.quitcigbro.R;

public class UtilsSpecificTime {

    public static boolean sInitializeSpecificTime;

    synchronized public static void cigaretteSchedule(Context ctx){
        if (sInitializeSpecificTime) return;
        Log.e("sheduleReminder", "insideScheduleReminder");
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(ctx));
        Job constraintedReminderJob = jobDispatcher.newJobBuilder()
                .setService(SpecificTimeFirebaseJobService.class)
                .setTag("specificTimeJobTag")
                //.setConstraints(Constraint.DEVICE_CHARGING)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setRecurring(false)
                //.setTrigger(Trigger.executionWindow(10,10 + 10))
                .setReplaceCurrent(false)
                .build();

        jobDispatcher.mustSchedule(constraintedReminderJob);
        sInitializeSpecificTime = true;
    }


    public class SpecificTimeFirebaseJobService extends JobService{
        private AsyncTask mSpecificTimeBackGroundTask;

        @Override
        public boolean onStartJob(final JobParameters jobParams) {

            mSpecificTimeBackGroundTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] objects) {
                    Context context = SpecificTimeFirebaseJobService.this;
                    //CigProcessUtils.executeTask(context, CigProcessUtils.ACTION_SCHEDULED);
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    jobFinished(jobParams, false);
                }
            };

            mSpecificTimeBackGroundTask.execute();
            Log.e("mSpecificTimeBackGroundTask", "the specific background task is executed");
            return true;
        }

        @Override
        public boolean onStopJob(JobParameters job) {
            if(mSpecificTimeBackGroundTask != null)
                mSpecificTimeBackGroundTask.cancel(true);
            return true;
        }
    }

    //- - - - -- - - - - -- - - - -- - - - - - - - - -- - - - - - - - - - - - - - - - - - -- - - -


    public static NotificationManager mNotifManager;


    public static void generateNotification(Context context){
        Log.e("generate", "generate notification is triggered");

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelReminder =
                    new NotificationChannel(
                            "specificTimeNotification",
                            "reminderNotification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(channelReminder);

            Intent resultIntent = new Intent(context, QuitcigMainActivity.class);
            TaskStackBuilder tsb = TaskStackBuilder.create(context);
            tsb.addParentStack(QuitcigMainActivity.class);
            PendingIntent resultPendingIntent =
                    tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            Notification.Builder notifBuilder = new Notification.Builder(context, "specificTimeNotification")
                    .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                    .setSmallIcon(R.drawable.ic_local_drink_grey_120px)
                    .setContentTitle("Oreo notification brooooooooo ")
                    .setStyle(new Notification.BigPictureStyle().bigLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.art_plane)))//.setStyle(new Notification.BigTextStyle().bigText("biiiiiig text brooo"))
                    .setContentText("the content text is here broooo")
                    .setContentIntent(resultPendingIntent)
                    .setAutoCancel(true);

            notificationManager.notify(91221, notifBuilder.build());
        }else {
            NotificationCompat.Builder notifBuilder =
                    new NotificationCompat.Builder(context, "specificOneTimeNotification")
                            .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                            .setSmallIcon(R.drawable.ic_power_pink_80px)
                            .setContentTitle("Scheduled newwwwwwwww  mofo cigarette notification broooo ")
                            //.setStyle(new NotificationCompat.BigTextStyle().bigText("biiiiiig text brooo"))
                            .setContentText("This notification is new and is also a scheduled notification for smoking cigarette")
                            .setAutoCancel(true)
                            .setTicker("take a look ticker broooo")
                            .setPriority(NotificationCompat.PRIORITY_HIGH);
            Bitmap bitmapImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.art_clear);
            NotificationCompat.BigPictureStyle bigPictureStyle =
                    new NotificationCompat.BigPictureStyle().bigPicture(bitmapImage);
            bigPictureStyle.setSummaryText("this is the big picture summary text broooo");
            notifBuilder.setStyle(bigPictureStyle);

            Intent resultIntent = new Intent(context, QuitcigMainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    context,
                    265262,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );


            notifBuilder.setContentIntent(resultPendingIntent);

            notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(91221, notifBuilder.build());
        }



    }


}//end of class

