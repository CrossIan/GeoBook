package com.cse.geobook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class DataParser {

	/*
	 * Stream passed should be getResources().openRawResources(R.raw.*) where *
	 * is the id of the file
	 */
	BufferedReader reader;
	String line;
	int firstComma = 11;
	int secondComma = 23;

	DataParser(Context c, int id) {
		InputStream stream = c.getResources().openRawResource(id);
		BufferedReader readr = new BufferedReader(new InputStreamReader(stream));

		this.reader = readr;
	}

	double getLat() {
		Double lat = 0.0;
		try {
			this.line = this.reader.readLine();
			lat = Double.parseDouble(this.line.substring(0, this.firstComma));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lat;

	}

	/* getLat() must be called first!! */
	double getLng() {
		Double lng = Double.parseDouble(this.line.substring(
		        this.firstComma + 1, this.secondComma));
		return lng;
	}

	String getName() {
		String name = this.line.substring(this.secondComma + 1,
		        this.line.length());
		return name;
	}

	// Cache getCache() {
	// Cache cache = new Cache();
	// return cache;
	// }

	void writeCache() {
		// TODO: save cache
	}

	boolean ready() {
		boolean ready = false;
		try {
			ready = this.reader.ready();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ready;
	}

}
