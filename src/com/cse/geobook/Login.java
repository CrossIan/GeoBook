package com.cse.geobook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		
		
		final Button button = (Button) findViewById(R.id.signButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
                // Move to the map viewer
            	startActivity(new Intent("android.intent.action.MAP"));
            }
        });

	}
	
	
	
	
	
	
	
}
