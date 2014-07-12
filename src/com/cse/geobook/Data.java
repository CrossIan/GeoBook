package com.cse.geobook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {
	@SuppressWarnings("hiding")
	ArrayList<Cache> foundCaches;
	ArrayList<Cache> allCaches;
	Cache target;
	int zoom;

	/* Indices for the below values */
	int primarySort;
	int secondarySort;
	// can remove all caches

	// data[][0] = name
	// data[][1] = latitude
	// data[][2] = longitude
	// data[][3] = creator
	// data[][4] = diff
	// data[][5] = terrain
	// data[][6] = awesomeness
	// data[][7] = container
	// data[][8] = date found
	// data[][9] = description
	public static final int numberOfdescriptors = 10;

	public static String CACHE_DATA;

	// edit SortBy to match indicies
	public enum SortBy {
		NAME(0), RATING(3), TYPE(4), CONTAINER(5), DIFFICULTY(7), DATE(8);
		int SORTING;

		SortBy(int i) {
			SORTING = i;
		}
	};

	public Data(ArrayList<Cache> found, ArrayList<Cache> all, Cache target,
			int zoom) {
		this.foundCaches = found;
		this.allCaches = all;
		this.target = target;
		this.zoom = zoom;

		primarySort = 0; // name
		secondarySort = 0;
	}

	public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
		public Data createFromParcel(Parcel in) {
			return new Data(in);
		}

		public Data[] newArray(int size) {
			return new Data[size];
		}
	};

	/**
	 * Reads a single cache from the parcel, a cache is equivalent to a single
	 * line from the data parcer
	 * 
	 * @param in
	 * @return
	 */
	private Cache getNextCache(Parcel in) {
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < numberOfdescriptors; i++) {
			String descriptor = in.readString(); // snippit
			temp.add(descriptor);
		}
		return new Cache(temp);
	}

	public Data(Parcel in) {
		// foundCaches
		foundCaches = new ArrayList<Cache>();
		int size = in.readInt(); // length
		for (int i = 0; i < size; i++) {
			Cache cache = getNextCache(in);
			foundCaches.add(cache);
		}

		// allCaches
		allCaches = new ArrayList<Cache>();
		size = in.readInt(); // length
		for (int i = 0; i < size; i++) {
			Cache cache = getNextCache(in);
			foundCaches.add(cache);
		}
		// target
		target = getNextCache(in);
		zoom = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Writes a singe cache to the parcel, a cache is equivalent to a comma
	 * separated line in the dataparser
	 * 
	 * @param dest
	 * @param cache
	 */
	private void writeNextCache(Parcel dest, Cache cache) {
		for (int i = 0; i < numberOfdescriptors; i++) {
			String descriptor = cache.get(i);
			dest.writeString(descriptor);
		}
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// foundCaches
		int size = foundCaches.size();
		dest.writeInt(size); // length

		for (int i = 0; i < size; i++) {
			Cache cache = foundCaches.get(i);
			writeNextCache(dest, cache);
		}

		// allCaches
		size = allCaches.size();
		dest.writeInt(size); // length
		for (int i = 0; i < size; i++) {
			Cache cache = allCaches.get(i);
			writeNextCache(dest, cache);
		}

		// Target
		writeNextCache(dest, target);

		// zoom
		dest.writeInt(zoom);
	}

	Comparator<Cache> comparator = new Comparator<Cache>() {

		@Override
		public int compare(Cache lhs, Cache rhs) {
			int result = lhs.get(primarySort).compareTo(rhs.get(primarySort));
			if (result == 0) {
				result = lhs.get(secondarySort).compareTo(
						rhs.get(secondarySort));
			}
			return result;
		}
	};

	public void sort(SortBy primary, SortBy secondary) {
		this.primarySort = primary.SORTING;
		this.secondarySort = secondary.SORTING;
		Collections.sort(allCaches, comparator);
		Collections.sort(foundCaches, comparator);
	}

	public void sort(SortBy ordering) {
		this.primarySort = ordering.SORTING;
		this.secondarySort = ordering.SORTING;
		Collections.sort(allCaches, comparator);
		Collections.sort(foundCaches, comparator);
	}

}
