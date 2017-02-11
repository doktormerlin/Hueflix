package com.merlbot.hueflix;

import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.merlbot.hueflix.customActivities.SelectSceneActivity;
import com.merlbot.hueflix.data.HueSharedPreferences;
import com.merlbot.hueflix.services.NetflixNotifyNewService;
import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.listener.PHSceneListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import com.philips.lighting.model.PHScene;

/**
 * This class provides the MainActivity for the app.
 *
 * @author DoktorMerlin
 *
 */
public class MainActivity extends Activity {
    private PHHueSDK phHueSDK;
    private HueSharedPreferences prefs;
    private static final int MAX_HUE=65535;
    public static final String TAG = "Hueflix";
    private static final int BACK_FROM_SCENE_PLAYING = 0;
    private static final int BACK_FROM_SCENE_PAUSED = 1;
    private String playingSelectedName = "none";
    private String playingSelectedID="0";
    private String pausedSelectedName = "none";
    private String pausedSelectedID="0";

    private Button playing;
    private Button paused;
    private TextView selectedPlaying;
    private TextView selectedPaused;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_main);
        phHueSDK = PHHueSDK.create();
        prefs = HueSharedPreferences.getInstance(getApplicationContext());

        playingSelectedName = prefs.getPlayingName();
        playingSelectedID = prefs.getPlayingId();
        pausedSelectedName = prefs.getPausedName();
        pausedSelectedID = prefs.getPausedId();

        playing = (Button) findViewById(R.id.button_playing);
        paused = (Button) findViewById(R.id.button_paused);
        selectedPaused = (TextView) findViewById(R.id.text_selected_paused);
        selectedPlaying = (TextView) findViewById(R.id.text_selected_playing);

        selectedPaused.setText(String.format(getResources().getString(R.string.currently_selected),pausedSelectedName));
        selectedPlaying.setText(String.format(getResources().getString(R.string.currently_selected),playingSelectedName));

        String enabled_notification_listeners = Settings.Secure.getString(this.getContentResolver(),"enabled_notification_listeners");

        //Check if the nofication listener is enabled, if not show a warning and a button to enable it.
        if(enabled_notification_listeners == null  || !enabled_notification_listeners.contains(getApplicationContext().getPackageName())){
            ImageView imgWarning = (ImageView) findViewById(R.id.img_warning);
            imgWarning.setVisibility(View.VISIBLE);

            TextView txtWarning = (TextView) findViewById(R.id.text_settings_label);
            txtWarning.setVisibility(View.VISIBLE);

            Button btnWarning = (Button) findViewById(R.id.button_enable_notification_access);
            btnWarning.setVisibility(View.VISIBLE);
            btnWarning.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                }
            });
        }else {
            Log.d(TAG,"Service Start");
            startService(new Intent(getApplicationContext(), NetflixNotifyNewService.class));
        }

        playing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectSceneActivity.class);
                startActivityForResult(intent,BACK_FROM_SCENE_PLAYING);
            }
        });

        paused.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectSceneActivity.class);
                startActivityForResult(intent,BACK_FROM_SCENE_PAUSED);
            }
        });




    }

    @Override
    protected void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {

            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }

            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case BACK_FROM_SCENE_PLAYING:
                if(resultCode == Activity.RESULT_OK){
                    playingSelectedName = data.getStringExtra("name");
                    playingSelectedID = data.getStringExtra("id");
                    prefs.setPlayingId(playingSelectedID);
                    prefs.setPlayingName(playingSelectedName);
                    selectedPlaying.setText(String.format(getResources().getString(R.string.currently_selected),playingSelectedName));
                }
                break;
            case BACK_FROM_SCENE_PAUSED:
                if(resultCode == Activity.RESULT_OK){
                    pausedSelectedName = data.getStringExtra("name");
                    pausedSelectedID = data.getStringExtra("id");
                    prefs.setPausedId(pausedSelectedID);
                    prefs.setPausedName(pausedSelectedName);
                    selectedPaused.setText(String.format(getResources().getString(R.string.currently_selected),pausedSelectedName));
                }
                break;
        }
    }
}
