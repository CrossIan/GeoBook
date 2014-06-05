package com.cse.geobook;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class Map extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	}
	
	
	
	
	/*
	 * Populates the map view menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_map, menu);
		return true;
	}

	
	
	
	/*
	 * Responds to touch events
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
		    case R.id.menu_map_profile:
	            return true;
		    case R.id.menu_map_settings:
	            return true;
	        case R.id.menu_map_about:
	        	AlertDialog.Builder builder = new AlertDialog.Builder(Map.this);
	        	builder.setMessage(R.string.dialog_about_message)
	        	       .setTitle(R.string.dialog_about_title);
	        	builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    // User clicked OK button
	                }
	            });

	        	AlertDialog dialog = builder.create();
	        	dialog.show();
	            return true;
	        case R.id.menu_map_signout:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
