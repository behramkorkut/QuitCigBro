package com.yenimobile.quitcigbro.someServices;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.yenimobile.quitcigbro.syncPackage.ReminderTasks;

public class CigaretteFirebaseJobService extends JobService  {

    private AsyncTask mBackgroundTask;



    @Override
    public boolean onStartJob(final JobParameters jobparams) {
        Log.e("cigJobService", "entered cigaretteFirebaseJobService");
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = CigaretteFirebaseJobService.this;
                ReminderTasks.executeCigTask(context, ReminderTasks.CIG_REMINDER_NOTIF);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobparams, false);
            }
        };

        mBackgroundTask.execute();
        Log.e("cigJobservice", "mBackgroundTask is executed");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}
