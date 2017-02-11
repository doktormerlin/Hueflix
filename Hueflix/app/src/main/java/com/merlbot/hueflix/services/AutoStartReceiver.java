package com.merlbot.hueflix.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

/**
 * This class starts the Service as soon as a boot is completed.
 *
 * @author DoktorMerlin
 */

public class AutoStartReceiver extends BroadcastReceiver{

    private static final String TAG = "HueflixBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

            String enabled_notification_listeners = Settings.Secure.getString(context.getContentResolver(),"enabled_notification_listeners");

            //Check if the nofication listener is enabled, and start the service if it is.
            if(enabled_notification_listeners != null  && enabled_notification_listeners.contains(context.getPackageName())){
                Log.d(TAG,"Service Start");
                context.startService(new Intent(context, NetflixNotifyNewService.class));
            }
        }
    }

}