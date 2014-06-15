package com.cse.geobook;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity {

	// GoogleMap gMap;

	GoogleMap gMap;
	Bundle extras;
	static ArrayList<LatLng> caches;
	LatLng target;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		setUpMap();

	}

	private void getExtras() {
		extras = getIntent().getExtras();
		caches = extras.getParcelableArrayList("caches");
		target = extras.getParcelable("target");
	}

	private void addMarker(LatLng location) {
		Marker m = this.gMap.addMarker(new MarkerOptions().position(location));
		m.setTitle("test"); // or add in MarkerOptions
		m.setSnippet("more test");
	}

	private void setUpActionListeners() {
		clickListener listener = new clickListener();
		gMap.setOnMarkerClickListener(listener);
		gMap.setOnMapClickListener(listener);
		gMap.setOnMapLongClickListener(listener);
		gMap.setOnInfoWindowClickListener(listener);
	}

	private void setUpMap() {
		if (gMap == null) {
			gMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}
		this.setUpActionListeners();
		getExtras();

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

	class clickListener implements OnMapClickListener, OnMapLongClickListener,
			OnMarkerClickListener, OnInfoWindowClickListener {

		@Override
		public void onMapClick(LatLng arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMapLongClick(LatLng cache) {

			/** dialog click listener ONLY for the below alert dialog box */
			class dialogClickListener implements
					DialogInterface.OnClickListener {

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
			dialogClickListener listener = new dialogClickListener();
			caches.add(cache);
			AlertDialog.Builder builder = new AlertDialog.Builder(Map.this);
			builder.setMessage(R.string.confirm_new_cache)
					.setTitle(R.string.new_cache)
					.setPositiveButton(R.string.cache_option_yes, listener)

					.setNegativeButton(R.string.cache_option_no, listener);

			AlertDialog dialog = builder.create();
			dialog.show();

		}

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

		@Override
		public void onInfoWindowClick(Marker arg0) {

			/** dialog click listener ONLY for the below alert dialog box */
			class dialogClickListener implements
					DialogInterface.OnClickListener {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						// TODO: goto cache view

						break;
					case DialogInterface.BUTTON_NEUTRAL:
						break;
					}
				}
			}
			dialogClickListener listener = new dialogClickListener();
			AlertDialog.Builder builder = new AlertDialog.Builder(Map.this);
			builder.setMessage(R.string.go_to_cache_message_ad)
					.setTitle(R.string.view_cache_title_ad)
					.setPositiveButton(R.string.cache_option_yes, listener)
					.setNegativeButton(R.string.cache_option_no, listener);

			AlertDialog dialog = builder.create();
			dialog.show();

		}
	}

	static void setDataToPass(Bundle b, Context c) {
		// target
		b.putParcelable("target", new LatLng(39.961138, -83.001465));
		// markers (caches)
		ArrayList<LatLng> data = new ArrayList<LatLng>();

		DataParser reader = new DataParser(c, R.raw.test_data);
		/*
		 * data.add(new LatLng(39.901138, -82.951465)); data.add(new
		 * LatLng(39.901138, -83.001465)); data.add(new LatLng(39.901138,
		 * -83.051465)); data.add(new LatLng(39.961138, -82.951465));
		 * data.add(new LatLng(39.961138, -83.001465)); data.add(new
		 * LatLng(39.961138, -83.051465)); data.add(new LatLng(39.991138,
		 * -82.951465)); data.add(new LatLng(39.991138, -83.001465));
		 * data.add(new LatLng(39.991138, -83.051465));
		 */
		for (int i = 0; i < 25; i++) {
			data.add(new LatLng(reader.getLat(), reader.getLng()));
		}
		b.putParcelableArrayList("caches", data);

	}

	@Override
	protected void onPause() {

		super.onPause();
		// TODO: save state
	}

}
