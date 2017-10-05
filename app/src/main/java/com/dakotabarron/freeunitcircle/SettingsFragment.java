package com.dakotabarron.freeunitcircle;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Fragment which will contain the list of settings customizable
 * by the user.<br>
 * Created by dakota on 8/1/17.
 */
public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Android documentation recommends registering here
        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        // Android documentation recommends unregistering here
        getPreferenceScreen()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String s) {
        if (s.equals(getString(R.string.pref_dark_theme_key))){
            MainActivity.darkTheme = !MainActivity.darkTheme;
        } else if (s.equals(getString(R.string.pref_angle_sign_key))){
            String angleSignValue = sharedPreferences.getString(
                    s, getString(R.string.pref_angle_sign_value_all_positive));

            if (angleSignValue.equals(getString(
                    R.string.pref_angle_sign_value_all_positive))){
                MainActivity.signSetting = MainActivity.AngleSign.ALL_POS;
            } else if (angleSignValue.equals(getString(
                    R.string.pref_angle_sign_value_split))){
                MainActivity.signSetting = MainActivity.AngleSign.SPLIT;
            } else { // must be all negative
                MainActivity.signSetting = MainActivity.AngleSign.ALL_NEG;
            }
        }
    }
}
