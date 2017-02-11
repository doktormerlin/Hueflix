package com.merlbot.hueflix.services;


import android.content.Intent;

import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.merlbot.hueflix.data.HueSharedPreferences;
import com.philips.lighting.hue.listener.PHSceneListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHScene;


import java.util.List;
import java.util.Map;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class provides the Service to read the Netflix Notifications. I haven't figured out yet,
 * how to actually read the media buttons, but it uses the workaround, that the notification gets updated 5 times
 * when hitting play and 3 times when hitting pause. There is a bit of false acceptance, but it's not enough to actually make
 * a difference.
 *
 * !!!IF ANYONE CAN HELP ME WITH READING THE ACTUAL BUTTONS, PLEASE HIT ME UP! I'D REALLY APPRECIATE IT!!!
 *
 * @author DoktorMerlin
 */

public class NetflixNotifyNewService extends NotificationListenerService {
    static int cnt = 1;
    private static String TAG = "HueflixNLS";
    HueSharedPreferences prefs;
    PHHueSDK phHueSDK;
    PHBridge bridge;


    static Timer timer;

    PHSceneListener sceneListener = new PHSceneListener() {
        @Override
        public void onScenesReceived(List<PHScene> list) {

        }

        @Override
        public void onSceneReceived(PHScene phScene) {

        }

        @Override
        public void onSuccess() {
        }

        @Override
        public void onError(int i, String s) {

        }

        @Override
        public void onStateUpdate(Map<String, String> map, List<PHHueError> list) {
            Log.w(TAG, "Scene has updated");
        }
    };

    @Override
    public void onCreate(){
        prefs = HueSharedPreferences.getInstance(getApplicationContext());
        phHueSDK = PHHueSDK.create();
        bridge = phHueSDK.getSelectedBridge();
        Log.i(TAG,"Bridge: " + bridge.toString());
        timer = new Timer("IsPlayingTimer");
        super.onCreate();
        bridge.activateScene(prefs.getPausedId(),"0",sceneListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();



        Log.i(TAG,"Package: " + pack);

        if(pack.equals("com.netflix.mediaclient")){

            if(cnt==1){
                Log.i(TAG,"Timer Start");
                timer.schedule(new myTimerTask(),600);
            }
            Log.i(TAG, "Count: " + Integer.toString(cnt++));
        }


    }

    private void setPlaying() {
        String playingId = prefs.getPlayingId();
        if(playingId!=null){
            Log.i(TAG, "PlayingID: " + playingId);
            bridge.activateScene(playingId,"0",sceneListener);
        }
        Log.i(TAG, "PLAY");
    }

    private void setPaused() {
        String pausedId = prefs.getPausedId();
        if(pausedId!=null){
            Log.i(TAG, "PausedID: " + pausedId);
            bridge.activateScene(pausedId,"0",sceneListener);
        }
        Log.d(TAG,"PAUSE");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"Notification Removed");

    }

    //Timer that checks how many times the notification got updated
    private class myTimerTask extends TimerTask {

        @Override
        public void run() {
            Log.i(TAG,"Im Timer");
            Log.i(TAG,"Count im Timer: " + Integer.toString(cnt));

            if(cnt==2){ //The "30 Seconds back" Button was pressed
                Log.i(TAG,"30 Sec");
                setPlaying();
            }
            else if(cnt>3){ //The "PLAY" Button was pressed
                setPlaying();
            }else{  //The "PAUSE" Button was pressed
                setPaused();
            }
            cnt=1;      //Reset the Counter for the next notification
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }



}
