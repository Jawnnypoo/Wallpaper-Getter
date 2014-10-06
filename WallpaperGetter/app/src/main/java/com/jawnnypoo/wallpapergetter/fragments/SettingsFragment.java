package com.jawnnypoo.wallpapergetter.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jawnnypoo.wallpapergetter.R;
import com.jawnnypoo.wallpapergetter.WallpaperGetterApplication;
import com.jawnnypoo.wallpapergetter.data.WallpaperSharedPreferenceManager;

/**
 * Created by Jawn on 9/14/2014.
 */
public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private Spinner mColorSpinner;
    private EditText mChannels;

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
        mChannels = (EditText) view.findViewById(R.id.channels);
        mColorSpinner.setOnItemSelectedListener(this);
        mChannels.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do your stuff here
                    addChannel();
                }
                return false;
            }
        });
        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChannel();
            }
        });
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

    private void addChannel() {
        ((WallpaperGetterApplication)getActivity().getApplication())
                .subscribeToChannel(mChannels.getText().toString());

        String message = String.format(getString(R.string.subscribed_to_channel), mChannels.getText().toString());
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
