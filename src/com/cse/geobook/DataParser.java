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

/**
 * <pre>
 * Reads in a file where each line is of the format:
 * 
 * 		latitude,longitude,title,description
 * 
 * </pre>
 * 
 * @author Nate
 * 
 */

public class DataParser {

	/*
	 * Stream passed should be getResources().openRawResources(R.raw.*) where *
	 * is the id of the file
	 */
	String FILENAME;
	Context context;
	BufferedReader reader;

	/**
	 * Constructor
	 * 
	 * Creates an instance of {@code DataParser} based on the {@code file} Can
	 * Read and Write to / from {@code file}
	 * 
	 * @param c
	 * @param file
	 */
	DataParser(Context c, String file) {
		this.FILENAME = file;
		this.context = c;
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
		if (this.reader == null && file == Cache.ALL_CACHES) {
			stream = c.getResources().openRawResource(R.raw.ohio);
			BufferedReader readr = new BufferedReader(new InputStreamReader(
					stream));
			this.reader = readr;
			Log.d("data", "static data read in, this is not desireable");

		}
	}

	/**
	 * Writes a single marker to the {@code stream}
	 * 
	 * @param mo
	 * @param stream
	 */
	private void writeMarker(MarkerOptions mo, FileOutputStream stream) {
		String line = String.valueOf(mo.getPosition().latitude) + ","
				+ mo.getPosition().longitude + "," + mo.getTitle() + ","
				+ mo.getSnippet() + "\n";
		try {
			stream.write(line.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Reads a single line and parses the line into a MarkerOptions
	 * 
	 * @return
	 */
	private MarkerOptions readMarker() {
		MarkerOptions marker = null;

		String line;
		try {
			line = this.reader.readLine();
			String[] contents = line.split(",");
			Log.d("RW", Integer.toString(contents.length));

			// if line matches required format
			String matchDouble = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
			if (contents[0].matches(matchDouble)
					&& contents[1].matches(matchDouble)) {

				Double lat = Double.parseDouble(contents[0]);
				Double lng = Double.parseDouble(contents[1]);
				String title = contents[2];
				String description = "";
				for (int i = 3; i < contents.length; i++) {
					description += contents[3];
				}
				marker = new MarkerOptions().position(new LatLng(lat, lng))
						.title(title).snippet(description);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return marker;
	}

	/**
	 * determines whether the stream is ready, and can be read from.
	 * 
	 * @return
	 */
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

	/**
	 * Reads all contents from a text file and returns a representation of the
	 * contents
	 * 
	 * @return
	 */
	public ArrayList<MarkerOptions> read() {

		ArrayList<MarkerOptions> cache_array = new ArrayList<MarkerOptions>();

		if(this.reader != null){
			while (this.ready()) {
				cache_array.add(this.readMarker());
			}

		}
		return cache_array;
	}

	/**
	 * Completely overwritest the file contained in this
	 * 
	 * 
	 */

	public void overwriteAll(ArrayList<MarkerOptions> data) {

		FileOutputStream writer;
		try {
			writer = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			int size = data.size();

			MarkerOptions temp = null;

			for (int i = 0; i < size; i++) {
				temp = data.get(i);
				writeMarker(temp, writer);
			}
			writer.close();

			Log.d("data", "data has been saved");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// NOT WORKING
	public void append(MarkerOptions mo, Context c) {

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

	public void close() {
		try {
			if (this.reader != null)
				this.reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
