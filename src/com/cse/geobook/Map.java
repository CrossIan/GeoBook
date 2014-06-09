package com.cse.geobook;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity {

	GoogleMap gMap;

	Bundle extras;
	ArrayList<LatLng> caches;
	LatLng target;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		getExtras();
		gMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		getExtras();
		setUpMap();
	}

	private void getExtras() {
		extras = getIntent().getExtras();
		caches = extras.getParcelableArrayList("caches");
		target = extras.getParcelable("target");

	}

	private void addMarker(LatLng location) {
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
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setUpMap() {
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

		gMap.setOnMarkerClickListener(new OnMarkerClickListener() {

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

		});

	}

}
