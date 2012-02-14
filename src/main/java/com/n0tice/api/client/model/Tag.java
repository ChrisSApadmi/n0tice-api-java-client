package com.n0tice.api.client.model;

public class Tag {
	
	final private String id;
	private String slug;
	private String name;

	public Tag(String id) {
		this.id = id;
	}
	
	public Tag(String id, String slug, String name) {
		this.id = id;
		this.slug = slug;
		this.name = name;
	}

	public String getId() {
		return id;
	}
	
	public String getSlug() {
		return slug;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Tag [id=" + id + ", name=" + name + ", slug=" + slug + "]";
	}
	
}
