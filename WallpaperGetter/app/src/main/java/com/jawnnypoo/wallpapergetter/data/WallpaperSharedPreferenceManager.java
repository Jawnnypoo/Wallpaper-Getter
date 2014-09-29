package com.jawnnypoo.wallpapergetter.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

/**
 * Manager class that helps to access shared preferences in a way
 * that keeps all the defaults and key/values in one place. Kinda like
 * a database helper in a way. But much simpler
 * Created by John on 9/29/2014.
 */
public class WallpaperSharedPreferenceManager {
    private static final String TAG = WallpaperSharedPreferenceManager.class.getSimpleName();

    private static final String PREFERENCE_FILE = "wallpaperSharedPreferences";
    private static final String KEY_COLOR_PREFERENCE = "key_color_preference";

    private static final int DEFAULT_COLOR_PREFERENCE = Color.WHITE;

    public static int getColorPreference(Context context) {
        return getSharedPreferences(context).getInt(KEY_COLOR_PREFERENCE, DEFAULT_COLOR_PREFERENCE);
    }

    public static void setColorPreference(Context context, int colorPreference) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(KEY_COLOR_PREFERENCE, colorPreference);
        editor.commit();
        Log.v(TAG, "Setting the color preference to: " + colorPreference);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
    }
}
