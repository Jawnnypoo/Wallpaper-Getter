package com.jawnnypoo.wallpapergetter.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jawnnypoo.wallpapergetter.R;
import com.jawnnypoo.wallpapergetter.data.Wallpaper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Our fragment for the main screen. Fragments were introduced as of Android
 * 3.0, and are summarized very well here: http://developer.android.com/guide/components/fragments.html
 * Created by Jawn on 9/9/2014.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private ViewPager mViewPager;
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager.setAdapter(new WallpaperPagerAdapter(getActivity(), getFakeImages()));
    }

    private ArrayList<Wallpaper> getFakeImages() {
        ArrayList<Wallpaper> wallpapers = new ArrayList<Wallpaper>();
        for (int i=0; i<10; i++) {
            wallpapers.add(new Wallpaper(i, "http://lorempixel.com/output/cats-q-c-640-480-5.jpg"));
        }
        return wallpapers;
    }

    private class WallpaperPagerAdapter extends PagerAdapter {
        ArrayList<Wallpaper> mWallpapers;
        Context mContext;
        LayoutInflater mLayoutInflater;

        public WallpaperPagerAdapter(Context context, ArrayList<Wallpaper> images) {
            mContext = context;
            mWallpapers = images;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        public void setImages(ArrayList<Wallpaper> images) {
            mWallpapers = images;
        }

        @Override
        public int getCount() {
            if (mWallpapers == null) {
                return 0;
            } else {
                return mWallpapers.size();
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mLayoutInflater.inflate(R.layout.item_wallpaper, container, false);
            Wallpaper wallpaper = mWallpapers.get(position);
            ImageView image = (ImageView) view.findViewById(R.id.wallpaper);
            Picasso.with(mContext).load(wallpaper.getUrl()).into(image);
            view.setTag(wallpaper);
            container.addView(view);
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
    }
}
