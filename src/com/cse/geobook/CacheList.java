package com.cse.geobook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

public class CacheList extends Activity implements OnItemClickListener,
        android.widget.AdapterView.OnItemSelectedListener {
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

		this.setContentView(R.layout.cache_list);

		this.lv = (ListView) this.findViewById(R.id.ListView01);
		this.found_cb = (CheckBox) this.findViewById(R.id.show_found_check);
		this.all_cb = (CheckBox) this.findViewById(R.id.show_not_found_check);

		// Add options to sort spinner
		this.sortOptions = new String[5];
		this.sortOptions[0] = "Name";
		this.sortOptions[1] = "Rating";
		this.sortOptions[2] = "Size";
		this.sortOptions[3] = "Difficulty";
		this.sortOptions[4] = "Terrain";
		Spinner sortSpinner = (Spinner) this.findViewById(R.id.sort_spinner);
		sortSpinner.setOnItemSelectedListener(this);

		ArrayAdapter spinnerAdapter = new ArrayAdapter(this,
		        android.R.layout.simple_spinner_item, this.sortOptions);
		sortSpinner.setAdapter(spinnerAdapter);

		if (this.caches != null) {

			this.caches.sort(Cache.DESCRIPTOR.NAME);

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

			this.lv.setOnItemClickListener(this);
			// initialize menu
			this.set_all_titles();
			this.set_found_titles();
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
		        android.R.layout.simple_list_item_1, this.titles);

		this.lv.setAdapter(listAdapter);
	}

	/**
	 * 
	 * @param s
	 *            moves into {@code s} the contents of {@code al}
	 * @param al
	 */
	private void set_all_titles() {
		int size = this.caches.allCaches.size();
		this.all_titles = new String[size];
		for (int i = 0; i < size; i++) {
			this.all_titles[i] = this.caches.allCaches.get(i).getName();
		}
	}

	/**
	 * 
	 * @param s
	 *            moves into {@code s} the contents of {@code al}
	 * @param al
	 */
	private void set_found_titles() {
		int size = this.caches.foundCaches.size();
		this.found_titles = new String[size];
		for (int i = 0; i < size; i++) {
			this.found_titles[i] = this.caches.foundCaches.get(i).getName();
		}
	}

	/**
	 * must be called before {@code updateList()}
	 */
	private void setTitlesBasedOnCheckBox() {
		int size = 0;
		// both
		if (this.found_cb.isChecked() && this.all_cb.isChecked()) {
			size = this.found_titles.length + this.all_titles.length;
			this.titles = new String[size];
			int index = this.found_titles.length;
			for (int i = 0; i < index; i++) {
				this.titles[i] = this.found_titles[i];
			}
			size = this.all_titles.length;
			for (int i = 0; index + i < size; i++) {
				this.titles[index + i] = this.all_titles[i];
			}

		} else if (this.all_cb.isChecked()) { // all
			size = this.found_titles.length;
			this.titles = new String[size];
			for (int i = 0; i < size; i++) {
				this.titles[i] = this.all_titles[i];
			}
		} else if (this.found_cb.isChecked()) { // found
			size = this.all_titles.length;
			this.titles = new String[size];
			for (int i = 0; i < size; i++) {
				this.titles[i] = this.found_titles[i];
			}
		} else { // none
			this.titles = new String[0];
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
	        long id) {
		switch (position) {
			case 0: // name
				this.caches.sort(Cache.DESCRIPTOR.NAME);
				break;
			case 1: // rating
				this.caches.sort(Cache.DESCRIPTOR.RATING);
				break;
			case 2: // size
				this.caches.sort(Cache.DESCRIPTOR.CONTAINER);
				break;
			case 3: // difficulty
				this.caches.sort(Cache.DESCRIPTOR.DIFFICULTY);
				break;
			case 4: // terrain
				this.caches.sort(Cache.DESCRIPTOR.TERRAIN);
				break;
			default:
				break;
		}
		this.set_all_titles();
		this.set_found_titles();

		this.setTitlesBasedOnCheckBox();
		this.updateList();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}
