package com.dakotabarron.freeunitcircle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity which launches when the user taps the "Settings" action button
 * in the app bar
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(
                android.R.id.content, new SettingsFragment()).commit();
    }
}
