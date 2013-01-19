package com.n0tice.api.client.model;

public class Image {
	
	private final String small;
	private final String medium;
	private final String large;
	private final String orientation;
	
	public Image(String small, String medium, String large, String orientation) {
		this.small = small;
		this.medium = medium;
		this.large = large;
		this.orientation = orientation;
	}

	public String getSmall() {
		return small;
	}

	public String getMedium() {
		return medium;
	}

	public String getLarge() {
		return large;
	}
	
	public String getOrientation() {
		return orientation;
	}

	@Override
	public String toString() {
		return "Image [large=" + large + ", medium=" + medium
				+ ", orientation=" + orientation + ", small=" + small + "]";
	}
		
}
