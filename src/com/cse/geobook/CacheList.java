package com.cse.geobook;

import java.util.ArrayList;

import android.R.anim;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cse.geobook.Data.SortBy;
import com.google.android.gms.maps.model.MarkerOptions;

public class CacheList extends Activity implements OnItemClickListener {
	static int startCacheNameID = 900;

	Data caches;
	String[] titles;
	Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(this.getClass().toString(), "getting extras");
		
		setContentView(R.layout.cache_list);
		ListView lv = (ListView) findViewById(R.id.ListView01);

		this.getExtras();
		if (this.caches != null) {

			//
			this.setUpTemporaryDataForSorting();
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

			int size = this.caches.data.size();
			this.titles = new String[size];
			for (int i = 0; i < size; i++) {
				ArrayList<String> ALi = this.caches.data.get(i);
				titles[i] = ALi.get(0);
			}

			// initialize menu
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, titles);
//			setListAdapter(adapter);
			
			
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(this);

		}
	}

	private void getExtras() {
		this.extras = this.getIntent().getExtras();
		this.caches = this.extras.getParcelable(Data.CACHE_DATA);
	}



	

	// this method is not needed for final
	// only used for testing before data is completed
	private void setUpTemporaryDataForSorting() {
		caches.data = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < this.caches.allCaches.size(); i++) {
			ArrayList<String> temp = new ArrayList<String>();
			MarkerOptions mo = caches.allCaches.get(i);
			temp.add(mo.getTitle());
			this.caches.data.add(temp);
		}
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
