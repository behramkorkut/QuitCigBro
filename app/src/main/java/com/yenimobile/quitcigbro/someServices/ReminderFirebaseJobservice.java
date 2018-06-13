package com.yenimobile.quitcigbro.someServices;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.yenimobile.quitcigbro.syncPackage.ReminderTasks;


public class ReminderFirebaseJobservice extends JobService {

    private AsyncTask mBackGroundTask;


    @Override
    public boolean onStartJob(final JobParameters jobParams) {
        mBackGroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = ReminderFirebaseJobservice.this;
                ReminderTasks.executeTask(context, ReminderTasks.CHARGING_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParams, false);
            }
        };

        mBackGroundTask.execute();
        Log.e("ReminderFirebase", "the background task is executed");
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters job) {
        if(mBackGroundTask != null) mBackGroundTask.cancel(true);
        return true;
    }



}
