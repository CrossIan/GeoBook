package com.cse.geobook;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//addPreferencesFromResource(R.xml.settings);
		setContentView(R.layout.activity_settings);
	}
}