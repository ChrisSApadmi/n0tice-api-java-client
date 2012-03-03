package com.n0tice.api.client.model;

public class Image {
	
	private String small;
	
	public Image(String small) {
		this.small = small;
	}
	
	public String getSmall() {
		return small;
	}

	@Override
	public String toString() {
		return "Image [small=" + small + "]";
	}
	
}
