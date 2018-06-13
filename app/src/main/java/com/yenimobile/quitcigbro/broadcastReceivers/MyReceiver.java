package com.yenimobile.quitcigbro.broadcastReceivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.yenimobile.quitcigbro.DayToDayActivity;
import com.yenimobile.quitcigbro.R;
import com.yenimobile.quitcigbro.StartDayActivity;

import java.util.Calendar;

public class MyReceiver extends BroadcastReceiver {

        private SharedPreferences mPrefIsFirstCig, mPrefNumCigDaily;

        public MyReceiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            mPrefIsFirstCig = context.getSharedPreferences("PrefIsFirstCig", context.MODE_PRIVATE);
            mPrefNumCigDaily = context.getSharedPreferences("PrefNumDailyCig", context.MODE_PRIVATE);
            boolean isFirstCig = mPrefIsFirstCig.getBoolean("isFirstCig", true);
            int numberDailyCig = mPrefNumCigDaily.getInt("numDailyCig", 0);

            Log.e("MyReceiver", "is it first cigarette ? -> " + isFirstCig);

            if(isFirstCig){
                    generateFirstCigNotification(context, numberDailyCig);
            }else {
                    generateDailyNotification(context, numberDailyCig);
            }

        }


        private void generateFirstCigNotification(Context context, int numberDailyCig){
            Log.e("generate", "generate first cig notification is triggered");

            Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channelReminder =
                        new NotificationChannel(
                                "specificTimeNotification",
                                "reminderNotification",
                                NotificationManager.IMPORTANCE_HIGH);

                notificationManager.createNotificationChannel(channelReminder);

                Intent resultIntent = new Intent(context, StartDayActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        context,
                        265262,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


                Notification.Builder notifBuilder = new Notification.Builder(context, "specificTimeNotification")
                        .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                        .setSmallIcon(R.drawable.ic_local_drink_grey_120px)
                        .setContentTitle("smoke ciggggg brooooooo")
                        .setStyle(new Notification.BigPictureStyle().bigLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.art_plane)))//.setStyle(new Notification.BigTextStyle().bigText("biiiiiig text brooo"))
                        .setContentText("the content Oreooooo text is here broooo")
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH);

                notificationManager.notify(91221, notifBuilder.build());

            }else {

                Bitmap bitmapImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.quitcig_makemoney);

                NotificationCompat.Builder notifBuilder =
                        new NotificationCompat.Builder(context, "specificOneTimeNotification")
                                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                                .setSmallIcon(R.drawable.baseline_monetization_on_white_48)
                                .setContentTitle("this is first cig notification")
                                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmapImage))
                                .setContentText("this is first cig notification,  you have "+ numberDailyCig + " cigs for today")
                                .setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                                .setPriority(NotificationCompat.PRIORITY_HIGH);


                Intent resultIntent = new Intent(context, StartDayActivity.class);
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


        private void generateDailyNotification(Context context, int numberDailyCig){
            Log.e("generate", "generate daily notification is triggered");

            Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channelReminder =
                        new NotificationChannel(
                                "specificTimeNotification",
                                "reminderNotification",
                                NotificationManager.IMPORTANCE_HIGH);

                notificationManager.createNotificationChannel(channelReminder);

                Intent resultIntent = new Intent(context, DayToDayActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        context,
                        265269,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


                Notification.Builder notifBuilder = new Notification.Builder(context, "specificTimeNotification")
                        .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                        .setSmallIcon(R.drawable.ic_local_drink_grey_120px)
                        .setContentTitle("smoke ciggggg brooooooo")
                        .setStyle(new Notification.BigPictureStyle().bigLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.art_plane)))//.setStyle(new Notification.BigTextStyle().bigText("biiiiiig text brooo"))
                        .setContentText("the content Oreooooo text is here broooo")
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH);

                notificationManager.notify(93221, notifBuilder.build());

            }else {

                Bitmap bitmapImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.quitcig_makemoney);

                NotificationCompat.Builder notifBuilder =
                        new NotificationCompat.Builder(context, "specificOneTimeNotification")
                                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                                .setSmallIcon(R.drawable.baseline_monetization_on_white_48)
                                .setContentTitle("this is daily cigs notification")
                                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmapImage))
                                .setContentText("you can smoke another cigarette now, you have "+ numberDailyCig + " cigatrettes left for today")
                                .setAutoCancel(true)
                                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                                .setPriority(NotificationCompat.PRIORITY_HIGH);


                Intent resultIntent = new Intent(context, DayToDayActivity.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(
                        context,
                        265269,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


                notifBuilder.setContentIntent(resultPendingIntent);

                notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(93221, notifBuilder.build());
            }
        }




}//end of class
