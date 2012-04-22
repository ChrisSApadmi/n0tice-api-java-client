package com.n0tice.api.client.model;

public class Update {
	
	private String body;
	private String link;
	private Image image;
	
	public Update(String body, String link, Image image) {
		this.body = body;
		this.link = link;
		this.image = image;
	}

	public String getBody() {
		return body;
	}

	public String getLink() {
		return link;
	}

	public Image getImage() {
		return image;
	}

	@Override
	public String toString() {
		return "Update [body=" + body + ", link=" + link + ", image=" + image + "]";
	}
	
}
