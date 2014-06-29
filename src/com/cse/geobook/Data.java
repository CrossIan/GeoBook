package com.cse.geobook;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Data implements Parcelable {
	@SuppressWarnings("hiding")
	ArrayList<MarkerOptions> data;
	MarkerOptions target;
	int zoom;

	public static String CACHE_DATA;

	public Data(ArrayList<MarkerOptions> ar, MarkerOptions target, int zoom) {
		this.data = ar;
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
		data = new ArrayList<MarkerOptions>();
		int size = in.readInt(); // length
		for (int i = 0; i < size; i++) {
			String title = in.readString(); // title
			String description = in.readString(); // snippit
			Double lat = in.readDouble();
			Double lng = in.readDouble();

			data.add(new MarkerOptions().title(title).snippet(description)
					.position(new LatLng(lat, lng)));
		}
		target = new MarkerOptions().title(in.readString()).position(
				new LatLng(in.readDouble(), in.readDouble()));
		zoom = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		int size = data.size();
		dest.writeInt(size); // length
		for (int i = 0; i < size; i++) {
			MarkerOptions temp = data.get(i);
			dest.writeString(temp.getTitle()); // title
			dest.writeString(temp.getSnippet()); // snippit
			dest.writeDouble(temp.getPosition().latitude);
			dest.writeDouble(temp.getPosition().longitude);

		}
		dest.writeString(target.getTitle());
		dest.writeDouble(target.getPosition().latitude);
		dest.writeDouble(target.getPosition().longitude);
		dest.writeInt(zoom);
	}
}
