package com.cse.geobook;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Photo extends Activity implements OnClickListener {
	Button bCapture;
	ImageView ivPhoto;
	Intent intent;
	Bitmap image;
	int data = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bCapture = (Button) findViewById(R.id.bCapture);
		ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bCapture:
			intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, data);
		default:
			if (image != null) {
				ivPhoto.setImageBitmap(image);
			}
		}

	}

	/* Taken from the course slides lec6_internal_services pg 16 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent cameraIntent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, cameraIntent);
		if (resultCode == RESULT_OK) {
			Bundle extras = cameraIntent.getExtras();
			image = (Bitmap) extras.get("data");
			ivPhoto.setImageBitmap(image);
		}
	}

	/* Modified from teh course slides lec6_internal_services pg 16 */

	protected void onPause() {
		Log.d("ActivityShowImage", "Pausing...");
		super.onPause();
		System.gc();
	}
}
