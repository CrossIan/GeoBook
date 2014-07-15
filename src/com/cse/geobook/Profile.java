package com.cse.geobook;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;


public class Profile extends Activity {

	public String userName;
	ProgressBar progress;
	TextView profileUserName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		progress = (ProgressBar) findViewById(R.id.cacheprogbar);
		
		profileUserName = (TextView) this.findViewById(R.id.usrname);
		userName = Settings.getProfileName(getApplicationContext());
		profileUserName.setText(userName);

		this.setProgress(35, 100);
		
	}
	private void setProgress(int progress, int max) {
		this.progress.setProgress(progress);
		this.progress.setMax(max);
	}
}
