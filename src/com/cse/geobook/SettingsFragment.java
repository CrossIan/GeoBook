package com.cse.geobook;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//Loads the settings layout from the respective XML
		addPreferencesFromResource(R.xml.settings);
	}
}
