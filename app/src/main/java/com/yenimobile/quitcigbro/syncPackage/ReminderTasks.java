package com.yenimobile.quitcigbro.syncPackage;

import android.content.Context;
import android.util.Log;

import com.yenimobile.quitcigbro.someUtilsPackage.BeforeOnotificationsUtils;
import com.yenimobile.quitcigbro.someUtilsPackage.PreferencesUtils;


public class ReminderTasks {

    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static final String REMINDER_NOTIFACTION_DISMISS = "dismissNotification";

    public static final String CHARGING_REMINDER = "chargingReminder";

    public static final String CIG_REMINDER_NOTIF = "cigReminderNotif";
    public static final String CIG_REMINDER_DISMISS = "cigReminderDismiss";
    public static final String CIG_INCREMENT_ACTION = "cigIncrementAction";



    public static void executeTask(Context context, String action) {

        if (ACTION_INCREMENT_WATER_COUNT.equals(action)) {
            incrementWaterCount(context);
        }else if (REMINDER_NOTIFACTION_DISMISS.equals(action)){
            BeforeOnotificationsUtils.clearAllNotifications(context);
        }else if (CHARGING_REMINDER.equals(action)) {
            issueChargingreminder(context);
        }
    }

    private static void incrementWaterCount(Context context) {

        PreferencesUtils.incrementWaterCount(context);
        BeforeOnotificationsUtils.clearAllNotifications(context);
    }


    private static void issueChargingreminder(Context ctx){
        //PreferencesUtils.incrementChargingReminderCount(ctx);
        BeforeOnotificationsUtils.remindUser(ctx);
    }


    //- - - - - - - - - - - - - - - - - -- - - - - -- -  - - - - - - --


    public static void executeCigTask(Context context, String action){
        if (CIG_REMINDER_NOTIF.equals(action)){
            makeCigReminderWithNotification(context);
            Log.e("reminderTasks" ,"inside executeCigTasks");
        }else if (CIG_REMINDER_DISMISS.equals(action)){
            BeforeOnotificationsUtils.clearAllNotifications(context);
        }else if (CIG_INCREMENT_ACTION.equals(action)){
            BeforeOnotificationsUtils.clearAllNotifications(context);
        }

    }

    private static void makeCigReminderWithNotification(Context ctx){
        BeforeOnotificationsUtils.cigReminderNotif(ctx);
    }


}
