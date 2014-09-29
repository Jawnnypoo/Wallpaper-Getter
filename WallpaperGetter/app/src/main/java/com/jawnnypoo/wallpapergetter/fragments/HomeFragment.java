package com.jawnnypoo.wallpapergetter.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.jawnnypoo.wallpapergetter.R;
import com.jawnnypoo.wallpapergetter.data.FlickrImage;
import com.jawnnypoo.wallpapergetter.data.WallpaperGetterContentProvider;
import com.jawnnypoo.wallpapergetter.dialogs.PhotoDialog;
import com.jawnnypoo.wallpapergetter.loader.FlickrSearchLoader;
import com.jawnnypoo.wallpapergetter.services.ImageDownloadService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Our fragment for the main screen. Fragments were introduced as of Android
 * 3.0, and are summarized very well here: http://developer.android.com/guide/components/fragments.html
 * Created by Jawn on 9/9/2014.
 */
public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<FlickrImage>>, PhotoDialog.OnPhotoDialogListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private static final String TAG_PHOTO_DIALOG = "tag_photo_dialog";

    private static final int LOADER_SEARCH = 123;

    private static final String STATE_SEARCH_TERM = "state_search_term";
    private static final String STATE_PAGER_POSITION = "state_pager_position";

    private ViewPager mViewPager;
    private PhotoDialog mPhotoDialog;
    private String mSearchTerm;
    private int mViewPagerPosition = -1;
    /**
     * DON'T USE THIS CONSTRUCTOR. Use the class's newInstance method instead. It allows for
     * one point of entry. We just need this blank constructor to make certain Android utils happy
     */
    public HomeFragment() {
        //Empty contructor that we should not use
    }

    /**
     * We like to create fragments via a sort of factory method. This means that if the
     * fragment does require some sort of data to display properly, there is only one point of entry
     * where we will define the data it needs (in this method.) It is a standard from Google, and they
     * are decently smart guys.
     * @return fragment
     */
    public static HomeFragment newInstance() {
        Log.d(TAG, "NewInstance for HomeFragment");
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Similar to activities, onCreate is where we could perform first time initializations
     * and restore data upon rotation
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mSearchTerm = "cats";
            updateActionBarText("cats");
        } else {
            mSearchTerm = savedInstanceState.getString(STATE_SEARCH_TERM);
            mViewPagerPosition = savedInstanceState.getInt(STATE_PAGER_POSITION, -1);
        }
        mPhotoDialog = new PhotoDialog();
        mPhotoDialog.setPhotoListener(this);
    }

    /**
     * This is where you will inflate the layout that you want to show for this fragment,
     * comparable to setContent in Activity
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * This is the best place for finding views that we have created in XML, and linking them back to our Java code
     * The view has been fully created and is ready to link references
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new WallpaperPagerAdapter(getActivity(), null));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //init loader will initialize and start the loader if the loader has not
        //already been run, otherwise, it will return right away in onLoadFinished
        getLoaderManager().initLoader(LOADER_SEARCH, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_SEARCH_TERM, mSearchTerm);
        if (mViewPager != null) {
            outState.putInt(STATE_PAGER_POSITION, mViewPager.getCurrentItem());
        }
    }

    public void updateSearch(String searchTerm) {
        mSearchTerm = searchTerm;
        updateActionBarText(searchTerm);
        mViewPagerPosition = 0;
        getLoaderManager().restartLoader(LOADER_SEARCH, null, this);
    }

    private void updateActionBarText(String text) {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle(getActivity().getString(R.string.searching) + " \"" + text + "\"");
    }

    @Override
    public Loader<ArrayList<FlickrImage>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_SEARCH:
                FlickrSearchLoader searchLoader = new FlickrSearchLoader(getActivity(), mSearchTerm);
                searchLoader.forceLoad();
                return searchLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FlickrImage>> loader, ArrayList<FlickrImage> data) {
        switch (loader.getId()) {
            case LOADER_SEARCH:
                Log.d(TAG, "onLoadFinished");
                if (data != null && data.size() > 0) {
                    ((WallpaperPagerAdapter) mViewPager.getAdapter()).setImages(data);
                    if (mViewPagerPosition != -1 && mViewPagerPosition < data.size()) {
                        Log.d(TAG, "Moving current position to " + mViewPagerPosition);
                        mViewPager.setCurrentItem(mViewPagerPosition);
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FlickrImage>> loader) { }

    @Override
    public void onAddToFavorites() {
        FlickrImage selectedImage = ((WallpaperPagerAdapter)mViewPager.getAdapter()).getSelectedImage();
        new AddToFavoritesTask(getActivity()).execute(selectedImage);
    }

    @Override
    public void onDownload() {
        Log.v(TAG, "onDownload");
        FlickrImage selectedImage = ((WallpaperPagerAdapter)mViewPager.getAdapter()).getSelectedImage();
        getActivity().startService(ImageDownloadService.newInstance(getActivity(), selectedImage));
    }

    private class WallpaperPagerAdapter extends PagerAdapter {
        ArrayList<FlickrImage> mImages;
        Context mContext;
        LayoutInflater mLayoutInflater;
        private FlickrImage mSelectedImage;

        public WallpaperPagerAdapter(Context context, ArrayList<FlickrImage> images) {
            mContext = context;
            mImages = images;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        public void setImages(ArrayList<FlickrImage> images) {
            mImages = images;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mImages == null) {
                return 0;
            } else {
                return mImages.size();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mLayoutInflater.inflate(R.layout.item_wallpaper, container, false);
            final FlickrImage wallpaper = mImages.get(position);
            ImageView image = (ImageView) view.findViewById(R.id.wallpaper);
            Log.v(TAG, "Loading image url: " + wallpaper.getUrl());
            Picasso.with(mContext).load(wallpaper.getUrl()).into(image);
            view.setTag(wallpaper);
            container.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedImage = wallpaper;
                    mPhotoDialog.show(getFragmentManager(), TAG_PHOTO_DIALOG);
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public FlickrImage getSelectedImage() {
            return mSelectedImage;
        }
    }

    /**
     * Simple async task we use to store favorites in the database on a background thread
     */
    private static class AddToFavoritesTask extends AsyncTask<FlickrImage, Integer, Uri> {

        private Context mContext;

        public AddToFavoritesTask(Context context) {
            mContext = context;
        }

        protected Uri doInBackground(FlickrImage... entries) {
            FlickrImage image = entries[0];

            ContentValues newValues = new ContentValues();

            // Assign values for each row.
            newValues.put(WallpaperGetterContentProvider.KEY_COLUMN_JSON,
                    image.getRawJSON());
            // [ ... Repeat for each column / value pair ... similar to shared preferences ]

            // Get the Content Resolver
            ContentResolver cr = mContext.getContentResolver();

            // Insert the row into your table
            return cr.insert(WallpaperGetterContentProvider.CONTENT_URI,
                    newValues);
        }

        protected void onProgressUpdate(Integer... progress) {
            //We do not track progress and show it to the user, since it will happen so quickly
        }

        protected void onPostExecute(Uri result) {
            if (result != null) {
                Toast.makeText(mContext, mContext.getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.failed_to_add_to_favorites), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
