package com.jawnnypoo.wallpapergetter.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.jawnnypoo.wallpapergetter.R;
import com.jawnnypoo.wallpapergetter.data.FlickrImage;
import com.jawnnypoo.wallpapergetter.data.WallpaperGetterContentProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Jawn on 9/14/2014.
 */
public class FavoritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = FavoritesFragment.class.getSimpleName();
    private static final int LOADER_FAVORITES = 42;
    private ListView mFavoritesList;

    public FavoritesFragment() {
        //Empty contructor that we should not use
    }

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFavoritesList = (ListView) view.findViewById(R.id.listview_favorites);
        mFavoritesList.setAdapter(new FavoritesAdapter(getActivity(), null));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_FAVORITES, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Construct the new query in the form of a Cursor Loader. Use the id
        // parameter to construct and return different loaders.
        switch (id) {
            case LOADER_FAVORITES:
                String[] projection = null;
                String where = null;
                String[] whereArgs = null;
                String sortOrder = null;

                // Query URI
                Uri queryUri = WallpaperGetterContentProvider.CONTENT_URI;

                // Create the new Cursor loader.
                return new CursorLoader(getActivity(), queryUri,
                        projection, where, whereArgs, sortOrder);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case LOADER_FAVORITES:
                //Turn the cursor into an ArrayList of FlickrImages
                ArrayList<FlickrImage> images = new ArrayList<FlickrImage>();
                if (data != null && data.getCount() > 0) {
                    //Iterate on all the entries we got back
                    while (data.moveToNext()) {
                        FlickrImage image = FlickrImage.fromCursor(data);
                        images.add(image);
                    }
                    if (!data.isClosed()) {
                        data.close();
                    }
                }
                ((FavoritesAdapter)mFavoritesList.getAdapter()).setImages(images);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private static class FavoritesAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private int mLastPosition = -1;
        private ArrayList<FlickrImage> mImages;

        public FavoritesAdapter(Context context, ArrayList<FlickrImage> images) {
            mContext = context;
            mImages = images;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public FlickrImage getItem(int position) {
            return mImages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
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
        public View getView(int position, View convertView, ViewGroup parent) {

            Holder holder;
            if (convertView != null) {
                holder = (Holder) convertView.getTag();
            } else {
                convertView = mLayoutInflater.inflate(R.layout.item_wallpaper, parent, false);
                holder = new Holder();
                holder.image = (ImageView) convertView.findViewById(R.id.wallpaper);
                convertView.setTag(holder);
            }

            FlickrImage image = getItem(position);
            Picasso.with(mContext).load(image.getUrl()).into(holder.image);

            Animation animation = AnimationUtils.loadAnimation(mContext,
                    (position > mLastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            convertView.startAnimation(animation);
            mLastPosition = position;

            return convertView;
        }

        public void setImages(ArrayList<FlickrImage> images) {
            mImages = images;
            notifyDataSetChanged();
        }

        private class Holder {
            ImageView image;
        }
    }
}
