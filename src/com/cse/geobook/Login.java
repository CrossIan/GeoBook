package com.cse.geobook;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;



public class Login extends Activity implements OnClickListener,
											   ConnectionCallbacks,
										   	   OnConnectionFailedListener {
	
	
	// -------------------------------------------------------*
	// Google+ resources
	// -------------------------------------------------------*
	private GoogleApiClient mGoogleApiClient;	// Client to interact with Google API
	private static final int RC_SIGN_IN = 0;	// Request code used to invoke sign-in user interactions
	private boolean mSignInClicked;				// Track if sign in button has been clicked to resolve 
	private ConnectionResult mConnectionResult;	// Store connection result from onConnectionFailed
	private boolean mIntentInProgress = false;	// Prevents simultaneous sign-in attempts


	
	// -------------------------------------------------------*
	// Facebook login resources
	// -------------------------------------------------------*
	

	
	// -------------------------------------------------------*
	// Twitter login resources
	// -------------------------------------------------------*
	

	
	
	// -------------------------------------------------------*
	// Generic resources
	// -------------------------------------------------------*
	private Button facebookButton,
				   twitterButton,
				   bypassButton;

	
	
	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Initialize buttons click listeners
		findViewById(R.id.google_sign_in_button).setOnClickListener(this);
		facebookButton = (Button) findViewById(R.id.facebookButton);
		facebookButton.setOnClickListener(this);
		twitterButton = (Button) findViewById(R.id.twitterButton);
		twitterButton.setOnClickListener(this);
		bypassButton = (Button) findViewById(R.id.bypassButton);
		bypassButton.setOnClickListener(this);
		
		
		// Initialize Google API client
		mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API, null)
        .addScope(Plus.SCOPE_PLUS_LOGIN)
        .build();
	}
	
	
	protected void onStart(){
		super.onStart();
	}
	
	protected void onStop() {
	    super.onStop();

	    if (mGoogleApiClient.isConnected()) {
	      mGoogleApiClient.disconnect();
	    }
	  }

	

	
	// Define click behavior
	@Override
	public void onClick(View v) {
		//
		// Try to sign in with Google+
		if(v.getId() == R.id.google_sign_in_button){
			Toast.makeText(Login.this, "Attempting to login with Google+",
					Toast.LENGTH_SHORT).show();
			// Try to connect...
			mGoogleApiClient.connect();
		}
		// Try to sign in with Facebook
		else if (v == facebookButton) {
			Toast.makeText(Login.this, "Attempting to login with Facebook (NON-OP)",
					Toast.LENGTH_SHORT).show();
		}
		// Try to sign in with Twitter
		else if (v == twitterButton) {
			Toast.makeText(Login.this, "Attempting to login with Twitter (NON-OP)",
					Toast.LENGTH_SHORT).show();
		}
		//
		// Bypass login (dev only)
		else if (v == bypassButton) {
			Toast.makeText(Login.this, "Bypassing login...",
					Toast.LENGTH_SHORT).show();
			Intent map = new Intent("android.intent.action.MAP");
			Bundle extra = new Bundle();
			map.putExtras(extra);
			// Finish login activity and move to map view
			Login.this.startActivity(map);
			Login.this.finish();
			finish();
		}
	}






	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress && result.hasResolution()) {
			try {
				mIntentInProgress = true;
				result.startResolutionForResult(this, // your activity
                        RC_SIGN_IN);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}






	@Override
	public void onConnected(Bundle connectionHint) {
		// We've resolved any connection errors. mGoogleApiClient can be used to
		// access Google APIs on behalf of the user.
		Toast.makeText(Login.this, "Google+ login successful!",
				Toast.LENGTH_SHORT).show();
		
		// TODO
		// Get user's name and profile picture. Link this to the in-app
		//	hash (generated later
		
		// Start the map
		Intent map = new Intent("android.intent.action.MAP");
		Bundle extra = new Bundle();
		map.putExtras(extra);
		// Finish login activity and move to map view
		Login.this.startActivity(map);
		Login.this.finish();
		finish();
	}







	@Override
	public void onConnectionSuspended(int cause) {
		mGoogleApiClient.connect();
	}
	
	
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		if (requestCode == RC_SIGN_IN) {
			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}
}