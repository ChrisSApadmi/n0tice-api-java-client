package com.n0tice.api.client.model;

public class Tag {
	
	final private String id;

	public Tag(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Tag [id=" + id + "]";
	}
	
}
