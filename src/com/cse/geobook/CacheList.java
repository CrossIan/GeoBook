package com.cse.geobook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.cse.geobook.Data.SortBy;

public class CacheList extends Activity implements OnItemClickListener {
	static int startCacheNameID = 900;

	Data caches;
	String[] found_titles;
	String[] all_titles;
	String[] titles, sortOptions;
	Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(this.getClass().toString(), "getting extras");

		setContentView(R.layout.cache_list);
		ListView lv = (ListView) findViewById(R.id.ListView01);

		// Add options to sort spinner
		sortOptions = new String[5];
		sortOptions[0] = "Name";
		sortOptions[1] = "Rating";
		sortOptions[2] = "Size";
		sortOptions[3] = "Difficulty";
		sortOptions[4] = "Terrain";
		Spinner sortSpinner = (Spinner) findViewById(R.id.sort_spinner);
		ArrayAdapter spinnerAdapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, sortOptions);
		sortSpinner.setAdapter(spinnerAdapter);

		this.getExtras();
		if (this.caches != null) {

			caches.sort(SortBy.NAME);

			/**
			 * <pre>
			 * int size = this.caches.foundCaches.size();
			 * this.titles = new String[size];
			 * for (int i = 0; i &lt; size; i++) {
			 * 	String next = this.caches.foundCaches.get(i).getTitle();
			 * 	this.titles[i] = next;
			 * }
			 * </pre>
			 */

			int size = this.caches.foundCaches.size();
			this.found_titles = new String[size];
			for (int i = 0; i < size; i++) {
				Cache ALi = this.caches.foundCaches.get(i);
				found_titles[i] = ALi.get(0);
			}

			size = this.caches.allCaches.size();
			this.all_titles = new String[size];
			for (int i = 0; i < size; i++) {
				Cache ALi = this.caches.allCaches.get(i);
				all_titles[i] = ALi.get(0);
			}

			// initialize menu
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, titles);

			lv.setAdapter(listAdapter);
			lv.setOnItemClickListener(this);

		}
	}

	private void getExtras() {
		this.extras = this.getIntent().getExtras();
		this.caches = this.extras.getParcelable(Data.CACHE_DATA);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
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
