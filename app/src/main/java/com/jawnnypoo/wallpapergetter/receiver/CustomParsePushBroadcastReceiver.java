package com.jawnnypoo.wallpapergetter.receiver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.jawnnypoo.wallpapergetter.data.WallpaperSharedPreferenceManager;
import com.parse.ParsePushBroadcastReceiver;

/**
 * This receiver will kick off if we get a push from Parse that has an action specified in the manifest.
 * We specify the push action by sending a JSON push from the Parse console. We would send JSON
 * like this to kick of the onReceive in this class:
 *  {
 *      "action": "com.jawnnypoo.UPDATE"
 *  }
 * Created by John on 10/5/2014.
 */
public class CustomParsePushBroadcastReceiver extends ParsePushBroadcastReceiver {

    private static final String TAG = CustomParsePushBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //This is a terrible example, but it shows the power of what
        //kind of actions you could perform with using parse
        Log.d(TAG, "Received push to perform custom action");
        WallpaperSharedPreferenceManager.setColorPreference(context, Color.CYAN);
    }
}
