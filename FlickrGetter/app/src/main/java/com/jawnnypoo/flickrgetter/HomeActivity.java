package com.jawnnypoo.flickrgetter;

import android.app.Activity;
import android.os.Bundle;

import com.jawnnypoo.flickrgetter.fragments.HomeFragment;

/**
 * Wow, its a pretty barren activity, isn't it? Most of the cool stuff is
 * found in the fragment it inflates into its view
 */
public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        //Keep in mind, saved instance state is null only once, when we first start the activity. So,
        //here we perform all of the actions only once.
        if (savedInstanceState == null) {
            HomeFragment homeFragment = HomeFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_root, homeFragment)
                    .commit();
        }
    }


}
