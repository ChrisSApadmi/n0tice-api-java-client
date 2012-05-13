package com.n0tice.api.client.model;

public class Place {

	private final String name;
	private final double latitude;
	private final double longitude;
	private final String timezone;
	
	public Place(String name, double latitude, double longitude, String timezone) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timezone = timezone;
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
	
	public String getTimezone() {
		return timezone;
	}

	@Override
	public String toString() {
		return "Place [name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + ", timezone=" + timezone + "]";
	}
	
}
