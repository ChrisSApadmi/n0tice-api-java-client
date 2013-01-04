package com.n0tice.api.client.model;

public class Video {
	
	private final String original;
	
	public Video(String original) {
		this.original = original;
	}
	
	public String getOriginal() {
		return original;
	}

	@Override
	public String toString() {
		return "Video [original=" + original + "]";
	}
	
}
