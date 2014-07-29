package com.cse.geobook;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Settings extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
		
	}
	
	/**
	 * Returns a string that corresponds to a color that will be used by Map
	 */
	public static String getColorMarker(Context context){
		//Default value of 1, which corresponds to the default color value of "azure" to contrast with the red
		return PreferenceManager.getDefaultSharedPreferences(context).getString("pref_pinColor", "1");
		
	}
}