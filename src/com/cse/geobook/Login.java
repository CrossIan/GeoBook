package com.cse.geobook;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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

				setDataToPass(extra);
				map.putExtras(extra);
				startActivity(map);
			}
		});

	}

	void setDataToPass(Bundle b) {
		// target
		b.putParcelable("target", new LatLng(39.961138, -83.001465));
		// markers (caches)
		ArrayList<LatLng> data = new ArrayList<LatLng>();

		DataParser reader = DataParser(getApplicationContext(), R.raw.test_data);
		/*
		 * data.add(new LatLng(39.901138, -82.951465)); data.add(new
		 * LatLng(39.901138, -83.001465)); data.add(new LatLng(39.901138,
		 * -83.051465)); data.add(new LatLng(39.961138, -82.951465));
		 * data.add(new LatLng(39.961138, -83.001465)); data.add(new
		 * LatLng(39.961138, -83.051465)); data.add(new LatLng(39.991138,
		 * -82.951465)); data.add(new LatLng(39.991138, -83.001465));
		 * data.add(new LatLng(39.991138, -83.051465));
		 */
		while (reader.ready()) {
			data.add(new LatLng(reader.getLat(), reader.getLng()));
		}
		b.putParcelableArrayList("caches", data);

	}

	private DataParser DataParser(Context applicationContext, int testData) {
		// TODO Auto-generated method stub
		return null;
	}
}
