package com.cse.geobook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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
				DataParser writer = new DataParser(getApplicationContext(),
						"PersistentData.txt");
				writer.overwriteAll(data);
				writer.close();

				Bundle extras_new = new Bundle();
				extras_new.putParcelable(Data.CACHE_DATA, data);

//				Intent map = new Intent("android.intent.action.MAP");
//				map.putExtras(extras_new);
//				startActivity(map);
				finish();

			}
		});
	}

	private void getExtras() {
		Bundle extras = getIntent().getExtras();
		data = extras.getParcelable(Data.CACHE_DATA);
		cacheName.setText(data.target.getTitle());
		description.setText(data.target.getSnippet());

	}

}
