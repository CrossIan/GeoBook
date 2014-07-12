package com.cse.geobook;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity {
	// GoogleMap gMap;

	GoogleMap gMap;
	Bundle extras;
	Data caches;
	Button listView;
	ArrayList<Marker> markers;

	private final double MAX_DISTANCEFROMCACHE = 25;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.map);
		this.getExtras();
		this.setUpMap();
	}

	/**
	 * Used to retrieve {@code Bundle extras} if any, otherwise initializes
	 * {@code caches} from persistent data.
	 * 
	 * Guarantees {@code caches } != null
	 */
	private void getExtras() {
		this.extras = this.getIntent().getExtras();
		MarkerOptions mo = null;
		int zoom = 11;
		if (extras != null && extras.containsKey(Data.CACHE_DATA)) {
			this.caches = this.extras.getParcelable(Data.CACHE_DATA);
			mo = createMarkerOptions(caches.target);
			zoom = caches.zoom;
		}

		this.caches = readInData();
		if (mo != null) {
			// caches.target = mo;
			// caches.zoom = zoom;
		}
	}

	/**
	 * Method where all action listeners are added
	 */
	private void setUpActionListeners() {
		clickListener listener = new clickListener();
		this.gMap.setOnMarkerClickListener(listener);
		this.gMap.setOnMapClickListener(listener);
		this.gMap.setOnMapLongClickListener(listener);
		this.gMap.setOnInfoWindowClickListener(listener);
	}

	/**
	 * <pre>
	 * Initialzes {@code this.gMap} 
	 * Adds all markers to {@code gMap}
	 * 
	 * </pre>
	 * 
	 * @requires {@code caches} != null
	 */
	private void setUpMap() {

		String colorValue = Settings.getColorMarker(this
				.getApplicationContext());
		// Default Value
		BitmapDescriptor colorMarker = BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);

		switch (colorValue) {
		case "1":
			colorMarker = BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
			break;
		case "2":
			colorMarker = BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED);
			break;
		case "3":
			colorMarker = BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
			break;
		case "4":
			colorMarker = BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
			break;
		case "5":
			colorMarker = BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
			break;
		case "6":
			colorMarker = BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
			break;
		case "7":
			colorMarker = BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
			break;
		case "8":
			colorMarker = BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
			break;
		case "9":
			colorMarker = BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
			break;
		case "10":
			colorMarker = BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
			break;
		}

		if (this.gMap == null) {
			this.gMap = ((SupportMapFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

		}
		markers = new ArrayList<Marker>();
		this.setUpActionListeners();
		this.gMap.setMyLocationEnabled(true);

		/*
		 * set up markers
		 */

		this.gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				this.caches.target.getLat(), this.caches.target.getLng()),
				this.caches.zoom));

		// set all caches
		int size = this.caches.allCaches.size();
		Log.d("cache", "all -size: " + size);
		for (int i = 0; i < size; i++) {
			markers.add(this.gMap
					.addMarker(createMarkerOptions(this.caches.allCaches.get(i))));
		}

		// set found caches
		size = this.caches.foundCaches.size();
		Log.d("cache", "found - size: " + size);
		for (int i = 0; i < size; i++) {
			markers.add(this.gMap.addMarker(createMarkerOptions(
					this.caches.foundCaches.get(i)).icon(colorMarker)));
		}

	}

	private void removeAllMarkers() {
		int size = markers.size();
		for (int i = 0; i < size; i++) {
			markers.get(i).remove();
		}
	}

	private boolean cacheFound(MarkerOptions mo) {
		return caches.foundCaches.contains(mo);
	}

	private static double distance(LatLng start, LatLng end) {
		double xSquared = Math.pow((start.latitude - end.latitude), 2);
		double ySquared = Math.pow((start.longitude - end.longitude), 2);
		// convert to feet
		double convertToFeet = (10000 / 90 * 3280.4);
		xSquared *= convertToFeet;
		ySquared *= convertToFeet;

		return Math.sqrt(xSquared + ySquared);

	}

	/**
	 * class to implement all Listeners
	 * 
	 * @author Nate
	 * 
	 */
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
		public void onMapLongClick(final LatLng pos) {

			/** dialog click listener ONLY for the below alert dialog box */
			class dialogClickListener implements
					DialogInterface.OnClickListener {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						Cache cache = new Cache();
						cache.lat(String.valueOf(pos.latitude));
						cache.lng(String.valueOf(pos.longitude));
						cache.name("default");
						cache.description("");
						Map.this.caches.allCaches.add(cache);
						Map.this.gMap.addMarker(createMarkerOptions(cache));
						// Todo place in hash map
						DataParser all = new DataParser(
								getApplicationContext(), Cache.ALL_CACHES);
						all.overwriteAll(Map.this.caches.allCaches);
						all.close();

						break;
					case DialogInterface.BUTTON_NEUTRAL:
						// Todo
						break;
					}
				}
			}
			dialogClickListener listener = new dialogClickListener();

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
			Cache cache = new Cache();
			cache.name(marker.getTitle());
			cache.description(marker.getSnippet());
			cache.lat(String.valueOf(marker.getPosition().latitude));
			cache.lng(String.valueOf(marker.getPosition().longitude));

			Intent cacheView = new Intent("android.intent.action.CACHE");
			Bundle extra = new Bundle();

			Map.this.caches.target = cache;
			extra.putParcelable(Data.CACHE_DATA, Map.this.caches);

			cacheView.putExtras(extra);
			/**
			 * <pre> doesn't work
			 * 
			 * <pre>
			 * Location currentLocation =
			 * gMap.getMyLocation();
			 * 
			 * LatLng position = new LatLng(currentLocation.getLatitude(),
			 * currentLocation.getLongitude());
			 * 
			 * if (cacheFound(mo)) { Map.this.startActivity(cache);
			 * 
			 * } else if (distance(position, mo.getPosition()) <
			 * MAX_DISTANCEFROMCACHE) {
			 * 
			 * Map.this.startActivity(cache);
			 * 
			 * } else { // error }
			 */
			// remove once above is working
			Map.this.startActivity(cacheView);

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
	 * Allows menu to respond to touch events
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		if (item.getItemId() == R.id.menu_map_profile) {
			this.startActivity(new Intent(Map.this, Profile.class));
			return true;
		} else if (item.getItemId() == R.id.menu_map_settings) {
			this.startActivity(new Intent(Map.this, Settings.class));
			// this.startActivity(new Intent(Map.this,
			// CacheList_Activity.class));
			return true;
		} else if (item.getItemId() == R.id.menu_map_cache_list) {
			Intent mapToList = new Intent("android.intent.action.CACHE_LIST");
			Bundle extra = new Bundle();
			extra.putParcelable(Data.CACHE_DATA, Map.this.caches);
			mapToList.putExtras(extra);

			Map.this.startActivity(mapToList);
			return true;
		} else if (item.getItemId() == R.id.menu_map_about) {
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
		} else if (item.getItemId() == R.id.menu_map_signout) {
			//
			this.finish();
			this.startActivity(new Intent(Map.this, Login.class));
			return true;
		} else
			return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.getExtras();
		this.removeAllMarkers();
		this.setUpMap();

	}

	private Data readInData() {

		File allCachesfile = getApplicationContext().getFileStreamPath(
				Cache.ALL_CACHES);
		File foundCachesfile = getApplicationContext().getFileStreamPath(
				Cache.FOUND_CACHES);
		File targetCacheFile = getApplicationContext().getFileStreamPath(
				Cache.TARGET_CACHE);

		ArrayList<Cache> ac = null;
		ArrayList<Cache> fc = null;
		ArrayList<Cache> t = null;

		DataParser all = new DataParser(getApplicationContext(),
				Cache.ALL_CACHES);
		ac = all.read();
		all.close();

		if (foundCachesfile.exists()) {
			DataParser found = new DataParser(getApplicationContext(),
					Cache.FOUND_CACHES);
			fc = found.read();
			found.close();
		} else {
			fc = new ArrayList<Cache>();
		}

		if (targetCacheFile.exists()) {
			DataParser target = new DataParser(getApplicationContext(),
					Cache.TARGET_CACHE);
			t = target.read();
			target.close();
		} else {
			DataParser target = new DataParser(getApplicationContext(),
					Cache.TARGET_CACHE);
			t = new ArrayList<Cache>();
			Cache cache = new Cache();
			cache.name("defaultTarget");
			cache.lat("39.961138");
			cache.lng("-83.001465");
			t.add(cache);
			target.overwriteAll(t);

			target.close();
		}

		return new Data(fc, ac, t.get(0), 11);
	}

	private MarkerOptions createMarkerOptions(Cache cache) {
		MarkerOptions result = new MarkerOptions();
		result.title(cache.getName());
		result.snippet(cache.getDescription());
		result.position(new LatLng(cache.getLat(), cache.getLng()));

		return result;
	}
}
