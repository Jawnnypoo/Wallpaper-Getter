package com.jawnnypoo.wallpapergetter;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.jawnnypoo.wallpapergetter.fragments.HomeFragment;

/**
 * Wow, its a pretty barren activity, isn't it? Most of the cool stuff is
 * found in the fragment it inflates into its view
 */
public class HomeActivity extends Activity implements SearchView.OnQueryTextListener{

    private static final String TAG = HomeActivity.class.getSimpleName();

    //Save a reference to our search menu item so that we can do things with it
    //later on
    private MenuItem mSearchMenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        mSearchMenuItem = menu.findItem(R.id.action_search);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                gotoSettings();
                break;
            case R.id.action_favorites:
                gotoFavorites();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Launch into the settings activity
     */
    private void gotoSettings() {
        Intent intent = SettingsActivity.newInstance(this);
        //Launch the activity!
        gotoActivity(intent);
    }

    private void gotoFavorites() {
        Intent intent = FavoritesActivity.newInstance(this);
        //Launch the activity!
        gotoActivity(intent);
    }

    /**
     * We have this method to sort of act as a wrapper for startActivity
     * so that we could perform other actions if we wanted to
     * @param intent
     */
    private void gotoActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //When the user hits submit, we want to collapse the search action
        mSearchMenuItem.collapseActionView();
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    /**
     * Here we handle the intent that we got from whatever has called us,
     * in our case it is the action search view, which handed off the
     * search terms to us
     * @param intent
     */
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "Search term from search view: " + query);
            //use the query to search your data somehow
        }
    }
}
