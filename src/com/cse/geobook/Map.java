package com.cse.geobook;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity {

	// GoogleMap gMap;
	static MapView mapView;
	static GoogleMap gMap;
	Bundle extras;
	static ArrayList<LatLng> caches;
	LatLng target;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		//mapView = ((MapView) findViewById(R.id.map));
		gMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		//gMap = (mapView.getMap();
		getExtras();
		setUpMap();

	}

	private void getExtras() {
		extras = getIntent().getExtras();
		caches = extras.getParcelableArrayList("caches");
		target = extras.getParcelable("target");

	}

	static private void addMarker(LatLng location) {
		Marker m = gMap.addMarker(new MarkerOptions().position(location));
		m.setTitle(""); // or add in MarkerOptions
		m.setSnippet("");
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
			startActivity(new Intent(Map.this, Profile.class));
			return true;
		case R.id.menu_map_settings:
			return true;
		case R.id.menu_map_about:
			AlertDialog.Builder builder = new AlertDialog.Builder(Map.this);
			builder.setMessage(R.string.dialog_about_message).setTitle(
					R.string.dialog_about_title);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User clicked OK button
						}
					});

			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		case R.id.menu_map_signout:
			//
			finish();
			startActivity(new Intent(Map.this, Login.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setUpMap() {

		gMap.setOnMarkerClickListener(new markerClickListener());
		gMap.setOnMapClickListener(new clickListener());
		gMap.setOnMapLongClickListener(new longClickListener());
		gMap.setMyLocationEnabled(true);
		/*
		 * set target & zoom. if target is passed use that, else use default of
		 * columbus ohio
		 */
		if (target != null) {
			gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, 11));
		} else {
			gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					39.961138, -83.001465), 11));
		}
		// set markers
		if (caches != null) {
			for (int i = 0; i < caches.size(); i++) {
				addMarker(caches.get(i));
			}
		}

	}

	public void setUpView() {

	}

	class clickListener implements OnMapClickListener {

		@Override
		public void onMapClick(LatLng arg0) {
			// TODO Auto-generated method stub

		}

	}

	class longClickListener implements OnMapLongClickListener {
		@Override
		public void onMapLongClick(LatLng cache) {
			caches.add(cache);
			AlertDialog.Builder builder = new AlertDialog.Builder(Map.this);
			builder.setMessage(R.string.confirm_new_cache)
					.setTitle(R.string.new_cache)
					.setPositiveButton(R.string.cache_option_yes,
							new dialogListener())

					.setNegativeButton(R.string.cache_option_no,
							new dialogListener());

			AlertDialog dialog = builder.create();
			dialog.show();

		}

	}

	class markerClickListener implements OnMarkerClickListener {
		@Override
		public boolean onMarkerClick(Marker m) {
			// TODO Auto-generated method stub
			if (m.isInfoWindowShown()) {
				m.hideInfoWindow();
			} else {
				m.showInfoWindow();
			}

			return false;
		}

	}

	class viewClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}

	}

	class viewLongClickListener implements View.OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	class dialogListener implements DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				addMarker(caches.get(caches.size() - 1));

				break;
			case DialogInterface.BUTTON_NEUTRAL:
				caches.remove(caches.size() - 1);
				break;
			}
		}
	}

}
