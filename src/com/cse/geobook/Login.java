package com.cse.geobook;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
	// Request code used to invoke sign in user interactions.
	private static final int RC_SIGN_IN = 0;
	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;
	// A flag indicating that a PendingIntent is in progress 
	// and prevents us from starting further intents.
	private boolean mIntentInProgress;

	
	// -------------------------------------------------------*
	// Facebook login resources
	// -------------------------------------------------------*
	

	
	// -------------------------------------------------------*
	// Twitter login resources
	// -------------------------------------------------------*
	

	
	
	// -------------------------------------------------------*
	// Generic resources
	// -------------------------------------------------------*
	private Button googleButton, 
				   facebookButton,
				   twitterButton,
				   bypassButton;
//	private RadioGroup signInOption;

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Initialize buttons click listeners
		findViewById(R.id.google_sign_in_button).setOnClickListener(this);
//		facebookButton = (Button) findViewById(R.id.bypassButton);
//		facebookButton.setOnClickListener(this);
//		twitterButton = (Button) findViewById(R.id.bypassButton);
//		twitterButton.setOnClickListener(this);
		bypassButton = (Button) findViewById(R.id.bypassButton);
		bypassButton.setOnClickListener(this);
		// signInOption = (RadioGroup) this.findViewById(R.id.loginRadio);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.google_sign_in_button){
			Toast.makeText(Login.this, "Attempting to login with Google+",
					Toast.LENGTH_SHORT).show();
			mGoogleApiClient = new GoogleApiClient.Builder(this)
												  .addConnectionCallbacks(this)
												  .addOnConnectionFailedListener(this).addApi(Plus.API, null)
												  .addScope(Plus.SCOPE_PLUS_LOGIN).build();
		}
		else if (v == bypassButton) {

			Intent map = new Intent("android.intent.action.MAP");
			Bundle extra = new Bundle();
			map.putExtras(extra);
			// Finish login activity and move to map view
			Login.this.startActivity(map);
			Login.this.finish();
			finish();

			// // Create Object of Dialog class
			// final Dialog login = new Dialog(this);
			//
			// int option = signInOption.getCheckedRadioButtonId();
			// option = option - signInOption.getId();
			// String dialogTitle;
			// switch (option) {
			// case 1:
			// // Google+
			// dialogTitle = "Sign in with Google+";
			// break;
			// case 2:
			// // Facebook
			// dialogTitle = "Sign in with Facebook";
			// break;
			// case 3:
			// // Twitter
			// dialogTitle = "Sign in with Twitter";
			// break;
			// default:
			// dialogTitle = "Error in Login.java";
			// Log.e("", "Error in Login.java");
			// }
			// login.setTitle(dialogTitle);
			//
			// // Set GUI of login screen
			// login.setContentView(R.layout.login_dialog);
			//
			// // Init button of login GUI
			// Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
			// Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
			// final EditText txtUsername = (EditText) login
			// .findViewById(R.id.txtUsername);
			// final EditText txtPassword = (EditText) login
			// .findViewById(R.id.txtPassword);
			//
			// // Attached listener for login GUI button
			// btnLogin.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// /*
			// * Try to validate sign in info. Proceed to map activity if
			// * ok.
			// */
			// if (txtUsername.getText().toString().trim().length() > 0
			// && txtPassword.getText().toString().trim().length() > 0) {
			// // Validate Your login credential here than display
			// // message
			// Toast.makeText(Login.this, "Login Sucessfull",
			// Toast.LENGTH_LONG).show();
			//
			// // Close dialog box
			// login.dismiss();
			// /*
			// * Prepare to launch the map activity.
			// */
			// // TODO Maybe start the map loader in a different
			// // thread?
			// // Seems to work fine now, but could be added at stage
			// // 3.
			// Intent map = new Intent("android.intent.action.MAP");
			// Bundle extra = new Bundle();
			// map.putExtras(extra);
			// // Finish login activity and move to map view
			// Login.this.startActivity(map);
			// Login.this.finish();
			// finish();
			//
			// }
			// /*
			// * No username and/or password entered. Prompt for info
			// */
			// else {
			// Toast.makeText(Login.this,
			// "Please enter Username and Password",
			// Toast.LENGTH_LONG).show();
			//
			// }
			// }
			// });
			// /*
			// * Cancel button listener Go back to the sign in selection view
			// */
			// btnCancel.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// login.dismiss();
			// }
			// });
			//
			// // Make dialog box visible after the user has selected their sign
			// in
			// // method
			// login.show();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
}