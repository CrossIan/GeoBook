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
	int lastComma;

	DataParser(Context c, int id) {
		InputStream stream = c.getResources().openRawResource(id);
		BufferedReader readr = new BufferedReader(new InputStreamReader(stream));

		this.reader = readr;
	}

	double getLat() {
		Double lat = 0.0;
		try {
			line = reader.readLine();
			this.lastComma = line.indexOf(",");
			lat = Double.parseDouble((String) line.substring(0, lastComma));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lat;

	}

	/* getLat() must be called first!! */
	double getLng() {
		Double lng = Double.parseDouble(line.substring(lastComma + 1,
				line.length()));
		return lng;
	}

	Cache getCache() {
		Cache cache = new Cache();
		return cache;
	}

	void writeCache() {
		// TODO: save cache
	}

	boolean ready() {
		boolean ready = false;
		try {
			ready = reader.ready();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ready;
	}

}
