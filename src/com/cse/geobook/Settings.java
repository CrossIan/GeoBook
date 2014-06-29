package com.cse.geobook;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}