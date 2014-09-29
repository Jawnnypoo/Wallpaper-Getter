package com.jawnnypoo.wallpapergetter.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.jawnnypoo.wallpapergetter.R;
import com.jawnnypoo.wallpapergetter.data.WallpaperSharedPreferenceManager;

/**
 * Created by Jawn on 9/14/2014.
 */
public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private Spinner mColorSpinner;

    private String[] mColorLogic;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mColorLogic = getResources().getStringArray(R.array.background_colors_logic);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mColorSpinner = (Spinner) view.findViewById(R.id.color_spinner);
        mColorSpinner.setOnItemSelectedListener(this);
        updateUiBasedOnSettings();
    }

    private void updateUiBasedOnSettings() {
        int currentlySelectedColor = WallpaperSharedPreferenceManager.getColorPreference(getActivity());
        for (int i=0; i<mColorLogic.length; i++) {
            if (Color.parseColor(mColorLogic[i]) == currentlySelectedColor) {
                mColorSpinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String chosenColor = mColorLogic[position];
        WallpaperSharedPreferenceManager.setColorPreference(getActivity(), Color.parseColor(chosenColor));
        getActivity().setResult(Activity.RESULT_OK);
    }
}
