package com.cse.geobook;

import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;

public class Profile extends Activity {

	private static final String TAG = "Profile.java";

	ProgressBar progressBar;
	Data caches;
	private Bundle extra;
	private Person currentPerson;
	private String userName, currentCity,
	        currentState;
	private ImageView profilePicView;
	private String profilePicUrl;
	private TextView userNameText, locationText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.profile);
		this.getExtras();

		// Link widgets
		this.progressBar = (ProgressBar) this
		        .findViewById(R.id.cache_progress_bar);
		this.profilePicView = (ImageView) this.findViewById(R.id.profile_pic);
		this.userNameText = (TextView) this.findViewById(R.id.user_name);
		this.locationText = (TextView) this.findViewById(R.id.location_text);

		// Set widget values
		this.userNameText.setText(this.userName);
		if(this.currentState.equals("")) {
	        this.locationText.setText(this.currentCity);
        } else {
	        this.locationText.setText(this.currentCity + ", " + this.currentState);
        }

		this.userNameText.setText(this.userName);
		this.locationText.setText(this.currentCity + ", " + this.currentState);


		this.profilePicUrl = this.profilePicUrl.substring(0,
		        this.profilePicUrl.length() - 2) + 400;

		new LoadProfileImage(this.profilePicView).execute(this.profilePicUrl);
		// profilePicView.setImageURI(profilePicImage.);

		this.setProgress();

	}

	private void getExtras() {
		this.extra = this.getIntent().getExtras();
		this.currentPerson = this.extra.getParcelable("USER");
		this.userName = this.currentPerson.getName().getGivenName();
		this.currentCity = this.extra.getString("CITY");
		this.currentState = this.extra.getString("STATE");
		this.caches = this.extra.getParcelable(Data.CACHE_DATA);

		this.profilePicUrl = this.currentPerson.getImage().getUrl();
	}

	private void setProgress() {
		int numFound = this.caches.foundCaches.size();
		int numTotal = this.caches.allCaches.size();

		this.progressBar.setProgress(numFound);
		this.progressBar.setMax(numTotal);
	}

	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public LoadProfileImage(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		@Override
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

		@Override
		protected void onPostExecute(Bitmap result) {
			this.bmImage.setImageBitmap(result);
		}
	}
}
