package com.n0tice.api.client.model;

public class ImageFile {

	private byte[] data;
	private String filename;
	
	public ImageFile(byte[] data, String filename) {
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
