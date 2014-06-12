package com.cse.geobook;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

public class Login extends Activity {

	private static final int DIALOG_ALERT = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);

		final Button button = (Button) this.findViewById(R.id.signButton);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				        Login.this);

				// set title
				alertDialogBuilder.setTitle("Sign in");

				// set dialog message
				alertDialogBuilder
				        .setMessage("Click yes to exit!")
				        .setCancelable(false)
				        .setPositiveButton("Yes",
				                new DialogInterface.OnClickListener() {
					                @Override
					                public void onClick(DialogInterface dialog,
					                        int id) {
						                // Move to the map viewer
						                Intent map = new Intent(
						                        "android.intent.action.MAP");
						                Bundle extra = new Bundle();

						                Login.this.setDataToPass(extra);
						                map.putExtras(extra);
						                Login.this.startActivity(map);
						                Login.this.finish();
					                }
				                })
				        .setNegativeButton("No",
				                new DialogInterface.OnClickListener() {
					                @Override
					                public void onClick(DialogInterface dialog,
					                        int id) {
						                // if this button is clicked, just close
						                // the dialog box and do nothing
						                dialog.cancel();
					                }
				                });

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			}
		});

	}

	void setDataToPass(Bundle b) {
		// target
		b.putParcelable("target", new LatLng(39.961138, -83.001465));
		// markers (caches)
		ArrayList<LatLng> data = new ArrayList<LatLng>();

		DataParser reader = new DataParser(this.getApplicationContext(),
		        R.raw.test_data);
		/*
		 * data.add(new LatLng(39.901138, -82.951465)); data.add(new
		 * LatLng(39.901138, -83.001465)); data.add(new LatLng(39.901138,
		 * -83.051465)); data.add(new LatLng(39.961138, -82.951465));
		 * data.add(new LatLng(39.961138, -83.001465)); data.add(new
		 * LatLng(39.961138, -83.051465)); data.add(new LatLng(39.991138,
		 * -82.951465)); data.add(new LatLng(39.991138, -83.001465));
		 * data.add(new LatLng(39.991138, -83.051465));
		 */
		for (int i = 0; i < 25; i++) {
			data.add(new LatLng(reader.getLat(), reader.getLng()));
		}
		b.putParcelableArrayList("caches", data);

	}

}
