package com.jawnnypoo.wallpapergetter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.jawnnypoo.wallpapergetter.fragments.SettingsFragment;

/**
 * Created by Jawn on 9/14/2014.
 */
public class SettingsActivity extends Activity {

    /**
     * We use the newInstance pattern with Activities, as we do for fragments,
     * for much of the same reasons, so that we can have tighter control over
     * what values are passed to the activities and to make sure that we never
     * call the activity without essential values
     * @param context
     * @return
     */
    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        //If we wanted to pass values to the activity, we would go about it by
        //placing values in the intent, see below:
        //intent.putExtra("Key", "value");
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        //If we had passed values in the intent to this activity, we could get them here, as shown:
        //getIntent().getStringExtra("Key");
        if (savedInstanceState == null) {
            SettingsFragment settingsFragment = SettingsFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_root, settingsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle home (actionbar back arrow) presses here
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
