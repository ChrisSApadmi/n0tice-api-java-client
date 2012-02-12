package com.n0tice.api.client.model;

public class Place {

	final private double latitude;
	final private double longitude;
	
	public Place(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	@Override
	public String toString() {
		return "Place [latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
}
