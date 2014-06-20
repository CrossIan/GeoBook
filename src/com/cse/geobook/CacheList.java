package com.cse.geobook;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

public class CacheList extends ListActivity {
	static int startCacheNameID = 900;
	ArrayList<String> cacheTitle;
	HashMap<String, String> cacheDescription;
	ArrayList<LatLng> cacheLocation;
	Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(this.getClass().toString(), "getting extras");

		getExtras();
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

		cacheLocation = this.extras.getParcelableArrayList("caches");
		cacheTitle = this.extras.getStringArrayList("cacheTitles");

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		String name = cacheTitle.get(position);

		Intent map = new Intent("android.intent.action.MAP");
		map.putExtras(extras);
		map.putExtra("target", name);
		startActivity(map);
		finish();

	}

	static void setDataToPass(Bundle b, ArrayList<LatLng> data) {

		b.putParcelableArrayList("caches", data);

	}

}
