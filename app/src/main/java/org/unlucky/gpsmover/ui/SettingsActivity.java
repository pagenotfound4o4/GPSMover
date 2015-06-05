package org.unlucky.gpsmover.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import org.unlucky.gpsmover.R;

public class SettingsActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
