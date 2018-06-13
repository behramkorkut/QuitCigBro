package com.yenimobile.quitcigbro.someServices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobService;
import com.yenimobile.quitcigbro.QuitcigMainActivity;
import com.yenimobile.quitcigbro.R;

public class DailyFirebaseJobservice extends JobService {


    private AsyncTask mBackGroundTask;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences mPrefCigRef =
                getSharedPreferences("PrefNumCigReference", MODE_PRIVATE);
        int mCigarettesReference = mPrefCigRef.getInt("numCigreference", 0);
        if (mCigarettesReference <= 0 ) {
            stopSelf();
        }
    }

    private void dailyNotification(Context context){

        SharedPreferences mPrefCigRef =
                context.getSharedPreferences("PrefNumCigReference", MODE_PRIVATE);
        int mCigarettesReference = mPrefCigRef.getInt("numCigreference", 0);

        Intent dayStartOff = new Intent(context, QuitcigMainActivity.class);
        PendingIntent daylyPendingIntent = PendingIntent.getActivity(
                context,
                1098,
                dayStartOff,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Log.e("BeforeO", "inside cigreminder notif");
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel cigNotifChannel =
                    new NotificationChannel(
                            "dailyCigReminder",
                            "dailyCigReminderName",
                            NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(cigNotifChannel);

            Notification.Builder nBuilder = new Notification.Builder(context, "dailyCigChannel")
                    .setColor(ContextCompat.getColor(context, R.color.material_red))
                    .setSmallIcon(R.drawable.baseline_monetization_on_white_48)
                    .setContentIntent(daylyPendingIntent)
                    .setAutoCancel(true);

            nm.notify(92892, nBuilder.build());

        }else {
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, "dailyCigChannel")
                    .setColor(ContextCompat.getColor(context, R.color.material_red))
                    .setSmallIcon(R.drawable.baseline_monetization_on_white_48)
                    .setContentIntent(daylyPendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                    .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                    .setContentTitle("Start Off your day of quitting cigarette ")
                    .setContentText("today you will smoke "+ mCigarettesReference + " cigarettes");

            nm.notify(92892, nBuilder.build());
            Log.e("beforeO", "the notification is notify");
        }
    }


    @Override
    public boolean onStartJob(final com.firebase.jobdispatcher.JobParameters jobParameters) {
        mBackGroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = DailyFirebaseJobservice.this;
                dailyNotification(context);



                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParameters, false);
            }
        };

        mBackGroundTask.execute();
        Log.e("ReminderFirebase", "the background task is executed");
        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        if(mBackGroundTask != null) mBackGroundTask.cancel(true);
        return true;
    }
}
