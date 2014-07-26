package com.cse.geobook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.android.gms.plus.model.people.Person;

public class Map extends FragmentActivity {
	// GoogleMap gMap;

	GoogleMap gMap;
	private Person currentPerson;
	Bundle extras;
	Data caches;
	Button listView;
	LatLng lastLocation;
	String currentCity, currentState;
	ArrayList<Marker> markers;
	BitmapDescriptor colorMarker;
	private static boolean startUp;
	private static final String TAG = "Map.java";

	// private final double MAX_DISTANCEFROMCACHE = 25;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.map);
		this.getExtras();
		this.setMarkerColor();
		this.setUpMap();
		startUp = true;
	}

	/**
	 * Used to retrieve {@code Bundle extras} if any, otherwise initializes
	 * {@code caches} from persistent data.
	 * 
	 * Guarantees {@code caches } != null
	 */
	private void getExtras() {
		this.extras = this.getIntent().getExtras();

		currentPerson = extras.getParcelable("USER");

		Cache tempTarget = null;
		int zoom = 11;
		if (extras != null && extras.containsKey(Data.CACHE_DATA)) {
			this.caches = this.extras.getParcelable(Data.CACHE_DATA);
			tempTarget = caches.target;
			zoom = caches.zoom;
		}

		this.caches = readInData();

		// if target is null, use the target set by readInData
		if (tempTarget != null) {
			caches.target = tempTarget;
			caches.zoom = zoom;
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

	private void setMarkerColor() {

		String colorValue = Settings.getColorMarker(this
				.getApplicationContext());
		// Default Value
		colorMarker = BitmapDescriptorFactory
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
		gMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		gMap.setMyLocationEnabled(true);
		if (gMap != null) {
			gMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
				@Override
				public void onMyLocationChange(Location arg0) {
					lastLocation = new LatLng(arg0.getLatitude(), arg0
							.getLongitude());
					Log.d("Map.java",
							"Starting location: " + lastLocation.toString());
					if (startUp) {
						gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
								lastLocation, 11));
						startUp = false;

						// Set local variables to current city/state based
						// on location
						setCurrentCityState(lastLocation);
					}
				}
			});
		}

		markers = new ArrayList<Marker>();
		this.setUpActionListeners();
		this.gMap.setMyLocationEnabled(true);

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

	/**
	 * returns the distance between two {@code LatLng} locations
	 * 
	 * @param start
	 *            the initial {@code LatLng}
	 * @param end
	 *            the finishing {@code LatLng}
	 * @return the distance between two {@code LatLng} locations
	 */
	private static double distance(LatLng start, LatLng end) {
		double lat1 = start.latitude;
		double lon1 = start.longitude;
		double lat2 = end.latitude;
		double lon2 = end.longitude;

		double R = 6371; // km
		double phi1 = Math.toRadians(lat1);
		double phi2 = Math.toRadians(lat2);
		double dphi = Math.toRadians(lat2 - lat1);
		double dlamb = Math.toRadians(lon2 - lon1);

		double a = Math.sin(dphi / 2) * Math.sin(dphi / 2) + Math.cos(phi1)
				* Math.cos(phi2) * Math.sin(dlamb / 2) * Math.sin(dlamb / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double d = R * c; // Distance in km

		return d * 1000; // Distance in m
	}

	/**
	 * class to implement all Listeners
	 */
	private class clickListener implements OnMapClickListener,
			OnMapLongClickListener, OnMarkerClickListener,
			OnInfoWindowClickListener, OnMyLocationButtonClickListener {

		@Override
		public void onMapClick(LatLng arg0) {
		}

		@Override
		public boolean onMyLocationButtonClick() {
			Log.d("Map.java", "Tapped MyLocation button");
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
					switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						Cache cache = new Cache();
						cache.lat(String.valueOf(pos.latitude));
						cache.lng(String.valueOf(pos.longitude));
						cache.name("default");
						cache.description("");
						Map.this.caches.foundCaches.add(cache);
						Map.this.gMap.addMarker(createMarkerOptions(cache));
						// Todo place in hash map
						DataParser found = new DataParser(
								getApplicationContext(), Cache.FOUND_CACHES);
						found.overwriteAll(Map.this.caches.foundCaches);
						found.close();

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
			if (marker.isInfoWindowShown()) {
				marker.hideInfoWindow();
			} else {
				marker.showInfoWindow();
			}
			return false;
		}

		@Override
		public void onInfoWindowClick(final Marker marker) {

			Intent cacheView = new Intent("android.intent.action.CACHEVIEW");
			Bundle extra = new Bundle();

			Cache target = caches.getCache(marker);

			Map.this.caches.target = target;

			double distanceFrom = distance(lastLocation, marker.getPosition());
			extra.putDouble("DISTANCE", distanceFrom);
			extra.putParcelable(Data.CACHE_DATA, Map.this.caches);
			extra.putParcelable("USER", (Parcelable) currentPerson);

			cacheView.putExtras(extra);

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
			Intent profileIntent = new Intent("android.intent.action.PROFILE");
			Bundle extra = new Bundle();
			extra.putParcelable("USER", (Parcelable) currentPerson);
			extra.putString("CITY", currentCity);
			extra.putString("STATE", currentState);
			profileIntent.putExtras(extra);

			// Start profile activity
			this.startActivity(profileIntent);
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
		File targetCacheFile = getApplicationContext().getFileStreamPath(
				Cache.TARGET_CACHE);

		if (targetCacheFile.exists()) {
			DataParser target = new DataParser(getApplicationContext(),
					Cache.TARGET_CACHE);
			caches.target = target.read().get(0);
			target.close();

			// if target exists && if target is in on the map, remove marker and
			// replace with new values

			// assumes that the cache is already added to the found caches in
			// cacheView
			for (int i = 0; i < markers.size(); i++) {
				if (caches.target.equals(markers.get(i))) {
					markers.remove(i);
					markers.add(this.gMap.addMarker(createMarkerOptions(
							caches.target).icon(colorMarker)));
				}
			}

		} else {
			caches.target = new Cache();
			caches.target.name("DEFAULT");
		}

	}

	private ArrayList<Cache> readFile(String fileName) {

		File file = getApplicationContext().getFileStreamPath(fileName);
		ArrayList<Cache> cacheList = null;
		if (file.exists() || fileName.equals(Cache.ALL_CACHES)) {
			DataParser parser = new DataParser(getApplicationContext(),
					fileName);
			cacheList = parser.read();
			parser.close();
		} else {
			cacheList = new ArrayList<Cache>();
		}
		return cacheList;
	}

	/**
	 * Reads in the persistent cache data and returns its values stored the
	 * {@code Data} class
	 * 
	 * @return the persistent data
	 */
	private Data readInData() {
		ArrayList<Cache> ac = null;
		ArrayList<Cache> fc = null;
		ArrayList<Cache> tc = null;
		Cache t = null;

		ac = readFile(Cache.ALL_CACHES);
		fc = readFile(Cache.FOUND_CACHES);
		tc = readFile(Cache.TARGET_CACHE);
		if (tc.size() > 0) {
			t = tc.get(0);
		}

		return new Data(fc, ac, t, 11);
	}

	private MarkerOptions createMarkerOptions(Cache cache) {
		MarkerOptions result = new MarkerOptions();
		result.title(cache.getName());
		result.snippet(cache.getDescription());
		result.position(new LatLng(cache.getLat(), cache.getLng()));

		return result;
	}

	/**
	 * This method sets the class variables currentCity and currentState based
	 * on a Geocoder object.
	 */
	private void setCurrentCityState(LatLng loc) {
		Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
		List<Address> addresses;
		try {
			addresses = gcd.getFromLocation(loc.latitude, loc.longitude, 1);
			if (addresses.size() > 0) {
				String[] addressLine = addresses.get(0).getAddressLine(1)
						.split(",");
				currentCity = addressLine[0];
				if (addressLine.length > 1)
					currentState = addressLine[1].substring(1, 3);
				else
					currentState = "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (currentState.length() > 1)
			Toast.makeText(Map.this, currentCity + ", " + currentState,
					Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(Map.this, currentCity, Toast.LENGTH_SHORT).show();
	}
}
