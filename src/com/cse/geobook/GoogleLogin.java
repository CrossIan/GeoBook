package com.cse.geobook;

import java.io.IOException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;



public class GoogleLogin extends Activity implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener {

  /* Request code used to invoke sign in user interactions. */
  private static final int RC_SIGN_IN = 0;

  /* Client used to interact with Google APIs. */
  private GoogleApiClient mGoogleApiClient;

  /* A flag indicating that a PendingIntent is in progress and prevents
   * us from starting further intents.
   */
  private boolean mIntentInProgress;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API, null)
        .addScope(Plus.SCOPE_PLUS_LOGIN)
        .build();
  }

  protected void onStart() {
    super.onStart();
    mGoogleApiClient.connect();
  }

  protected void onStop() {
    super.onStop();

    if (mGoogleApiClient.isConnected()) {
      mGoogleApiClient.disconnect();
    }
  }
  
  
  public void onConnectionFailed(ConnectionResult result) {
	  if (!mIntentInProgress && result.hasResolution()) {
	    try {
	      mIntentInProgress = true;
//	      startIntentSenderForResult(result.getIntentSender(),
//	          RC_SIGN_IN, null, 0, 0, 0);
	      result.startResolutionForResult(this, // your activity
                  RC_SIGN_IN);
	    } catch (SendIntentException e) {
	      // The intent was canceled before it was sent.  Return to the default
	      // state and attempt to connect to get an updated ConnectionResult.
	      mIntentInProgress = false;
	      mGoogleApiClient.connect();
	    }
	  }
	}

	public void onConnected(Bundle connectionHint) {
	  // We've resolved any connection errors.  mGoogleApiClient can be used to
	  // access Google APIs on behalf of the user.
	}

	
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		  if (requestCode == RC_SIGN_IN) {
		    mIntentInProgress = false;

		    if (!mGoogleApiClient.isConnecting()) {
		      mGoogleApiClient.connect();
		    }
		  }
		}
	
	
	public void onConnectionSuspended(int cause) {
		  mGoogleApiClient.connect();
		}

  
}