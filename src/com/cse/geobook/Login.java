package com.cse.geobook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class Login extends Activity {

	private static final int DIALOG_ALERT = 10;

	// Widget sockets we'll use later
	private Button signInButton;
	private RadioGroup signInOption;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);

		// Link to widgets
		signInButton = (Button) this.findViewById(R.id.signInButton);
		signInOption = (RadioGroup) this.findViewById(R.id.loginRadio);

		// Set action for onClick
		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int option = signInOption.getCheckedRadioButtonId();
				option = option - signInOption.getId();
				String dialogTitle, dialogMessage;
				switch (option) {
				case 1:
					// Google+
					dialogTitle = "Sign in with Google+";
					dialogMessage = "Proceed to Google+ sign in?";
					break;
				case 2:
					// Facebook
					dialogTitle = "Sign in with Facebook";
					dialogMessage = "Proceed to Facebook sign in?";
					break;
				case 3:
					// Twitter
					dialogTitle = "Sign in with Twitter";
					dialogMessage = "Proceed to Twitter sign in?";
					break;
				default:
					dialogTitle = "Error in Login.java";
					dialogMessage = "Error in Login.java";
					Log.e("", "Error in Login.java");
				}

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Login.this);
				// set title
				alertDialogBuilder.setTitle(dialogTitle);

				// set dialog message
				alertDialogBuilder
						.setMessage(dialogMessage)
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										// Move to the map viewer

										Intent map = new Intent(
												"android.intent.action.MAP");
										Bundle extra = new Bundle();
										DataParser reader = new DataParser(
												getApplicationContext(),
												Data.ALL_CACHES);

										Data data = reader.read();
										extra.putParcelable(Data.CACHE_DATA,
												data);
										reader.close();
										map.putExtras(extra);

										Login.this.startActivity(map);
										Login.this.finish();
										finish();

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			}
		});

	}
}
