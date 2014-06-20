package com.cse.geobook;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity {

	// GoogleMap gMap;

	GoogleMap gMap;
	Bundle extras;

	HashMap<String, Marker> cache;

	// eventually remove and only use hash to reduce storage cost
	static ArrayList<LatLng> cachesLocation;
	static ArrayList<String> cacheTitles;

	LatLng target_loc;
	String target_name;
	int zoom;
	Button listView;
	static int startCacheNameID = 900;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.map);
		listView = (Button) findViewById(R.id.mapToList);
		listView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mapToList = new Intent(
						"android.intent.action.CACHE_LIST");

				mapToList.putExtras(extras);

				startActivity(mapToList);
				finish();

			}
		});
		this.setUpMap();

	}

	private void getExtras() {
		this.extras = this.getIntent().getExtras();
		cachesLocation = this.extras
				.getParcelableArrayList(Cache.CACHE_LOCATION);
		cacheTitles = this.extras.getStringArrayList(Cache.CACHE_TITLES);

		target_loc = this.extras.getParcelable(Cache.TARGET_LOC);
		target_name = this.extras.getString(Cache.TARGET_NAME);
		zoom = this.extras.getInt(Cache.ZOOM);

	}

	private void initHash() {

		cache = new HashMap<String, Marker>();

	}

	private Marker addMarker(LatLng location, String title) {
		Marker m = this.gMap.addMarker(new MarkerOptions().position(location));
		m.setTitle(title); // or add in
		// MarkerOptions
		m.setSnippet("");
		return m;
	}

	private void addMarker(LatLng location) {
		Marker m = this.gMap.addMarker(new MarkerOptions().position(location));
	}

	private void setUpActionListeners() {
		clickListener listener = new clickListener();
		this.gMap.setOnMarkerClickListener(listener);
		this.gMap.setOnMapClickListener(listener);
		this.gMap.setOnMapLongClickListener(listener);
		this.gMap.setOnInfoWindowClickListener(listener);
	}

	private void setUpMap() {
		if (this.gMap == null) {
			this.gMap = ((SupportMapFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}
		this.setUpActionListeners();
		this.getExtras();
		this.initHash();

		this.gMap.setMyLocationEnabled(true);
		/*
		 * set target & zoom. if target is passed use that, else use default of
		 * columbus ohio
		 */
		if (this.target_loc != null) {
			this.gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					this.target_loc, zoom));
			/*
			 * if (target_name != null && target_name.compareTo("") != 0) {
			 * cache.get(target_name).showInfoWindow(); }
			 */
		} else {
			this.gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(39.961138, -83.001465), zoom));
		}
		// set markers
		if (cachesLocation != null && cacheTitles != null) {
			for (int i = 0; i < cachesLocation.size(); i++) {
				Marker m = this.addMarker(cachesLocation.get(i),
						cacheTitles.get(i));

				cache.put(cacheTitles.get(i), m);
			}
		}

	}

	private class clickListener implements OnMapClickListener,
			OnMapLongClickListener, OnMarkerClickListener,
			OnInfoWindowClickListener, OnMyLocationButtonClickListener {

		@Override
		public void onMapClick(LatLng arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean onMyLocationButtonClick() {
			if (Map.this.gMap != null) {
				Map.this.gMap.stopAnimation();
				Location myloc = Map.this.gMap.getMyLocation();
				if (myloc != null) {
					Map.this.gMap.animateCamera(CameraUpdateFactory
							.newLatLng(new LatLng(myloc.getLatitude(), myloc
									.getLongitude())));
				}
			}
			return true;
		}

		@Override
		public void onMapLongClick(LatLng cache) {

			/** dialog click listener ONLY for the below alert dialog box */
			class dialogClickListener implements
					DialogInterface.OnClickListener {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						Map.this.addMarker(cachesLocation.get(cachesLocation
								.size() - 1));

						break;
					case DialogInterface.BUTTON_NEUTRAL:
						cachesLocation.remove(cachesLocation.size() - 1);
						break;
					}
				}
			}
			dialogClickListener listener = new dialogClickListener();
			cachesLocation.add(cache);
			AlertDialog.Builder builder = new AlertDialog.Builder(Map.this);
			builder.setMessage(R.string.confirm_new_cache)
					.setTitle(R.string.new_cache)
					.setPositiveButton(R.string.cache_option_yes, listener)

					.setNegativeButton(R.string.cache_option_no, listener);

			AlertDialog dialog = builder.create();
			dialog.show();

		}

		@Override
		public boolean onMarkerClick(Marker marker) {
			// TODO Auto-generated method stub
			if (marker.isInfoWindowShown()) {
				marker.hideInfoWindow();
			} else {
				marker.showInfoWindow();
			}

			return false;
		}

		@Override
		public void onInfoWindowClick(final Marker marker) {

			/** dialog click listener ONLY for the below alert dialog box */
			class dialogClickListener implements
					DialogInterface.OnClickListener {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						// TODO: goto cache view

						Intent cache = new Intent("android.intent.action.CACHE");
						Bundle extra = new Bundle();

						// Cache.setDataToPass(extra, marker);
						cache.putExtras(extra);
						Map.this.startActivity(cache);

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

	/*
	 * Populates the map view menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.menu_map, menu);
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
			this.startActivity(new Intent(Map.this, Profile.class));
			return true;
		case R.id.menu_map_settings:
			return true;
		case R.id.menu_map_about:
			AlertDialog.Builder builder = new AlertDialog.Builder(Map.this);
			builder.setMessage(R.string.dialog_about_message).setTitle(
					R.string.dialog_about_title);
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// User clicked OK button
						}
					});

			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		case R.id.menu_map_signout:
			//
			this.finish();
			this.startActivity(new Intent(Map.this, Login.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	static void setDataToPass(Bundle b, Context c) {
		// target
		b.putParcelable(Cache.TARGET_LOC, new LatLng(39.961138, -83.001465));
		// markers (caches)
		ArrayList<LatLng> data = new ArrayList<LatLng>();
		ArrayList<String> title = new ArrayList<String>();

		DataParser reader = new DataParser(c, R.raw.ohio);
		int i = startCacheNameID;
		while (reader.ready()) {
			data.add(new LatLng(reader.getLat(), reader.getLng()));
			title.add(reader.getName());
			i++;
		}
		b.putStringArrayList(Cache.CACHE_TITLES, title);

		b.putParcelableArrayList(Cache.CACHE_LOCATION, data);

	}

	@Override
	protected void onPause() {

		super.onPause();
		// TODO: save state
	}

	public static void setPassMarkerList(Bundle extra,
			Context applicationContext) {

	}

}
