package com.yenimobile.quitcigbro.someUtilsPackage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.yenimobile.quitcigbro.CigreminderActivity;
import com.yenimobile.quitcigbro.QuitcigMainActivity;
import com.yenimobile.quitcigbro.R;
import com.yenimobile.quitcigbro.someServices.CigReminderIntentService;
import com.yenimobile.quitcigbro.someServices.ReminderInstantService;
import com.yenimobile.quitcigbro.syncPackage.ReminderTasks;

public class BeforeOnotificationsUtils {

    private static final int REMINDER_NOTIF_ID = 2331;
    private static final int REMINDER_PENDINGINTENT_ID = 3214;
    private static final String REMINDER_NOTIF_CHANNNEL_ID = "reminderNotif";

    private static final int IGNORE_REMINDER_PENDINGINTENT_ID = 3333;
    private static final int PROCCEDD_REMINDER_PENDINGINTENT_ID = 4444;



    private static final int CIG_NOTIF_ID = 12;
    private static final int CIG_PENDINGINTENT_ID = 13;
    private static final String CIG_NOTIF_CHANNEL_ID = "cigNotif";
    private static final int IGNORE_CIG_PENDINGINTENT_ID = 14;
    private static final int PROCEED_CIG_PENDINGINTENT_ID = 15;



    public static void clearAllNotifications(Context ctx){
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancelAll();

    }


    private static Notification.Action ignoreReminderAction(Context ctx){
        Intent ignoreReminderIntent = new Intent(ctx, ReminderInstantService.class);
        ignoreReminderIntent.setAction(ReminderTasks.REMINDER_NOTIFACTION_DISMISS);
        PendingIntent ignoreReminderPendingIntent =
                PendingIntent.getService(
                        ctx,
                        IGNORE_CIG_PENDINGINTENT_ID,
                        ignoreReminderIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Notification.Action ignoreAction = new Notification.Action(
                R.drawable.ic_local_drink_grey_120px,
                "no thanks broooo",
                ignoreReminderPendingIntent
        );

        return ignoreAction;
    }


    private static Notification.Action ignoreCigReminderAction(Context ctx){
        Intent ignoreCigReminderIntent = new Intent(ctx, CigReminderIntentService.class);
        ignoreCigReminderIntent.setAction(ReminderTasks.CIG_REMINDER_DISMISS);
        PendingIntent ignoreCigReminderPendingIntent =
                PendingIntent.getService(
                        ctx,
                        IGNORE_REMINDER_PENDINGINTENT_ID,
                        ignoreCigReminderIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Notification.Action cigIgnoreAction = new Notification.Action(
                R.drawable.ic_local_drink_grey_120px,
                "no i don't wanna smoke now",
                ignoreCigReminderPendingIntent
        );

        return cigIgnoreAction;
    }

    private static NotificationCompat.Action ignoreCigReminderActionCompat(Context ctx){
        Intent ignoreCigReminderIntent = new Intent(ctx, CigReminderIntentService.class);
        ignoreCigReminderIntent.setAction(ReminderTasks.CIG_REMINDER_DISMISS);
        PendingIntent ignoreCigReminderPendingIntent =
                PendingIntent.getService(
                        ctx,
                        IGNORE_CIG_PENDINGINTENT_ID,
                        ignoreCigReminderIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Action cigIgnoreAction = new NotificationCompat.Action(
                R.drawable.ic_local_drink_grey_120px,
                "no i don't wanna smoke now",
                ignoreCigReminderPendingIntent
        );

        return cigIgnoreAction;
    }

    private static NotificationCompat.Action ignoreReminderActionCompat(Context ctx){
        Intent ignoreReminderIntent = new Intent(ctx, ReminderInstantService.class);
        ignoreReminderIntent.setAction(ReminderTasks.REMINDER_NOTIFACTION_DISMISS);
        PendingIntent ignoreReminderPendingIntent =
                PendingIntent.getService(
                        ctx,
                        IGNORE_REMINDER_PENDINGINTENT_ID,
                        ignoreReminderIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Action ignoreAction = new NotificationCompat.Action(
                R.drawable.ic_local_drink_grey_120px,
                "no thanks broooo",
                ignoreReminderPendingIntent
        );

        return ignoreAction;
    }

    private static NotificationCompat.Action proceedReminderActionCompat(Context ctx){
        Intent proceedReminderIntent  = new Intent(ctx, ReminderInstantService.class);
        proceedReminderIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        PendingIntent proceedReminderPendingIntent =
                PendingIntent.getService(
                        ctx,
                        PROCCEDD_REMINDER_PENDINGINTENT_ID,
                        proceedReminderIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        NotificationCompat.Action proceedAction = new NotificationCompat.Action(
                R.drawable.ic_cancel_black_24px,
                "yess i complete the drinking",
                proceedReminderPendingIntent
        );
        return proceedAction;
    }


    private static Notification.Action proceedCigReminderAction(Context ctx){
        Intent proccedIntent = new Intent(ctx, CigReminderIntentService.class);
        proccedIntent.setAction(ReminderTasks.CIG_INCREMENT_ACTION);
        PendingIntent proccedPendingIntent = PendingIntent.getService(
                ctx, PROCEED_CIG_PENDINGINTENT_ID,
                proccedIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        Notification.Action proccedCigAction = new Notification.Action(
                R.drawable.ic_power_pink_80px,
                "yes bro i'm gonna smoke that cig",
                proccedPendingIntent
        );
        return proccedCigAction;
    }


    private static NotificationCompat.Action proceedCigReminderActionCompat(Context ctx){
        Intent proccedIntent = new Intent(ctx, CigReminderIntentService.class);
        proccedIntent.setAction(ReminderTasks.CIG_INCREMENT_ACTION);
        PendingIntent proccedPendingIntent = PendingIntent.getService(
                ctx, PROCEED_CIG_PENDINGINTENT_ID,
                proccedIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Action proccedCigAction = new NotificationCompat.Action(
                R.drawable.ic_power_pink_80px,
                "yes bro i'm gonna smoke that cig",
                proccedPendingIntent
        );
        return proccedCigAction;
    }

    private static Notification.Action proceedReminderAction(Context ctx){
        Intent proceedReminderIntent  = new Intent(ctx, ReminderInstantService.class);
        proceedReminderIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);
        PendingIntent proceedReminderPendingIntent =
                PendingIntent.getService(
                        ctx,
                        PROCCEDD_REMINDER_PENDINGINTENT_ID,
                        proceedReminderIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Notification.Action proceedAction = new Notification.Action(
                R.drawable.ic_cancel_black_24px,
                "yess i complete the drinking",
                proceedReminderPendingIntent
        );
        return proceedAction;
    }

    private static PendingIntent contentIntent(Context ctx){
        Intent startCigActivity = new Intent(ctx, CigreminderActivity.class);
        return PendingIntent.getActivity(
                ctx,
                REMINDER_PENDINGINTENT_ID,
                startCigActivity,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private static Bitmap largeIconBitmap(Context ctx){

        Resources res = ctx.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_local_drink_black_24px);
        return largeIcon;

    }


    public static void remindUser(Context ctx){

        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        //we need to create a notification channel for Android O and latest
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelReminder =
                    new NotificationChannel(
                            REMINDER_NOTIF_CHANNNEL_ID,
                            "reminderNotification",
                            NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(channelReminder);

            Notification.Builder notifBuilder = new Notification.Builder(ctx, REMINDER_NOTIF_CHANNNEL_ID)
                    .setColor(ContextCompat.getColor(ctx, R.color.colorAccent))
                    .setSmallIcon(R.drawable.ic_local_drink_black_24px)
                    .setLargeIcon(largeIconBitmap(ctx))
                    .setContentTitle("Yooooooo papi brossartttttttt")
                    //.setStyle(new Notification.BigTextStyle().bigText("biiiiiig text brooo"))
                    .setContentText("this is content text broooooooo")
                    .setContentIntent(contentIntent(ctx))
                    .setAutoCancel(true)
                    .addAction(ignoreReminderAction(ctx))
                    .addAction(proceedReminderAction(ctx));

            nm.notify(REMINDER_NOTIF_ID, notifBuilder.build());

        }else {
            NotificationCompat.Builder notifBuilder =
                    new NotificationCompat.Builder(ctx, REMINDER_NOTIF_CHANNNEL_ID)
                            .setColor(ContextCompat.getColor(ctx, R.color.colorAccent))
                            .setSmallIcon(R.drawable.ic_local_drink_black_24px)
                            .setLargeIcon(largeIconBitmap(ctx))
                            .setContentTitle("Reminder notification brooooooooo ")
                            //.setStyle(new NotificationCompat.BigTextStyle().bigText("biiiiiig text brooo"))
                            .setContentText("this is content text broooooooo")
                            .setContentIntent(contentIntent(ctx))
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .addAction(ignoreReminderActionCompat(ctx))
                            .addAction(proceedReminderActionCompat(ctx));

            nm.notify(REMINDER_NOTIF_ID, notifBuilder.build());
        }

    }



    public static void cigReminderNotif(Context ctx){
        Log.e("BeforeO", "inside cigreminder notif");
        NotificationManager nm = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel cigNotifChannel =
                    new NotificationChannel(
                            CIG_NOTIF_CHANNEL_ID,
                            "cigReminderChannel",
                            NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(cigNotifChannel);

            Notification.Builder nBuilder = new Notification.Builder(ctx, CIG_NOTIF_CHANNEL_ID)
                    .setColor(ContextCompat.getColor(ctx, R.color.colorAccent))
                    .setSmallIcon(R.drawable.ic_power_pink_80px)
                    .setContentIntent(cigContentIntent(ctx))
                    .setAutoCancel(true)
                    .addAction(ignoreCigReminderAction(ctx))
                    .addAction(proceedCigReminderAction(ctx));

            nm.notify(CIG_NOTIF_ID, nBuilder.build());

        }else {
            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(ctx, CIG_NOTIF_CHANNEL_ID)
                    .setColor(ContextCompat.getColor(ctx, R.color.colorAccent))
                    .setSmallIcon(R.drawable.ic_power_pink_80px)
                    .setContentIntent(cigContentIntent(ctx))
                    .setAutoCancel(true)
                    .setContentTitle("cigarette reminder my to ask you to smoke ")
                    .setContentText("this notification is made to remind you that you can smoke a cigarette")
                    .addAction(ignoreCigReminderActionCompat(ctx))
                    .addAction(proceedCigReminderActionCompat(ctx));

            nm.notify(CIG_NOTIF_ID, nBuilder.build());
            Log.e("beforeO", "the notification is notify");
        }
    }


    private static PendingIntent cigContentIntent(Context context) {
        Intent startQuitCigActivity = new Intent(context, QuitcigMainActivity.class);
        return PendingIntent.getActivity(
                context,
                CIG_PENDINGINTENT_ID,
                startQuitCigActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }





}
