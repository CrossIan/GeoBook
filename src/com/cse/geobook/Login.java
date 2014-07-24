package com.cse.geobook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

public class Login extends Activity implements OnClickListener {

	// -------------------------------------------------------*
	// Google+ resources
	// -------------------------------------------------------*
	private static final String TAG = "Login.java";
	// A magic number we will use to know that our sign-in error
	// resolution activity has completed.
	private static final int OUR_REQUEST_CODE = 49404;
	// The core Google+ client.
	private PlusClient mPlusClient;
	// A flag to stop multiple dialogues appearing for the user.
	private boolean mResolveOnFail;
	// We can store the connection result from a failed connect()
	// attempt in order to make the application feel a bit more
	// responsive for the user.
	private ConnectionResult mConnectionResult;
	// A progress dialog to display when the user is connecting in
	// case there is a delay in any of the dialogs being ready.
	private ProgressDialog mConnectionProgressDialog;

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

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "Stop");
		// It can be a little costly to keep the connection open
		// to Google Play Services, so each time our activity is
		// stopped we should disconnect.
		if (mPlusClient.isConnected())
			mPlusClient.disconnect();
	}

	/*
	 * Gets user Google+ profile info
	 */
	private void retrieveProfileInfo() {
		currentPerson = mPlusClient.getCurrentPerson();
		if (currentPerson.getName().hasGivenName())
			currentUserName = currentPerson.getName().getGivenName();
		else
			currentUserName = "Teddy Tester";
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