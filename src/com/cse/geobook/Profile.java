package com.cse.geobook;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

public class Profile extends Activity {

	ProgressBar progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		progress = (ProgressBar) findViewById(R.id.cacheprogbar);

		this.setProgress(35, 100);
		
	}
	private void setProgress(int progress, int max) {
		this.progress.setProgress(progress);
		this.progress.setMax(max);
	}
}
