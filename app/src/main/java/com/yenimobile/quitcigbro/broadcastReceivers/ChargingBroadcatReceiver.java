package com.yenimobile.quitcigbro.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.yenimobile.quitcigbro.R;

public class ChargingBroadcatReceiver extends BroadcastReceiver {

    private ImageView mChargingImageView;

    public ChargingBroadcatReceiver(ImageView mChargingImageView) {
        this.mChargingImageView = mChargingImageView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        boolean isCharging = action.equals(Intent.ACTION_POWER_CONNECTED);
        showCharging(isCharging);

    }

    private void showCharging(boolean isCharging){
        if (isCharging){
            mChargingImageView.setImageResource(R.drawable.ic_power_pink_80px);
        }else {
            mChargingImageView.setImageResource(R.drawable.ic_power_gery_80px);
        }
    }
}
