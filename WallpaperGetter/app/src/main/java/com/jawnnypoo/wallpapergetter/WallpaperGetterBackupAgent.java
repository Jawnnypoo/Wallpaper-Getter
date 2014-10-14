package com.jawnnypoo.wallpapergetter;

import android.app.backup.BackupAgentHelper;
import android.app.backup.SharedPreferencesBackupHelper;

import com.jawnnypoo.wallpapergetter.data.WallpaperSharedPreferenceManager;


/**
 * Created by Administrator on 10/13/2014.
 */
public class WallpaperGetterBackupAgent extends BackupAgentHelper{

    private static final String BACKUP_KEY = "com.jawnnypoo.wallpapergetter.BACKUP";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesBackupHelper helper =
                new SharedPreferencesBackupHelper(getApplicationContext(), WallpaperSharedPreferenceManager.PREFERENCE_FILE);
        addHelper(BACKUP_KEY, helper);
    }
}
