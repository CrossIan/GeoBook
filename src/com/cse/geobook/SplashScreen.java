package com.cse.geobook;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreen extends Activity {
	// Variables that handle splash screen duration
	protected boolean active = true;
	protected int splashTime = 3500;
	protected int timeIncrement = 100;
	protected int sleepTime = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		// Thread for displaying the splash screen
		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					/*
					 * This section periodically checks if time has run out. It
					 * checks every 100ms to save resources. The splash screen
					 * is terminated when time runs out of the user taps the
					 * screen.
					 */
					int elapsedTime = 0;
					while (active && (elapsedTime < splashTime)) {
						sleep(sleepTime);
						if (active)
							elapsedTime = elapsedTime + timeIncrement;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					finish();
					// TODO: change to .Login
					//startActivity(new Intent("android.intent.action.MAP"));
					startActivity(new Intent(SplashScreen.this,
							Login.class));
				}
			}
		};
		splashThread.start();
	}

	/*
	 * If touched before the count down is complete, move to the next screen.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			active = false;
		}
		return true;
	}
}
