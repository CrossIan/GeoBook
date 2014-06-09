package com.cse.geobook;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		final Button button = (Button) findViewById(R.id.signButton);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				// Move to the map viewer
				Intent map = new Intent("android.intent.action.MAP");

				Bundle extra = new Bundle();
				// test data for map
				ArrayList<LatLng> t = new ArrayList<LatLng>();
				t.add(new LatLng(-37.81319, 144.96298));
				t.add(new LatLng(-33.87365, 151.20689));
				t.add(new LatLng(-34.87365, 152.20689));

				map.putParcelableArrayListExtra("caches", t);
				// map.putExtra("caches", extra);

				startActivity(map);
			}
		});

	}
}
