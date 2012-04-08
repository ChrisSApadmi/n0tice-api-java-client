package com.n0tice.api.client.model;

public class Place {

	private final String name;
	final private double latitude;
	final private double longitude;
	
	public Place(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getName() {
		return name;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "Place [latitude=" + latitude + ", longitude=" + longitude + ", name=" + name + "]";
	}
	
}
