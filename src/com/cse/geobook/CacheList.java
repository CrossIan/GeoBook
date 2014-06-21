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

		getExtras();
		if (caches != null) {
			int size = caches.data.size();
			titles = new String[size];
			for (int i = 0; i < size; i++) {
				String next = caches.data.get(i).getTitle();
				titles[i] = next;
			}

			// initialize menu
			setListAdapter(new ArrayAdapter<String>(CacheList.this,
					android.R.layout.simple_list_item_1, titles));

		}
	}

	private void getExtras() {
		this.extras = this.getIntent().getExtras();
		caches = extras.getParcelable(Data.CACHE_DATA);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		int zoom = 16;

		Data data = new Data(caches.data, caches.data.get(position), zoom);

		Bundle extras_new = new Bundle();
		extras_new.putParcelable(Data.CACHE_DATA, data);

		Intent map = new Intent("android.intent.action.MAP");
		map.putExtras(extras_new);
		startActivity(map);
		finish();

	}
}
