package com.cse.geobook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Data implements Parcelable {
	@SuppressWarnings("hiding")
	ArrayList<MarkerOptions> foundCaches;
	ArrayList<MarkerOptions> allCaches;
	MarkerOptions target;
	int zoom;

	// data[][0] = name
	// data[][1] = latitude
	// data[][2] = longitude
	// data[][3] = rating
	// data[][4] = type
	// data[][5] = container
	// data[][6] = terrain
	// data[][7] = diff
	// data[][8] = description
	
	ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
	
	

	public static String CACHE_DATA;

	public Data(ArrayList<MarkerOptions> fc, ArrayList<MarkerOptions> ac,
			MarkerOptions target, int zoom) {
		this.foundCaches = fc;
		this.allCaches = ac;
		this.target = target;
		this.zoom = zoom;


	}

	public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
		public Data createFromParcel(Parcel in) {
			return new Data(in);
		}

		public Data[] newArray(int size) {
			return new Data[size];
		}
	};

	public Data(Parcel in) {
		// foundCaches
		foundCaches = new ArrayList<MarkerOptions>();
		int size = in.readInt(); // length
		for (int i = 0; i < size; i++) {
			String title = in.readString(); // title
			String description = in.readString(); // snippit
			Double lat = in.readDouble();
			Double lng = in.readDouble();

			foundCaches.add(new MarkerOptions().title(title)
					.snippet(description).position(new LatLng(lat, lng)));
		}

		// allCaches
		allCaches = new ArrayList<MarkerOptions>();
		size = in.readInt(); // length
		for (int i = 0; i < size; i++) {
			String title = in.readString(); // title
			String description = in.readString(); // snippit
			Double lat = in.readDouble();
			Double lng = in.readDouble();

			allCaches.add(new MarkerOptions().title(title).snippet(description)
					.position(new LatLng(lat, lng)));
		}

		// target
		target = new MarkerOptions().title(in.readString())
				.snippet(in.readString())
				.position(new LatLng(in.readDouble(), in.readDouble()));
		zoom = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// foundCaches
		int size = foundCaches.size();
		dest.writeInt(size); // length
		for (int i = 0; i < size; i++) {
			MarkerOptions temp = foundCaches.get(i);
			dest.writeString(temp.getTitle()); // title
			dest.writeString(temp.getSnippet()); // snippit
			dest.writeDouble(temp.getPosition().latitude);
			dest.writeDouble(temp.getPosition().longitude);

		}

		// allCaches
		size = allCaches.size();
		dest.writeInt(size); // length
		for (int i = 0; i < size; i++) {
			MarkerOptions temp = allCaches.get(i);
			dest.writeString(temp.getTitle()); // title
			dest.writeString(temp.getSnippet()); // snippit
			dest.writeDouble(temp.getPosition().latitude);
			dest.writeDouble(temp.getPosition().longitude);

		}

		// Target
		dest.writeString(target.getTitle());
		dest.writeString(target.getSnippet());
		dest.writeDouble(target.getPosition().latitude);
		dest.writeDouble(target.getPosition().longitude);

		dest.writeInt(zoom);
	}
}
