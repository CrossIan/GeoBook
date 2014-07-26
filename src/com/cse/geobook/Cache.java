package com.cse.geobook;

import java.util.ArrayList;

import com.google.android.gms.maps.model.Marker;

public class Cache {
	ArrayList<String> cache;
	int size;

	public static final String FOUND_CACHES = "foundCaches.txt";
	public static final String ALL_CACHES = "PersistentData.txt";
	public static final String TARGET_CACHE = "Target.txt";

	// data[][0] = name
	// data[][2] = latitude
	// data[][1] = longitude
	// data[][3] = creator
	// data[][4] = photo
	// data[][5] = diff
	// data[][6] = terrain
	// data[][7] = awesomeness
	// data[][8] = container
	// data[][9] = description

	public static final int numberOfdescriptors = 10;

	public enum DESCRIPTOR {
		NAME(0), LAT(2), LNG(1), CREATOR(3), PHOTO(4), DIFFICULTY(5), TERRAIN(6), RATING(
				7), CONTAINER(8), DESCRIPTION(9);
		int INDEX;

		DESCRIPTOR(int i) {
			INDEX = i;
		}

	};

	Cache() {
		this.cache = new ArrayList<String>();
		size = 0;
		for (int i = 0; i < Cache.numberOfdescriptors; i++) {
			cache.add("");
			size++;
		}
	}

	public String getName() {
		return cache.get(DESCRIPTOR.NAME.INDEX);
	}

	public Double getLat() {
		return Double.parseDouble(cache.get(DESCRIPTOR.LAT.INDEX));
	}

	public Double getLng() {
		return Double.parseDouble(cache.get(DESCRIPTOR.LNG.INDEX));
	}

	public String getCreator() {
		return cache.get(DESCRIPTOR.CREATOR.INDEX);
	}

	public String getPhoto() {
		return cache.get(DESCRIPTOR.PHOTO.INDEX);
	}

	public double getDifficulty() {
		return Double.parseDouble(cache.get(DESCRIPTOR.DIFFICULTY.INDEX));
	}

	public double getTerrain() {
		return Double.parseDouble(cache.get(DESCRIPTOR.TERRAIN.INDEX));
	}

	public double getRating() {
		return Double.parseDouble(cache.get(DESCRIPTOR.RATING.INDEX));
	}

	public double getContainer() {
		return Double.parseDouble(cache.get(DESCRIPTOR.CONTAINER.INDEX));
	}

	public String getDescription() {
		return cache.get(DESCRIPTOR.DESCRIPTION.INDEX);
	}

	public void name(String name) {
		int index = DESCRIPTOR.NAME.INDEX;
		cache.set(index, name);
	}

	public void lat(String lat) {
		int index = DESCRIPTOR.LAT.INDEX;
		cache.set(index, lat);
	}

	public void lng(String lng) {
		int index = DESCRIPTOR.LNG.INDEX;
		cache.set(index, lng);
	}

	public void creator(String creator) {
		int index = DESCRIPTOR.CREATOR.INDEX;
		cache.set(index, creator);
	}

	public void photo(String photoName) {
		int index = DESCRIPTOR.PHOTO.INDEX;
		cache.set(index, photoName);
	}

	public void difficulty(String difficulty) {
		int index = DESCRIPTOR.DIFFICULTY.INDEX;
		cache.set(index, difficulty);
	}

	public void terrain(String terrain) {
		int index = DESCRIPTOR.TERRAIN.INDEX;
		cache.set(index, terrain);
	}

	public void rating(String rating) {
		int index = DESCRIPTOR.RATING.INDEX;
		cache.set(index, rating);
	}

	public void container(String container) {
		int index = DESCRIPTOR.CONTAINER.INDEX;
		cache.set(index, container);
	}

	public void description(String description) {
		int index = DESCRIPTOR.DESCRIPTION.INDEX;
		cache.set(index, description);
	}

	public String get(int i) {
		return cache.get(i);

	}

	public void set(int index, String value) {
		cache.set(index, value);
	}

	public int size() {
		return size;
	}

	public String toString() {
		String result = "";

		if (this.cache != null) {
			result = cache.get(0);
		}
		return result;

	}

	@Override
	public boolean equals(Object obj) {
		return this.getName().equals(obj.toString());
	}

	public boolean equals(Marker m) {
		return this.getName().equals(m.getTitle());

	}

	public boolean is(Cache c) {
		return this.getName().equals(c.getName())
				&& this.getPhoto().equals(c.getPhoto())
				&& this.getDescription().equals(c.getDescription())
				&& this.getRating() == c.getRating();

	}

}
