package com.cse.geobook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Cache extends Activity {

	LinearLayout view;
	EditText cacheName;
	EditText description;
	Button save;
	Data data;

	// PHOTO

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cache);
		save = (Button) findViewById(R.id.save);

		cacheName = (EditText) findViewById(R.id.cacheName);

		description = (EditText) findViewById(R.id.cacheDescription);
		getExtras();
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int size = data.data.size();
				boolean searching = true;
				int i = 0;
				while (searching && i < size) {
					MarkerOptions temp = data.data.get(i);
					if (temp.getPosition().latitude == data.target
							.getPosition().latitude
							&& temp.getPosition().longitude == data.target
									.getPosition().longitude) {
						temp.title(cacheName.getText().toString());
						temp.snippet(description.getText().toString());
						searching = false;
					}
					i++;
				}

				DataParser.overwriteAll(data, getApplicationContext());
			}
		});
	}

	private void getExtras() {
		Bundle extras = getIntent().getExtras();
		data = extras.getParcelable(Data.CACHE_DATA);
		cacheName.setText(data.target.getTitle());
		description.setText(data.target.getSnippet());

	}

	static void setDataToPass(Bundle b, Marker marker) {

		b.putString("title", marker.getTitle());
		b.putString("snippit", marker.getSnippet());
		b.putDouble("Lat", marker.getPosition().latitude);
		b.putDouble("Lng", marker.getPosition().longitude);
	}

}
