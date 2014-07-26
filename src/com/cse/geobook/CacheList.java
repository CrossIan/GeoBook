package com.cse.geobook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

public class CacheList extends Activity implements OnItemClickListener,
		android.widget.AdapterView.OnItemSelectedListener, OnClickListener {
	static int startCacheNameID = 900;

	Data caches;
	String[] found_titles;
	String[] all_titles;
	String[] titles, sortOptions;
	Bundle extras;

	ListView lv;
	Spinner spin;
	CheckBox found_cb;
	CheckBox all_cb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(this.getClass().toString(), "getting extras");
		this.getExtras();

		setContentView(R.layout.cache_list);

		lv = (ListView) findViewById(R.id.ListView01);
		found_cb = (CheckBox) findViewById(R.id.show_found_check);
		all_cb = (CheckBox) findViewById(R.id.show_not_found_check);

		found_cb.setOnClickListener(this);
		all_cb.setOnClickListener(this);

		// Add options to sort spinner
		sortOptions = new String[5];
		sortOptions[0] = "Name";
		sortOptions[1] = "Rating";
		sortOptions[2] = "Size";
		sortOptions[3] = "Difficulty";
		sortOptions[4] = "Terrain";
		Spinner sortSpinner = (Spinner) findViewById(R.id.sort_spinner);
		sortSpinner.setOnItemSelectedListener(this);

		ArrayAdapter spinnerAdapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, sortOptions);
		sortSpinner.setAdapter(spinnerAdapter);

		if (this.caches != null) {

			caches.sort(Cache.DESCRIPTOR.NAME);

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

			lv.setOnItemClickListener(this);
			// initialize menu
			set_all_titles();
			set_found_titles();
			this.setTitlesBasedOnCheckBox();
			this.updateList();

		}
	}

	private void getExtras() {
		this.extras = this.getIntent().getExtras();
		this.caches = this.extras.getParcelable(Data.CACHE_DATA);
	}

	/**
	 * <pre>
	 * updates the list bases on the titles
	 * -- {@code setTitlesBasedOnCheckBoxes} 
	 * --   must be called first to update titles
	 * </pre>
	 */
	private void updateList() {

		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, titles);

		lv.setAdapter(listAdapter);
	}

	/**
	 * 
	 * @param s
	 *            moves into {@code s} the contents of {@code al}
	 * @param al
	 */
	private void set_all_titles() {
		int size = caches.allCaches.size();
		all_titles = new String[size];
		for (int i = 0; i < size; i++) {
			all_titles[i] = caches.allCaches.get(i).getName();
		}
	}

	/**
	 * 
	 * @param s
	 *            moves into {@code s} the contents of {@code al}
	 * @param al
	 */
	private void set_found_titles() {
		int size = caches.foundCaches.size();
		found_titles = new String[size];
		for (int i = 0; i < size; i++) {
			found_titles[i] = caches.foundCaches.get(i).getName();
		}
	}

	/**
	 * must be called before {@code updateList()}
	 */
	private void setTitlesBasedOnCheckBox() {
		int size = 0;
		// both
		if (found_cb.isChecked() && all_cb.isChecked()) {
			size = found_titles.length + all_titles.length;
			titles = new String[size];
			int index = found_titles.length;
			for (int i = 0; i < index; i++) {
				titles[i] = found_titles[i];
			}
			size = all_titles.length;
			for (int i = 0; index + i < size; i++) {
				titles[index + i] = all_titles[i];
			}

		} else if (all_cb.isChecked()) { // all
			size = all_titles.length;
			titles = new String[size];
			for (int i = 0; i < size; i++) {
				titles[i] = all_titles[i];
			}
		} else if (found_cb.isChecked()) { // found
			size = found_titles.length;
			titles = new String[size];
			for (int i = 0; i < size; i++) {
				titles[i] = found_titles[i];
			}
		} else { // none
			titles = new String[0];
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		caches.target = caches.getCache(titles[position]);

		Bundle extras_new = new Bundle();
		extras_new.putParcelable(Data.CACHE_DATA, caches);

		Intent cacheView = new Intent("android.intent.action.CACHEVIEW");
		cacheView.putExtras(extras_new);
		this.startActivity(cacheView);
		this.finish();

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0: // name
			caches.sort(Cache.DESCRIPTOR.NAME);
			break;
		case 1: // rating
			caches.sort(Cache.DESCRIPTOR.RATING);
			break;
		case 2: // size
			caches.sort(Cache.DESCRIPTOR.CONTAINER);
			break;
		case 3: // difficulty
			caches.sort(Cache.DESCRIPTOR.DIFFICULTY);
			break;
		case 4: // terrain
			caches.sort(Cache.DESCRIPTOR.TERRAIN);
			break;
		default:
			break;
		}
		set_all_titles();
		set_found_titles();

		this.setTitlesBasedOnCheckBox();
		this.updateList();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {

		this.setTitlesBasedOnCheckBox();
		this.updateList();

	}

}
