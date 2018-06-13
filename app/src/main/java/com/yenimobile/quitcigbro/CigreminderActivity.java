package com.yenimobile.quitcigbro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yenimobile.quitcigbro.broadcastReceivers.ChargingBroadcatReceiver;
import com.yenimobile.quitcigbro.someServices.ReminderInstantService;
import com.yenimobile.quitcigbro.someUtilsPackage.BeforeOnotificationsUtils;
import com.yenimobile.quitcigbro.someUtilsPackage.ConnectionUtils;
import com.yenimobile.quitcigbro.someUtilsPackage.PreferencesUtils;
import com.yenimobile.quitcigbro.syncPackage.ReminderTasks;

public class CigreminderActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {



    private TextView mWaterCountDisplay;
    private TextView mChargingCountDisplay;
    private ImageView mChargingImageView;
    private FrameLayout mConnectivityBanner;

    private Toast mToast;

    private IntentFilter mChargingIntentFilter;
    private ChargingBroadcatReceiver mChargingBR;


    private IntentFilter mConnectedIntentFilter;
    private BroadcastReceiver mConnectivityBR;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cigreminder);

        mWaterCountDisplay = (TextView) findViewById(R.id.tv_water_count);
        mChargingCountDisplay = (TextView) findViewById(R.id.tv_charging_reminder_count);
        mChargingImageView = (ImageView) findViewById(R.id.iv_power_increment);
        mConnectivityBanner = findViewById(R.id.connectivity_banner);

        mChargingIntentFilter = new IntentFilter();
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        mChargingIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        mChargingBR = new ChargingBroadcatReceiver(mChargingImageView);


        mConnectedIntentFilter = new IntentFilter();
        mConnectedIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mConnectivityBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                NetworkInfo info = (NetworkInfo) extras.getParcelable("networkInfo");
                NetworkInfo.State state = info.getState();
                Log.d("TEST Internet", info.toString() + " "
                        + state.toString());

                if (state == NetworkInfo.State.CONNECTED) {
                    Toast.makeText(CigreminderActivity.this, "Internet connection is on", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Internet connection is Off", Toast.LENGTH_LONG).show();
                }

            }
        };






        /** Set the original values in the UI **/
        updateWaterCount();
        updateChargingReminderCount();


        /**
         * repeating job here
         */
        //ReminderUtils.scheduleReminder(this);



        /** Setup the shared preference listener **/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);


        mChargingCountDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BeforeOnotificationsUtils.remindUser(CigreminderActivity.this);
            }
        });
    }//end of onCreate



    /**
     * Updates the TextView to display the new water count from SharedPreferences
     */
    private void updateWaterCount() {
        int waterCount = PreferencesUtils.getWaterCount(this);
        mWaterCountDisplay.setText(waterCount+"");
    }

    /**
     * Updates the TextView to display the new charging reminder count from SharedPreferences
     */
    private void updateChargingReminderCount() {
        int chargingReminders = PreferencesUtils.getChargingReminderCount(this);
        String formattedChargingReminders = getResources().getQuantityString(
                R.plurals.charge_notification_count, chargingReminders, chargingReminders);
        mChargingCountDisplay.setText(formattedChargingReminders);

    }

    /**
     * Adds one to the water count and shows a toast
     */
    public void incrementWater(View view) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, R.string.water_chug_toast, Toast.LENGTH_SHORT);
        mToast.show();


        Intent incrementWaterCountIntent = new Intent(this, ReminderInstantService.class);

        incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);

        /** here starts the service // startService(incrementWaterCountIntent); **/

    }

    private void showCharging(boolean isCharging) {
        if (isCharging) {
            mChargingImageView.setImageResource(R.drawable.ic_power_pink_80px);

        } else {
            mChargingImageView.setImageResource(R.drawable.ic_power_gery_80px);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mChargingBR, mChargingIntentFilter);
        registerReceiver(mConnectivityBR, mConnectedIntentFilter);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //check the batteryLevel
            BatteryManager batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
            showCharging(batteryManager.isCharging());
        }


    }

    @Override
    protected void onPause() {
        unregisterReceiver(mChargingBR);
        unregisterReceiver(mConnectivityBR);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /** Cleanup the shared preference listener **/
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * This is a listener that will update the UI when the water count or charging reminder counts
     * change
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PreferencesUtils.KEY_WATER_COUNT.equals(key)) {
            updateWaterCount();
        } else if (PreferencesUtils.KEY_CHARGING_REMINDER_COUNT.equals(key)) {
            updateChargingReminderCount();
        }
    }


    private void showConnectivityBanner(){
        if(ConnectionUtils.isConnectedToInternet(this)){
            mConnectivityBanner.setVisibility(View.GONE);
        }else {
            mConnectivityBanner.setVisibility(View.VISIBLE);
        }
    }







}
