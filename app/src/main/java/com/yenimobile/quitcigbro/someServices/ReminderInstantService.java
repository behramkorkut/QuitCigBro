package com.yenimobile.quitcigbro.someServices;

import android.app.IntentService;
import android.content.Intent;

import com.yenimobile.quitcigbro.syncPackage.ReminderTasks;

public class ReminderInstantService extends IntentService {


    public ReminderInstantService() {
        super("ReminderIntentService");
    }
    

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);
    }
}
