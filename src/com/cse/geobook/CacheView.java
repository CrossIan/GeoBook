package com.cse.geobook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;

public class CacheView extends Activity implements OnClickListener {

	private static final String TAG = "CacheView.java";

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
	}

	@Override
	public void onClick(View v) {
		/*
		 * Share on social media
		 */
		if (v.getId() == R.id.share_cache_button) {
			// Create the share dialog
			Log.d(TAG, "Tapped share_cache_button");
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

		cacheName = extras.getString("NAME");
		cachePlacedBy = extras.getString("PLACEDBY");
		cacheDateFound = extras.getString("DATE");
		cacheLat = extras.getDouble("LAT");
		cacheLng = extras.getDouble("LNG");
		cacheDifficulty = extras.getDouble("DIFF");
		cacheTerrain = extras.getDouble("TERR");
		cacheAwesomeness = extras.getDouble("AWES");
		cacheSize = extras.getDouble("SIZE");
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
	 * Perform actions according to which activity is returning a result.
	 */
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		Log.v(TAG, "ActivityResult: " + requestCode);
		// Photo activity result SUCCESS
		if (requestCode == PHOTO_REQUEST_CODE && responseCode == RESULT_OK) {
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

		}

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
