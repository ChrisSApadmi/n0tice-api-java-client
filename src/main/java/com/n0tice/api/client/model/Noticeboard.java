package com.n0tice.api.client.model;

public class Noticeboard {
	
	private final String domain;
	private final String name;
	private final String description;

	public Noticeboard(String domain, String name, String description) {
		this.domain = domain;
		this.name = name;
		this.description = description;
	}

	public String getDomain() {
		return domain;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "Noticeboard [domain=" + domain + ", name=" + name + ", description=" + description + "]";
	}
	
}
