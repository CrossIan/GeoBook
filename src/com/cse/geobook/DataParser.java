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
	private static final String TAG = "DataParser.java";
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
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		// DEFAULT FILE
		if (this.reader == null && file == Cache.ALL_CACHES) {
			stream = c.getResources().openRawResource(R.raw.ohio);
			BufferedReader readr = new BufferedReader(new InputStreamReader(
					stream));
			this.reader = readr;
		}
	}

	/**
	 * Writes a single marker to the {@code stream}
	 * 
	 * @param mo
	 * @param stream
	 */
	private void writeMarker(Cache cache, FileOutputStream stream) {
		String line = "";
		for (int i = 0; i < Cache.numberOfdescriptors - 1; i++) {
			line += cache.get(i) + ",";
		}
		line += cache.get(Cache.numberOfdescriptors - 1);
		line += "\n";
		try {
			stream.write(line.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads a single line and parses the line into a MarkerOptions
	 * 
	 * @return
	 */
	private Cache readMarker() {
		Cache result = new Cache();

		String line;
		try {
			line = this.reader.readLine();
			String[] contents = line.split("[,]");

			// Log.d("Length",
			// "Length of array: " + Integer.toString(contents.length));

			// if line matches required format
			String matchDouble = "[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?";
			if (contents[1].matches(matchDouble)
					&& contents[2].matches(matchDouble)) {

				/*
				 * for (int i = 0; i < Data.numberOfdescriptors - 2; i++) {
				 * result.add(contents[i]); }
				 */
				int size = contents.length;
				for (int i = 0; i < Cache.numberOfdescriptors - 1; i++) {
					// Log.d("values", "Contents " + i + ":" + contents[i]);
					if (i < size && contents[i] != null) {
						result.set(i, contents[i]);
					} else {
						result.set(i, "");
					}
				}

				String description = "";
				for (int i = Cache.numberOfdescriptors - 1; i < size; i++) {
					description += contents[i];
				}
				result.set(Cache.numberOfdescriptors - 1, description);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
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
	public ArrayList<Cache> read() {

		ArrayList<Cache> cache_array = new ArrayList<Cache>();

		if (this.reader != null) {
			// int counter = 0;
			while (this.ready()) {
				cache_array.add(this.readMarker());
				// counter++;
				// Log.d(TAG,Integer.toString(counter));
			}
		}
		Log.d("data", "reading");
		return cache_array;
	}

	/**
	 * Completely overwrites the file contained in this
	 * 
	 * 
	 */

	public void overwriteAll(ArrayList<Cache> data) {

		FileOutputStream writer;
		try {
			writer = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			int size = data.size();

			Cache cache = new Cache();

			for (int i = 0; i < size; i++) {
				cache = data.get(i);
				writeMarker(cache, writer);
			}
			writer.close();

			Log.d("data", "data has been saved");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (this.reader != null)
				this.reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
