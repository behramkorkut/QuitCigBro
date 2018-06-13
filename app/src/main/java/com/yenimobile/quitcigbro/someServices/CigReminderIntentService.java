package com.yenimobile.quitcigbro.someServices;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.yenimobile.quitcigbro.syncPackage.ReminderTasks;

public class CigReminderIntentService extends IntentService {


    public CigReminderIntentService() {
        super("cigReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeCigTask(this, action);
    }

    public void stopLeserviceIllico(){
        stopSelf();
    }
}
