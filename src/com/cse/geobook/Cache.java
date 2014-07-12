package com.cse.geobook;

import java.util.ArrayList;

public class Cache {
	public static final int numberOfdescriptors = 8;
	ArrayList<String> cache;

	public static final String FOUND_CACHES = "foundCaches.txt";
	public static final String ALL_CACHES = "PersistentData.txt";
	public static final String TARGET_CACHE = "Target.txt";

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

	private enum DESCRIPTOR {
		NAME(0), LAT(1), LNG(2), CREATOR(3), DIFFICULTY(4), TERRAIN(5), RATING(
				6), CONTAINER(7), DATE(8), DESCRIPTION(9);
		int INDEX;

		DESCRIPTOR(int i) {
			INDEX = i;
		}

	};

	Cache(ArrayList<String> cache) {
		this.cache = cache;
	}

	Cache() {
		this.cache = new ArrayList<String>();
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

	public String getDifficulty() {
		return cache.get(DESCRIPTOR.DIFFICULTY.INDEX);
	}

	public String getTerrain() {
		return cache.get(DESCRIPTOR.TERRAIN.INDEX);
	}

	public String getContainer() {
		return cache.get(DESCRIPTOR.CONTAINER.INDEX);
	}

	public String getDate() {
		return cache.get(DESCRIPTOR.DATE.INDEX);
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
		int index = DESCRIPTOR.LAT.INDEX;
		cache.set(index, lng);
	}

	public void creator(String creator) {
		int index = DESCRIPTOR.CREATOR.INDEX;
		cache.set(index, creator);
	}

	public void difficulty(String difficulty) {
		int index = DESCRIPTOR.DIFFICULTY.INDEX;
		cache.set(index, difficulty);
	}

	public void terrain(String terrain) {
		int index = DESCRIPTOR.TERRAIN.INDEX;
		cache.set(index, terrain);
	}

	public void container(String container) {
		int index = DESCRIPTOR.CONTAINER.INDEX;
		cache.set(index, container);
	}

	public void date(String date) {
		int index = DESCRIPTOR.DATE.INDEX;
		cache.set(index, date);
	}

	public void description(String description) {
		int index = DESCRIPTOR.DESCRIPTION.INDEX;
		cache.set(index, description);
	}

	public String get(int i) {
		return cache.get(i);

	}

	public int size() {
		return this.cache.size();
	}
}
