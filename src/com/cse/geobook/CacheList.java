package com.cse.geobook;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CacheList extends ListActivity {
	static int startCacheNameID = 900;

	Data caches;
	String[] titles;
	Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(this.getClass().toString(), "getting extras");

		this.getExtras();
		if (this.caches != null) {
			int size = this.caches.foundCaches.size();
			this.titles = new String[size];
			for (int i = 0; i < size; i++) {
				String next = this.caches.foundCaches.get(i).getTitle();
				this.titles[i] = next;
			}

			// initialize menu
			this.setListAdapter(new ArrayAdapter<String>(CacheList.this,
			        android.R.layout.simple_list_item_1, this.titles));

		}
	}

	private void getExtras() {
		this.extras = this.getIntent().getExtras();
		this.caches = this.extras.getParcelable(Data.CACHE_DATA);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		int zoom = 16;

		Data data = new Data(this.caches.foundCaches, this.caches.foundCaches,
		        this.caches.foundCaches.get(position), zoom);

		Bundle extras_new = new Bundle();
		extras_new.putParcelable(Data.CACHE_DATA, data);

		Intent map = new Intent("android.intent.action.MAP");
		map.putExtras(extras_new);
		this.startActivity(map);
		this.finish();

	}
}
