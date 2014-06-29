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

	BufferedReader reader;
	String filename;

	/**
	 * constructor, creates a new object of type DataParser based on the file,
	 * {@code this} is able to read and write to the {@code file}
	 * 
	 * @param c
	 * @param file
	 */
	DataParser(Context c, String file) {
		InputStream stream;
		this.filename = file;

		try {
			stream = c.openFileInput(file);
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

	/** checks if the stream can be read */
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

	/** Overwrites the entire contents of {@code this.filname} */
	public void overwriteAll(Data write, Context c) {

		ArrayList<MarkerOptions> data = write.data;

		FileOutputStream writer;
		try {
			writer = c.openFileOutput(filename, Context.MODE_PRIVATE);
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

	/**
	 * NOT WORKING YET
	 * 
	 * Appends the passed {@code MarkerOptions mo} to the end of the
	 * {@code stream}
	 * 
	 * @param mo
	 * @param c
	 */
	public void append(MarkerOptions mo, Context c) {

		FileOutputStream writer;
		try {
			writer = c.openFileOutput(filename, Context.MODE_APPEND);
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

	/** Writes to the passed {@code stream} the {@code MarkerOptions mo} */
	private void writeMarker(MarkerOptions mo, FileOutputStream stream) {
		String line = String.valueOf(mo.getPosition().latitude) + ","
				+ mo.getPosition().longitude + "," + mo.getTitle() + "\n";
		try {
			stream.write(line.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
