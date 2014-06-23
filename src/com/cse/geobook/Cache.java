package com.cse.geobook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Cache extends Activity {

	LinearLayout view;
	EditText cacheName;
	EditText description;
	LatLng position;
	MarkerOptions mo;

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
		Data data = extras.getParcelable(Data.CACHE_DATA);
		mo = data.target;
		cacheName.setText(mo.getTitle());
		description.setText(mo.getSnippet());
		position = mo.getPosition();

	}

	static void setDataToPass(Bundle b, Marker marker) {

		b.putString("title", marker.getTitle());
		b.putString("snippit", marker.getSnippet());
		b.putDouble("Lat", marker.getPosition().latitude);
		b.putDouble("Lng", marker.getPosition().longitude);
	}
}
