package com.cse.geobook;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DataParser {

	/*
	 * Stream passed should be getResources().openRawResources(R.raw.*) where *
	 * is the id of the file
	 */
	static String FILENAME = "PersistentData.txt";

	BufferedReader reader;

	DataParser(Context c, String file) {
		this.FILENAME = file;
		InputStream stream;

		try {
			stream = c.openFileInput(FILENAME);
			BufferedReader readr = new BufferedReader(new InputStreamReader(
					stream));

			this.reader = readr;
			Log.d("data", "persistent data read in, this is desireable");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		// DEFAULT FILE
		if (this.reader == null) {
			stream = c.getResources().openRawResource(R.raw.ohio);
			BufferedReader readr = new BufferedReader(new InputStreamReader(
					stream));
			this.reader = readr;
			Log.d("data", "static data read in, this is not desireable");

		}
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

	public Data read() {

		ArrayList<MarkerOptions> cache_array = new ArrayList<MarkerOptions>();
		while (this.ready()) {

			String line;
			try {
				line = this.reader.readLine();
				String[] contents = line.split(",");

				String title = contents[2];

				Double lat = Double.parseDouble(contents[0]);
				Double lng = Double.parseDouble(contents[1]);

				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(lat, lng)).title(title);
				cache_array.add(marker);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		MarkerOptions target = new MarkerOptions();

		target.position(new LatLng(39.961138, -83.001465));
		Data data = new Data(cache_array, target, 11);

		return data;
	}

	/** used when you delete a place */
	public static void overwriteAll(Data write, Context c) {

		ArrayList<MarkerOptions> data = write.data;

		FileOutputStream writer;
		try {
			writer = c.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			int size = data.size();

			for (int i = 0; i < size; i++) {
				MarkerOptions temp = data.get(i);
				writeMarker(temp, writer);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void append(MarkerOptions mo, Context c) {

		FileOutputStream writer;
		try {

			writer = c.openFileOutput(FILENAME, Context.MODE_APPEND);
			writeMarker(mo, writer);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void writeMarker(MarkerOptions mo, FileOutputStream stream) {
		String line = String.valueOf(mo.getPosition().latitude) + ","
				+ mo.getPosition().longitude + "," + mo.getTitle() + "\n";
		try {
			stream.write(line.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
