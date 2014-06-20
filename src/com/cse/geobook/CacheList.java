package com.cse.geobook;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

public class CacheList extends ListActivity {
	static int startCacheNameID = 900;

	HashMap<String, Pair<String, LatLng>> cache;
	ArrayList<String> cacheTitle;
	ArrayList<LatLng> cacheLocation;
	Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(this.getClass().toString(), "getting extras");

		getExtras();
		initHash();
		if (cacheTitle != null && cacheLocation != null) {
			String[] titles = new String[cacheTitle.size()];
			for (int i = 0; i < cacheTitle.size(); i++) {
				titles[i] = cacheTitle.get(i);
			}
			setListAdapter(new ArrayAdapter<String>(CacheList.this,
					android.R.layout.simple_list_item_1, titles));
		}
	}

	private void getExtras() {
		this.extras = this.getIntent().getExtras();
		Log.d(this.getClass().toString(), extras.toString());

		cacheLocation = this.extras
				.getParcelableArrayList(Cache.CACHE_LOCATION);
		cacheTitle = this.extras.getStringArrayList(Cache.CACHE_TITLES);

	}

	private void initHash() {
		cache = new HashMap<String, Pair<String, LatLng>>();
		for (int i = 0; i < cacheLocation.size(); i++) {
			cache.put(cacheTitle.get(i), new Pair<String, LatLng>("test",
					cacheLocation.get(i)));
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		int zoom = 15;

		Bundle extra = new Bundle();
		extra.putAll(extras);
		extras.remove(Cache.ZOOM);
		extras.putInt(Cache.ZOOM, zoom);
		extras.remove(Cache.TARGET_LOC);
		extras.putParcelable(Cache.TARGET_LOC,
				cache.get(cacheTitle.get(position)).second);
		extras.putString(Cache.TARGET_NAME, cacheTitle.get(position));
		Intent map = new Intent("android.intent.action.MAP");
		map.putExtras(extras);
		startActivity(map);
		finish();

	}
}
