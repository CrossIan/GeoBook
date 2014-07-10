package com.cse.geobook;

import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;

public class Cache extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener, OnClickListener, OnAccessRevokedListener {
	
	//
	// Start Google+ resources
	private static final String TAG = "Cache";
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
	// End Google+ resources
	//

	LinearLayout view;
	EditText cacheName;
	EditText description;
	TextView cacheLat;
	TextView cacheLong;
	TextView dateVisited;
	Button saveCacheButton,shareCacheButton;
	Data data;

	public static final String FOUND_CACHES = "foundCaches.txt";
	public static final String ALL_CACHES = "PersistentData.txt";
	public static final String TARGET_CACHE = "Target.txt";

	final Double EPISILON = .00001;

	// PHOTO

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cache);
		
		
		saveCacheButton = (Button) this.findViewById(R.id.save_cache_button);
		saveCacheButton.setOnClickListener(this);
		shareCacheButton = (Button) this.findViewById(R.id.share_cache_button);
		shareCacheButton.setOnClickListener(this);
		cacheName = (EditText) this.findViewById(R.id.cacheName);
		description = (EditText) this.findViewById(R.id.cacheDescription);
		cacheLat = (TextView) this.findViewById(R.id.cachelat);
		cacheLong = (TextView) this.findViewById(R.id.cachelong);

		this.getExtras();
		
		
		
		mPlusClient = new PlusClient.Builder(this, this, this)
		.setActions("http://schemas.google.com/BuyActivity")
        .build();
	}
	
	
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.save_cache_button){
			int size = Cache.this.data.allCaches.size();
			boolean searching = true;
			int i = 0;
			while (searching && i < size) {
				MarkerOptions temp = Cache.this.data.allCaches.get(i);
				if (Math.abs(temp.getPosition().latitude
				        - Cache.this.data.target.getPosition().latitude) < Cache.this.EPISILON
				        && Math.abs(temp.getPosition().longitude
				                - Cache.this.data.target.getPosition().longitude) < Cache.this.EPISILON) {
					temp.title(Cache.this.cacheName.getText().toString());
					temp.snippet(Cache.this.description.getText()
					        .toString());
					searching = false;
					Cache.this.data.foundCaches.add(temp);
					Cache.this.data.allCaches.remove(i);
	
				}
				i++;
			}
			if (!searching) {
				Log.d("data", "marker found");
			} else {
				Log.d("data", "marker not found");
			}
	
			DataParser found = new DataParser(Cache.this
			        .getApplicationContext(), Cache.FOUND_CACHES);
			found.overwriteAll(Cache.this.data.foundCaches);
			found.close();
	
			DataParser all = new DataParser(Cache.this
			        .getApplicationContext(), Cache.ALL_CACHES);
			all.overwriteAll(Cache.this.data.allCaches);
			all.close();
	
			/*
			 * Bundle extras_new = new Bundle();
			 * extras_new.putParcelable(Data.CACHE_DATA, data);
			 * 
			 * Intent map = new Intent("android.intent.action.MAP");
			 * map.putExtras(extras_new); startActivity(map);
			 */
			Cache.this.finish();
		}
		/*
		 * Share on social media
		 */
		else if(v.getId() == R.id.share_cache_button){
			// Create the share dialog
			Log.d("Cache", "Tapped share_cache_button in Cache.java");
			final Dialog shareDialog = new Dialog(this);
			shareDialog.setTitle("Share with:");
			shareDialog.setContentView(R.layout.share_dialog);
			
			// Link widgets
			Button GoogleShareButton = (Button) shareDialog.findViewById(R.id.google_share_button);
			Button FacebookShareButton = (Button) shareDialog.findViewById(R.id.facebook_share_button);
			Button TwitterShareButton = (Button) shareDialog.findViewById(R.id.twitter_share_button);
			
			// Set click actions
			// Google+
			GoogleShareButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					shareDialog.dismiss();
//					Toast.makeText(Cache.this, "Tapped Google+ share",
//							Toast.LENGTH_SHORT).show();
					mConnectionProgressDialog = new ProgressDialog(Cache.this);
					mConnectionProgressDialog.setMessage("Signing in with Google+...");
					
					if (!mPlusClient.isConnected()) {
						// Show the dialog as we are now signing in.
						mConnectionProgressDialog.show();
						// Make sure that we will start the resolution (e.g. fire the
						// intent and pop up a dialog for the user) for any errors
						// that come in.
						mResolveOnFail = true;
						// We should always have a connection result ready to resolve,
						// so we can start that process.
						if (mConnectionResult != null) {
							startResolution();
						} else {
							// If we don't have one though, we can start connect in
							// order to retrieve one.
							mPlusClient.connect();
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (mPlusClient.isConnected()) {
						// Construct share text
						String shareText = String.format(
								"Ryan found a new cache using GeoBook!\n"
										+ "Cache Name:\t%s\n"
										+ "Coordinates:\t%s, %s\n",
								cacheName.getText(), cacheLat.getText(),
								cacheLong.getText());

						// Intent shareIntent = new
						// PlusShare.Builder(Cache.this)
						// .setType("text/plain")
						// .setText(shareText)
						// .setContentUrl(Uri.parse("https://developers.google.com/+/"))
						// .getIntent();

						Intent shareIntent = ShareCompat.IntentBuilder.from(Cache.this)
								   .setText(shareText)
								   .setType("image/*")
								   .getIntent()
								   .setPackage("com.google.android.apps.plus");
						
						startActivityForResult(shareIntent, 0);
					}
				}
			});
			// Facebook
			FacebookShareButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(Cache.this, "Tapped Facebook share",
							Toast.LENGTH_SHORT).show();
				}
			});
			// Twitter actions
			TwitterShareButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(Cache.this, "Tapped Twitter share",
							Toast.LENGTH_SHORT).show();
				}
			});
			
			// Show share dialog
			shareDialog.show();
		}
	}
	
	
	
	
	
	
	
	
	

	private void getExtras() {
		Bundle extras = this.getIntent().getExtras();
		this.data = extras.getParcelable(Data.CACHE_DATA);
		this.cacheName.setText(this.data.target.getTitle());
		this.description.setText(this.data.target.getSnippet());
		this.cacheLat.setText(Double.toString(this.data.target.getPosition().latitude));
		this.cacheLong.setText(Double.toString(this.data.target.getPosition().longitude));
	}
	
	
	
	
	
	
	
	
	/*
	 * Google+ callback methods
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.v(TAG, "ConnectionFailed");
		// Most of the time, the connection will fail with a
		// user resolvable result. We can store that in our
		// mConnectionResult property ready for to be used
		// when the user clicks the sign-in button.
		if (result.hasResolution()) {
			mConnectionResult = result;
			if (mResolveOnFail) {
				// This is a local helper function that starts
				// the resolution of the problem, which may be
				// showing the user an account chooser or similar.
				startResolution();
			}
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		// Yay! We can get the oAuth 2.0 access token we are using.
		Log.v(TAG, "Connected to Google+");

		// Turn off the flag, so if the user signs out they'll have to
		// tap to sign in again.
		mResolveOnFail = false;

		// Hide the progress dialog if its showing.
		mConnectionProgressDialog.dismiss();

		// Retrieve the oAuth 2.0 access token.
		final Context context = this.getApplicationContext();
		AsyncTask task = new AsyncTask() {
			@Override
			protected Object doInBackground(Object... params) {
				String scope = "oauth2:" + Scopes.PLUS_LOGIN;
				try {
					// We can retrieve the token to check via
					// tokeninfo or to pass to a service-side
					// application.
					String token = GoogleAuthUtil.getToken(context,
							mPlusClient.getAccountName(), scope);
				} catch (UserRecoverableAuthException e) {
					// This error is recoverable, so we could fix this
					// by displaying the intent to the user.
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (GoogleAuthException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		task.execute((Void) null);
	}

	@Override
	public void onDisconnected() {
		// Bye!
		Log.v(TAG, "Disconnected from Google+");
	}

	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		Log.v(TAG, "ActivityResult: " + requestCode);
		if (requestCode == OUR_REQUEST_CODE && responseCode == RESULT_OK) {
			// If we have a successful result, we will want to be able to
			// resolve any further errors, so turn on resolution with our
			// flag.
			mResolveOnFail = true;
			// If we have a successful result, lets call connect() again. If
			// there are any more errors to resolve we'll get our
			// onConnectionFailed, but if not, we'll get onConnected.
			mPlusClient.connect();
		} else if (requestCode == OUR_REQUEST_CODE && responseCode != RESULT_OK) {
			// If we've got an error we can't resolve, we're no
			// longer in the midst of signing in, so we can stop
			// the progress spinner.
			mConnectionProgressDialog.dismiss();
		}
	}
	
	@Override
	public void onAccessRevoked(ConnectionResult status) {
		// mPlusClient is now disconnected and access has been revoked.
		// We should now delete any data we need to comply with the
		// developer properties. To reset ourselves to the original state,
		// we should now connect again. We don't have to disconnect as that
		// happens as part of the call.
		mPlusClient.connect();
	}

	/**
	 * A helper method to flip the mResolveOnFail flag and start the resolution
	 * of the ConnenctionResult from the failed connect() call.
	 */
	private void startResolution() {
		try {
			// Don't start another resolution now until we have a
			// result from the activity we're about to start.
			mResolveOnFail = false;
			// If we can resolve the error, then call start resolution
			// and pass it an integer tag we can use to track. This means
			// that when we get the onActivityResult callback we'll know
			// its from being started here.
			mConnectionResult.startResolutionForResult(this, OUR_REQUEST_CODE);
		} catch (SendIntentException e) {
			// Any problems, just try to connect() again so we get a new
			// ConnectionResult.
			mPlusClient.connect();
		}
	}
}
