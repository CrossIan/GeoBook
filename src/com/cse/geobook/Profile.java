package com.cse.geobook;

import java.io.InputStream;

import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.Image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Profile extends Activity {

	private static final String TAG = "Profile.java";

	ProgressBar progressBar;
	private Bundle extra;
	private Person currentPerson;
	private String userName,cacheName,cachePlacedBy,dateFound,
					currentCity,currentState;
	private ImageView profilePicView,cachePicView;
	private String profilePicUrl;
	private TextView userNameText,locationText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		this.getExtras();

		// Link widgets
		progressBar = (ProgressBar) findViewById(R.id.cache_progress_bar);
		profilePicView = (ImageView) findViewById(R.id.profile_pic);
		cachePicView = (ImageView)	findViewById(R.id.cache_image);
		userNameText = (TextView) findViewById(R.id.user_name);
		locationText = (TextView) findViewById(R.id.location_text);

		// Set widget values
		userNameText.setText(userName);
		locationText.setText(currentCity + ", " + this.currentState);

		profilePicUrl = profilePicUrl.substring(0,
				profilePicUrl.length() - 2)
                + 400;

        new LoadProfileImage(profilePicView).execute(profilePicUrl);
//		profilePicView.setImageURI(profilePicImage.);
		this.setProgress(35, 100);

	}

	private void getExtras() {
		this.extra = this.getIntent().getExtras();
		currentPerson = extra.getParcelable("USER");
		userName = currentPerson.getName().getGivenName();
		currentCity = extra.getString("CITY");
		currentState = extra.getString("STATE");

		profilePicUrl = currentPerson.getImage().getUrl();
	}

	private void setProgress(int progress, int max) {
		this.progressBar.setProgress(progress);
		this.progressBar.setMax(max);
	}

	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
}