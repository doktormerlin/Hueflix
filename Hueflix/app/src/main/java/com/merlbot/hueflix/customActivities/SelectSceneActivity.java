package com.merlbot.hueflix.customActivities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.merlbot.hueflix.R;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHScene;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the Activity to select a scene to play when pausing and when watching.
 *
 * @author DoktorMerlin
 */

public class SelectSceneActivity extends Activity {

    private PHHueSDK phHueSDK;
    private static final int MAX_HUE=65535;
    public static final String TAG = "Hueflix";

    private HashMap<String, String> nameToIDMap = new HashMap<>();

    private RadioGroup rg;
    private Button apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_scene);
        phHueSDK = PHHueSDK.create();

        rg = (RadioGroup) findViewById(R.id.radio_scenes_playing);
        apply = (Button) findViewById(R.id.button_apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rg.getCheckedRadioButtonId()!=-1){
                    int id= rg.getCheckedRadioButtonId();
                    View radioButton = rg.findViewById(id);
                    int radioId = rg.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) rg.getChildAt(radioId);
                    String selection = (String) btn.getText();

                    Intent result = new Intent();
                    result.putExtra("name",selection);
                    result.putExtra("id",nameToIDMap.get(selection));
                    setResult(Activity.RESULT_OK,result);
                    finish();
                }
            }
        });

        addScenes();
    }

    /**
     * adds the Scenes to the RadioGroup to Select
     */
    private void addScenes(){
        PHBridge bridge = phHueSDK.getSelectedBridge();
        List<PHScene> allScenes = bridge.getResourceCache().getAllScenes();
        for(PHScene scene : allScenes){
            nameToIDMap.put(scene.getName(),scene.getSceneIdentifier());
            RadioButton rb = new RadioButton(this);
            rb.setText(scene.getName());
            rg.addView(rb);
        }
    }
}
