package com.cse.geobook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Cache extends Activity {

	LinearLayout view;
	EditText cacheName;
	EditText description;
	LatLng position;
	public static String TARGET_LOC = "target_loc",
			TARGET_NAME = "target_name", CACHE_LOCATION = "cacheLocation",
			CACHE_TITLES = "cacheTitles", ZOOM = "zoom";

	// PHOTO

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cache);
		cacheName = (EditText) findViewById(R.id.cacheName);
		description = (EditText) findViewById(R.id.cacheDescription);
		getExtras();

	}

	private void getExtras() {
		Bundle extras = getIntent().getExtras();

		cacheName.setText((extras.getString("title")));
		description.setText((extras.getString("snippit")));
		position = (new LatLng(extras.getDouble("Lat"), extras.getDouble("Lng")));

	}

	static void setDataToPass(Bundle b, Marker marker) {

		b.putString("title", marker.getTitle());
		b.putString("snippit", marker.getSnippet());
		b.putDouble("Lat", marker.getPosition().latitude);
		b.putDouble("Lng", marker.getPosition().longitude);
	}
}
