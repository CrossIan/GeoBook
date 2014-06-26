package com.cse.geobook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	
	// Widget sockets we'll use later
	private Button signInButton;
	private RadioGroup signInOption;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Init Widget Button and set click listener
		signInButton = (Button) findViewById(R.id.signInButton);
		signInButton.setOnClickListener(this);
	}

	
	
	
	
	@Override
	public void onClick(View v) {
		if (v == signInButton) {

			// Create Object of Dialog class
			final Dialog login = new Dialog(this);
			// Set GUI of login screen
			login.setContentView(R.layout.login_dialog);
			login.setTitle("Login to Pulse 7");

			// Init button of login GUI
			Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
			Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
			final EditText txtUsername = (EditText) login
					.findViewById(R.id.txtUsername);
			final EditText txtPassword = (EditText) login
					.findViewById(R.id.txtPassword);

			// Attached listener for login GUI button
			btnLogin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (txtUsername.getText().toString().trim().length() > 0
							&& txtPassword.getText().toString().trim().length() > 0) {
						// Validate Your login credential here than display
						// message
						Toast.makeText(Login.this,
								"Login Sucessfull", Toast.LENGTH_LONG).show();

						// Redirect to dashboard / home screen.
						login.dismiss();
					} else {
						Toast.makeText(Login.this,
								"Please enter Username and Password",
								Toast.LENGTH_LONG).show();

					}
				}
			});
			btnCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					login.dismiss();
				}
			});

			// Make dialog box visible.
			login.show();
		}
	}
}

// public class Login extends Activity {
//
//
//
// // Widget sockets we'll use later
// private Button signInButton;
// private RadioGroup signInOption;
//
// @Override
// protected void onCreate(Bundle savedInstanceState) {
// super.onCreate(savedInstanceState);
// this.setContentView(R.layout.login);
//
// // Link to widgets
// signInButton = (Button) this.findViewById(R.id.signInButton);
// signInOption = (RadioGroup) this.findViewById(R.id.loginRadio);
//
// // Set action for onClick
// signInButton.setOnClickListener(new View.OnClickListener() {
// @Override
// public void onClick(View v) {
// int option = signInOption.getCheckedRadioButtonId();
// option = option - signInOption.getId();
// String dialogTitle, dialogMessage;
// switch (option) {
// case 1:
// // Google+
// dialogTitle = "Sign in with Google+";
// dialogMessage = "Proceed to Google+ sign in?";
// break;
// case 2:
// // Facebook
// dialogTitle = "Sign in with Facebook";
// dialogMessage = "Proceed to Facebook sign in?";
// break;
// case 3:
// // Twitter
// dialogTitle = "Sign in with Twitter";
// dialogMessage = "Proceed to Twitter sign in?";
// break;
// default:
// dialogTitle = "Error in Login.java";
// dialogMessage = "Error in Login.java";
// Log.e("", "Error in Login.java");
// }
//
// AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
// Login.this);
// // set title
// alertDialogBuilder.setTitle(dialogTitle);
//
// // set dialog message
// alertDialogBuilder
// .setMessage(dialogMessage)
// .setCancelable(false)
// .setPositiveButton("Yes",
// new DialogInterface.OnClickListener() {
// @Override
// public void onClick(DialogInterface dialog,
// int id) {
// // Move to the map viewer
//
// Intent map = new Intent(
// "android.intent.action.MAP");
// Bundle extra = new Bundle();
// DataParser reader = new DataParser(
// getApplicationContext());
//
// Data data = reader.read();
// extra.putParcelable(Data.CACHE_DATA,
// data);
// map.putExtras(extra);
//
// Login.this.startActivity(map);
// Login.this.finish();
// finish();
//
// }
// })
// .setNegativeButton("No",
// new DialogInterface.OnClickListener() {
// @Override
// public void onClick(DialogInterface dialog,
// int id) {
// // if this button is clicked, just close
// // the dialog box and do nothing
// dialog.cancel();
// }
// });
//
// // create alert dialog
// AlertDialog alertDialog = alertDialogBuilder.create();
//
// // show it
// alertDialog.show();
//
// }
// });
//
// }
// }
