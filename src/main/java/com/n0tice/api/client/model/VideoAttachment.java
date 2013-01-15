package com.n0tice.api.client.model;

import java.io.InputStream;

public class VideoAttachment {

	private InputStream data;
	private String filename;
	
	public VideoAttachment(InputStream data, String filename) {
		super();
		this.data = data;
		this.filename = filename;
	}

	public InputStream getData() {
		return data;
	}
	
	public String getFilename() {
		return filename;
	}

}
