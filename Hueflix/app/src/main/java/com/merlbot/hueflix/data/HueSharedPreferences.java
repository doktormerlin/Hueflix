package com.merlbot.hueflix.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class HueSharedPreferences {
    private static final String HUE_SHARED_PREFERENCES_STORE = "HueSharedPrefs";
    private static final String LAST_CONNECTED_USERNAME      = "LastConnectedUsername";
    private static final String LAST_CONNECTED_IP            = "LastConnectedIP";
    private static final String PLAYING_NAME                 = "PlayingName";
    private static final String PLAYING_ID                   = "PlayingID";
    private static final String PAUSED_NAME                  = "PausedName";
    private static final String PAUSED_ID                    = "PausedID";
    private static HueSharedPreferences instance = null;
    private SharedPreferences mSharedPreferences = null;
    
    private Editor mSharedPreferencesEditor = null;
    
    
    public void create() {
      
    }
    
    public static HueSharedPreferences getInstance(Context ctx) {
        if (instance == null) {
            instance = new HueSharedPreferences(ctx);
        }
        return instance;
    }
    
    private HueSharedPreferences(Context appContext) {
        mSharedPreferences = appContext.getSharedPreferences(HUE_SHARED_PREFERENCES_STORE, 0); // 0 - for private mode
        mSharedPreferencesEditor = mSharedPreferences.edit();
    }
    
    
    public String getUsername() {
         String username = mSharedPreferences.getString(LAST_CONNECTED_USERNAME, "");
    	 return username;
	}

	public boolean setUsername(String username) {
        mSharedPreferencesEditor.putString(LAST_CONNECTED_USERNAME, username);
        return (mSharedPreferencesEditor.commit());
	}
    
    public String getLastConnectedIPAddress() {
        return mSharedPreferences.getString(LAST_CONNECTED_IP, "");
    }

    public boolean setLastConnectedIPAddress(String ipAddress) {
       mSharedPreferencesEditor.putString(LAST_CONNECTED_IP, ipAddress);
       return (mSharedPreferencesEditor.commit());
    }

    public String getPlayingName() {
        return mSharedPreferences.getString(PLAYING_NAME, "none");
    }

    public boolean setPlayingName(String name) {
        mSharedPreferencesEditor.putString(PLAYING_NAME, name);
        return(mSharedPreferencesEditor.commit());
    }

    public String getPlayingId(){
        return mSharedPreferences.getString(PLAYING_ID,"");
    }

    public boolean setPlayingId(String id) {
        mSharedPreferencesEditor.putString(PLAYING_ID,id);
        return mSharedPreferencesEditor.commit();
    }

    public String getPausedName(){
        return mSharedPreferences.getString(PAUSED_NAME,"none");
    }

    public boolean setPausedName(String name){
        mSharedPreferencesEditor.putString(PAUSED_NAME,name);
        return mSharedPreferencesEditor.commit();
    }

    public String getPausedId(){
        return mSharedPreferences.getString(PAUSED_ID,"");
    }

    public boolean setPausedId(String id){
        mSharedPreferencesEditor.putString(PAUSED_ID, id);
        return mSharedPreferencesEditor.commit();
    }
}
