package com.cse.geobook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;

public class Login extends Activity implements OnClickListener {

	// -------------------------------------------------------*
	// Google+ resources
	// -------------------------------------------------------*
	private static final String TAG = "Login.java";
	// -------------------------------------------------------*
	// Generic resources
	// -------------------------------------------------------*
	private Button bypassButton, signoutButton, revokeAccessButton;
	private Person currentPerson;
	private String currentUserName;

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Initialize buttons click listeners
		findViewById(R.id.google_sign_in_button).setOnClickListener(this);
		signoutButton = (Button) findViewById(R.id.sign_out_button);
		signoutButton.setOnClickListener(this);
		revokeAccessButton = (Button) findViewById(R.id.revoke_access_button);
		revokeAccessButton.setOnClickListener(this);
		bypassButton = (Button) findViewById(R.id.bypass_button);
		bypassButton.setOnClickListener(this);
	}

	// Define click behavior
	@Override
	public void onClick(View v) {
		if (v == bypassButton) {
			Toast.makeText(Login.this, "Bypassing login...", Toast.LENGTH_SHORT)
					.show();
			Intent map = new Intent("android.intent.action.MAP");
			Bundle extra = new Bundle();
			if (currentPerson != null)
				extra.putParcelable("USER", (Parcelable) currentPerson);
			else
				extra.putParcelable("USER", null);

			map.putExtras(extra);
			// Finish login activity and move to map view
			Login.this.startActivity(map);
			Login.this.finish();
			finish();
		}
	}
}