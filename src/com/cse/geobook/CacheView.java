package com.cse.geobook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusClient.OnAccessRevokedListener;
import com.google.android.gms.plus.model.people.Person;

public class CacheView extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener, OnClickListener, OnAccessRevokedListener {

	private static final String TAG = "CacheView.java";

	//
	// Start Google+ resources
	private static final int GOOGLE_REQUEST_CODE = 49404; // What to listen for
															// in
															// onActivityResult
	private static final int GOOGLE_SHARE_CODE = 49405;
	private PlusClient mPlusClient; // The core Google+ client.
	private boolean mResolveOnFail; // A flag to stop multiple dialogues
									// appearing for the user.
	private ConnectionResult mConnectionResult; // Store the result of a failed
												// connect()
	private ProgressDialog mConnectionProgressDialog; // Sign in progress dialog
	// End Google+ resources

	// Resources for photo functionality
	private static final int PHOTO_REQUEST_CODE = 6969;
	private static final int PHOTO_SHARE_REQUEST_CODE = 123321;

	//
	// Data for this cache
	public Person currentPerson;
	public String userName, cacheName, cachePlacedBy, cacheDateFound,
			userDescription;
	public Double cacheLat, cacheLng, cacheDifficulty, cacheTerrain,
			cacheAwesomeness, cacheSize, distanceFrom;
	public String mCurrentPhotoPath; // The absolute path to the caches photo
	public File mCurrentPhoto;
	public Data data;
	public Double distThreshold = 20000.0; // 20km

	//
	// Layout widgets
	ImageView cacheThumbnail;
	TextView cacheNameText, cachePlacedByText, dateVisited, cacheDateFoundText,
			cacheDifficultyText, cacheTerrainText, cacheAwesomenessText,
			cacheSizeText;
	EditText userDescriptionText;
	Button saveCacheButton, shareCacheButton, captureImageButton,
			foundCacheButton;

	final Double EPISILON = .00001;

	boolean cacheHasBeenFound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cache);
		Log.d(TAG, "onCreate");

		// Link widgets
		cacheThumbnail = (ImageView) this.findViewById(R.id.cache_thumbnail);
		shareCacheButton = (Button) this.findViewById(R.id.share_cache_button);
		shareCacheButton.setOnClickListener(this);
		captureImageButton = (Button) this
				.findViewById(R.id.capture_image_button);
		captureImageButton.setOnClickListener(this);
		foundCacheButton = (Button) this.findViewById(R.id.found_cache_button);
		foundCacheButton.setOnClickListener(this);
		cacheNameText = (TextView) this.findViewById(R.id.cache_name);
		cachePlacedByText = (TextView) this.findViewById(R.id.cache_placed_by);
		cacheDateFoundText = (TextView) this.findViewById(R.id.date_found);
		cacheDifficultyText = (TextView) this
				.findViewById(R.id.cache_difficulty);
		cacheTerrainText = (TextView) this.findViewById(R.id.cache_terrain);
		cacheAwesomenessText = (TextView) this
				.findViewById(R.id.cache_awesomeness);
		cacheSizeText = (TextView) this.findViewById(R.id.cache_size);
		userDescriptionText = (EditText) this
				.findViewById(R.id.user_description);

		// Get extras from originating activity
		this.getExtras();
		mCurrentPhotoPath = "";

		// Determine if we're close enough to have found the cache
		if (distanceFrom <= distThreshold) {
			foundCacheButton.setVisibility(View.VISIBLE);
			cacheHasBeenFound = true;
		} else {
			foundCacheButton.setVisibility(View.INVISIBLE);
			cacheHasBeenFound = false;
		}
		// Initialize the Google+ client
		mPlusClient = new PlusClient.Builder(this, this, this).setActions(
				"http://schemas.google.com/BuyActivity").build();
	}

	@Override
	public void onClick(View v) {
		/*
		 * Share on social media
		 */
		if (v.getId() == R.id.share_cache_button) {
			// Create the share dialog
			Log.d(TAG, "Tapped share_cache_button");

			// Show the sign-in dialog
			mConnectionProgressDialog = new ProgressDialog(CacheView.this);
			mConnectionProgressDialog.setMessage("Signing in with Google+...");

			if (!mPlusClient.isConnected()) {
				// Show the dialog as we are now signing in.
				mConnectionProgressDialog.show();
				// Make sure that we will start the resolution (e.g. fire
				// the intent and pop up a dialog for the user) for any
				// errors the come in.
				mResolveOnFail = true;
				// We should always have a connection result ready to
				// resolve, so we can start that process.
				if (mConnectionResult != null) {
					startResolution();
				} else {
					// If we don't have one though, we can start connect
					// in order to retrieve one.
					mPlusClient.connect();
				}
			}
		}
		/*
		 * Take a picture of this cache
		 */
		else if (v.getId() == R.id.capture_image_button) {
			Log.d(TAG, "Tapped capture_image_button");
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// Ensure that there's a camera activity to handle the intent
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, PHOTO_REQUEST_CODE);
			}
		}
		/*
		 * Found the cache
		 */
		else if (v.getId() == R.id.found_cache_button) {
			Log.d(TAG, "Tapped found_cache_button");
			Toast.makeText(this, "Found cache!", Toast.LENGTH_SHORT).show();

			// Take picture
			Intent takePictureIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			// Ensure that there's a camera activity to handle the intent
			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, PHOTO_SHARE_REQUEST_CODE);
			}
		}
	}

	/*
	 * Load extras from parent activity
	 */
	private void getExtras() {
		Bundle extras = this.getIntent().getExtras();
		this.data = extras.getParcelable(Data.CACHE_DATA);

		cacheName = data.target.getName();
		cachePlacedBy = data.target.getCreator();
		cacheDateFound = data.target.getPhoto();
		cacheLat = data.target.getLat();
		cacheLng = data.target.getLng();
		cacheDifficulty = data.target.getDifficulty();
		cacheTerrain = data.target.getTerrain();
		cacheAwesomeness = data.target.getRating();
		cacheSize = data.target.getContainer();
		currentPerson = extras.getParcelable("USER");
		if (currentPerson != null)
			userName = currentPerson.getName().getGivenName();
		else
			userName = "Teddy Tester";
		distanceFrom = extras.getDouble("DISTANCE");

		// Set widget text
		cacheNameText.setText(cacheName);
		cachePlacedByText.setText("Placed by: \"" + cachePlacedBy + "\"");
		cacheDateFoundText.setText("Date found: " + cacheDateFound);
		cacheDifficultyText.setText(Double.toString(cacheDifficulty));
		cacheTerrainText.setText(Double.toString(cacheTerrain));
		cacheAwesomenessText.setText(Double.toString(cacheAwesomeness));
		cacheSizeText.setText(Double.toString(cacheSize));
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

	/*
	 * When connected, get the oAuth 2.0 token, then
	 */
	@Override
	public void onConnected(Bundle bundle) {
		// Yay! We can get the oAuth 2.0 access token we are using.
		Log.v(TAG, "Connected to Google+");
		Toast.makeText(this, "Signed in to Google+", Toast.LENGTH_SHORT).show();

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

		// Construct share text
		shareCacheToGoogle();
		// Disconnect when finished sharing
		mPlusClient.disconnect();
	}

	@Override
	public void onDisconnected() {
		// Bye!
		Log.v(TAG, "Disconnected from Google+");
	}

	/*
	 * Perform actions according to which activity is returning a result.
	 */
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		Log.v(TAG, "ActivityResult: " + requestCode);
		//
		// Google+ request result SUCCESS
		if (requestCode == GOOGLE_REQUEST_CODE && responseCode == RESULT_OK) {
			Log.d(TAG, "Google+ sign in returned OK.");
			// If we have a successful result, we will want to be able to
			// resolve any further errors, so turn on resolution with our
			// flag.
			mResolveOnFail = true;
			// If we have a successful result, lets call connect() again. If
			// there are any more errors to resolve we'll get our
			// onConnectionFailed, but if not, we'll get onConnected.
			mPlusClient.connect();
			//
			// Google+ request result FAILED
		} else if (requestCode == GOOGLE_REQUEST_CODE
				&& responseCode != RESULT_OK) {
			Log.d(TAG, "Google+ sign in returned NOT OK.");
			// If we've got an error we can't resolve, we're no
			// longer in the midst of signing in, so we can stop
			// the progress spinner.
			mConnectionProgressDialog.dismiss();
			//
			// Google+ share result SUCCESS
		} else if (requestCode == GOOGLE_SHARE_CODE
				&& responseCode == RESULT_OK) {
			Log.d(TAG, "Share activity returned OK.");
			//
			// Photo activity result SUCCESS
		} else if (requestCode == PHOTO_REQUEST_CODE
				&& responseCode == RESULT_OK) {
			Log.d(TAG, "Photo activity returned OK.");

			// Change thumbnail
			Bundle extras = intent.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			cacheThumbnail.setImageBitmap(imageBitmap);

			//
			// Save picture to our directory
			//
			// Get path to our picture folder. Create Geobook directory
			// if necessary
			File path = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"Geobook/");
			if (!path.mkdirs()) {
				Log.e(TAG, "Directory not created");
			}

			// Get the new photos name and add to path
			String fileName = getPhotoName();
			File file = new File(path, fileName);
			Log.d(TAG, file.getAbsolutePath());
			// Write image to file
			try {
				FileOutputStream fOut = new FileOutputStream(file);
				imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// On success, record the file name to this cache
			mCurrentPhoto = file;
			String[] dateInfo = fileName.split("_");
			cacheDateFound = dateInfo[2];
			final String OLD_FORMAT = "yyyyMMdd";
			final String NEW_FORMAT = "dd/MM/yyyy";

			SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
			Date d;
			try {
				d = sdf.parse(dateInfo[2]);
			} catch (ParseException e) {
				e.printStackTrace();
				d = null;
			}
			sdf.applyPattern(NEW_FORMAT);
			cacheDateFound = sdf.format(d);
			cacheDateFoundText.setText("Date found: " + cacheDateFound);

			//
			// P
		} else if (requestCode == PHOTO_SHARE_REQUEST_CODE
				&& responseCode == RESULT_OK) {
			Log.d(TAG, "Photo activity returned OK.");

			// Change thumbnail
			Bundle extras = intent.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			cacheThumbnail.setImageBitmap(imageBitmap);

			//
			// Save picture to our directory
			//
			// Get path to our picture folder. Create Geobook directory
			// if necessary
			File path = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"Geobook/");
			if (!path.mkdirs()) {
				Log.e(TAG, "Directory not created");
			}

			// Get the new photos name and add to path
			String fileName = getPhotoName();
			File file = new File(path, fileName);
			Log.d(TAG, file.getAbsolutePath());
			// Write image to file
			try {
				FileOutputStream fOut = new FileOutputStream(file);
				imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// On success, record the file name to this cache
			mCurrentPhoto = file;

			// Post to Google+
			// Show the sign-in dialog
			mConnectionProgressDialog = new ProgressDialog(CacheView.this);
			mConnectionProgressDialog.setMessage("Signing in with Google+...");

			if (!mPlusClient.isConnected()) {
				// Show the dialog as we are now signing in.
				mConnectionProgressDialog.show();
				// Make sure that we will start the resolution (e.g. fire
				// the intent and pop up a dialog for the user) for any
				// errors the come in.
				mResolveOnFail = true;
				// We should always have a connection result ready to
				// resolve, so we can start that process.
				if (mConnectionResult != null) {
					startResolution();
				} else {
					// If we don't have one though, we can start connect
					// in order to retrieve one.
					mPlusClient.connect();
				}
			}
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

	/*
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
			mConnectionResult.startResolutionForResult(this,
					GOOGLE_REQUEST_CODE);
		} catch (SendIntentException e) {
			// Any problems, just try to connect() again so we get a new
			// ConnectionResult.
			mPlusClient.connect();
		}
	}

	/*
	 * Generates an intent that shares the current cache to Google+.
	 */
	private void shareCacheToGoogle() {
		userDescription = userDescriptionText.getText().toString();
		Log.d("CacheView.java", userDescription);
		String shareText;

		if (userDescription.length() > 1) {
			shareText = String.format("%s found a new cache using GeoBook!\n\n"
					+ "Cache Name:  %s\n" + "Placed By:  %s\n"
					+ "Coordinates:  %2.6f, %2.6f\n\n" + "Difficulty:  %1.1f\n"
					+ "Terrain:  %1.1f\n" + "Awesomeness:  %1.1f\n"
					+ "Size:  %1.1f\n\n" + "User notes:  %s\n", userName,
					cacheName, cachePlacedBy, cacheLat, cacheLng,
					cacheDifficulty, cacheTerrain, cacheAwesomeness, cacheSize,
					userDescription);
		} else {
			shareText = String.format("%s found a new cache using GeoBook!\n\n"
					+ "Cache Name:  %s\n" + "Placed By:  %s\n"
					+ "Coordinates:  %2.6f, %2.6f\n\n" + "Difficulty:  %1.1f\n"
					+ "Terrain:  %1.1f\n" + "Awesomeness:  %1.1f\n"
					+ "Size:  %1.1f\n\n", userName, cacheName, cachePlacedBy,
					cacheLat, cacheLng, cacheDifficulty, cacheTerrain,
					cacheAwesomeness, cacheSize);
		}

		// Build the share intent
		Intent shareIntent;
		if (mCurrentPhoto != null && mCurrentPhoto.exists()) {
			Uri contentUri = Uri.fromFile(mCurrentPhoto);
			shareIntent = ShareCompat.IntentBuilder.from(CacheView.this)
					.setStream(contentUri).setText(shareText)
					.setType("image/jpg").getIntent()
					.setPackage("com.google.android.apps.plus");
		} else {
			shareIntent = ShareCompat.IntentBuilder.from(CacheView.this)
					.setText(shareText).setType("image/*").getIntent()
					.setPackage("com.google.android.apps.plus");
		}

		// Start Google+ with share intent
		startActivityForResult(shareIntent, GOOGLE_SHARE_CODE);
	}

	/**
	 * A helper method to create a unique file name for the current photo.
	 * 
	 * @return image- a file that contains the image.
	 * @throws IOException
	 */
	private String getPhotoName() {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "Geobook_PNG_" + timeStamp + ".png";

		return imageFileName;
	}

	@Override
	protected void onPause() {
		super.onPause();

		// saves found caches if it exists && target
		boolean cacheIsInFound = false;
		int size = data.foundCaches.size();
		for (int i = 0; i < size && !cacheIsInFound; i++) {
			if (data.target.equals(data.foundCaches.get(i))) {
				data.foundCaches.set(i, data.target);
				cacheIsInFound = true;
			}
		}
		// adds the cache to found if it did not exist and you are within range
		if (!cacheIsInFound && cacheHasBeenFound) {
			data.foundCaches.add(data.target);
			cacheIsInFound = true;
		}

		// overwrites the found caches only if cacheIsInFound
		if (cacheIsInFound) {
			DataParser found = new DataParser(
					CacheView.this.getApplicationContext(), Cache.FOUND_CACHES);
			found.overwriteAll(CacheView.this.data.foundCaches);
			found.close();
		}

		// overwrites target cache always
		DataParser target_dp = new DataParser(
				CacheView.this.getApplicationContext(), Cache.TARGET_CACHE);

		ArrayList<Cache> local_target = new ArrayList<Cache>();
		local_target.add(data.target);
		target_dp.overwriteAll(local_target);
		target_dp.close();
	}

}
