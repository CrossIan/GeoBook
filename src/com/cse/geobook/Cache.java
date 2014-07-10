package com.cse.geobook;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.MarkerOptions;

public class Cache extends Activity implements OnClickListener{

	LinearLayout view;
	EditText cacheName;
	EditText description;
	TextView cachelat;
	TextView cachelong;
	TextView datevisited;
	Button saveCacheButton,shareCacheButton;
	Data data;

	public static final String FOUND_CACHES = "foundCaches.txt";
	public static final String ALL_CACHES = "PersistentData.txt";
	public static final String TARGET_CACHE = "Target.txt";

	final Double EPISILON = .00001;

	// PHOTO

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cache);
		
		
		saveCacheButton = (Button) this.findViewById(R.id.save_cache_button);
		saveCacheButton.setOnClickListener(this);
		shareCacheButton = (Button) this.findViewById(R.id.share_cache_button);
		shareCacheButton.setOnClickListener(this);
		cacheName = (EditText) this.findViewById(R.id.cacheName);
		description = (EditText) this.findViewById(R.id.cacheDescription);
		cachelat = (TextView) this.findViewById(R.id.cachelat);
		cachelong = (TextView) this.findViewById(R.id.cachelong);

		this.getExtras();
	}
	
	
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.save_cache_button){
			int size = Cache.this.data.allCaches.size();
			boolean searching = true;
			int i = 0;
			while (searching && i < size) {
				MarkerOptions temp = Cache.this.data.allCaches.get(i);
				if (Math.abs(temp.getPosition().latitude
				        - Cache.this.data.target.getPosition().latitude) < Cache.this.EPISILON
				        && Math.abs(temp.getPosition().longitude
				                - Cache.this.data.target.getPosition().longitude) < Cache.this.EPISILON) {
					temp.title(Cache.this.cacheName.getText().toString());
					temp.snippet(Cache.this.description.getText()
					        .toString());
					searching = false;
					Cache.this.data.foundCaches.add(temp);
					Cache.this.data.allCaches.remove(i);
	
				}
				i++;
			}
			if (!searching) {
				Log.d("data", "marker found");
			} else {
				Log.d("data", "marker not found");
			}
	
			DataParser found = new DataParser(Cache.this
			        .getApplicationContext(), Cache.FOUND_CACHES);
			found.overwriteAll(Cache.this.data.foundCaches);
			found.close();
	
			DataParser all = new DataParser(Cache.this
			        .getApplicationContext(), Cache.ALL_CACHES);
			all.overwriteAll(Cache.this.data.allCaches);
			all.close();
	
			/*
			 * Bundle extras_new = new Bundle();
			 * extras_new.putParcelable(Data.CACHE_DATA, data);
			 * 
			 * Intent map = new Intent("android.intent.action.MAP");
			 * map.putExtras(extras_new); startActivity(map);
			 */
			Cache.this.finish();
		}
		/*
		 * Share on social media
		 */
		else if(v.getId() == R.id.share_cache_button){
			// Create the share dialog
			Log.d("Cache", "Tapped share_cache_button in Cache.java");
			final Dialog shareDialog = new Dialog(this);
			shareDialog.setTitle("Share with:");
			shareDialog.setContentView(R.layout.share_dialog);
			
			// Link widgets
			Button GoogleShareButton = (Button) shareDialog.findViewById(R.id.google_share_button);
			Button FacebookShareButton = (Button) shareDialog.findViewById(R.id.facebook_share_button);
			Button TwitterShareButton = (Button) shareDialog.findViewById(R.id.twitter_share_button);
			
			// Set click actions
			
			
			// Show share dialog
			shareDialog.show();
		}
	}
	
	
	
	
	
	
	
	
	

	private void getExtras() {
		Bundle extras = this.getIntent().getExtras();
		this.data = extras.getParcelable(Data.CACHE_DATA);
		this.cacheName.setText(this.data.target.getTitle());
		this.description.setText(this.data.target.getSnippet());
		this.cachelat
		        .setText(Double.toString(this.data.target.getPosition().latitude));
		this.cachelong
		        .setText(Double.toString(this.data.target.getPosition().longitude));
	}
}
