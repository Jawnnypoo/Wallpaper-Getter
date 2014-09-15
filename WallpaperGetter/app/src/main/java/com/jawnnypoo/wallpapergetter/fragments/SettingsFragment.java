package com.jawnnypoo.wallpapergetter.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.jawnnypoo.wallpapergetter.R;

/**
 * Created by Jawn on 9/14/2014.
 */
public class SettingsFragment extends Fragment {

    private Spinner mColorSpinner;

    public SettingsFragment() {
        //Empty contructor that we should not use
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mColorSpinner = (Spinner) view.findViewById(R.id.color_spinner);
    }
}
