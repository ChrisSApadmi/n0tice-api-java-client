package com.n0tice.api.client.model;

public class MediaFile {

	private byte[] data;
	private String filename;
	
	public MediaFile(byte[] data, String filename) {
		super();
		this.data = data;
		this.filename = filename;
	}

	public byte[] getData() {
		return data;
	}
	
	public String getFilename() {
		return filename;
	}

}
