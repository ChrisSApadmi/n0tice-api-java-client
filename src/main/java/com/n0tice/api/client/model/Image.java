package com.n0tice.api.client.model;

public class Image {
	
	private String small;
	private final String medium;
	private final String large;
	
	public Image(String small, String medium, String large) {
		this.small = small;
		this.medium = medium;
		this.large = large;
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

	@Override
	public String toString() {
		return "Image [large=" + large + ", medium=" + medium + ", small=" + small + "]";
	}
		
}
