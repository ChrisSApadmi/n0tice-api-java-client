package com.n0tice.api.client.model;

public class Update {
	
	private String body;
	private String link;
	private Image image;
	private final User user;
	
	public Update(User user, String body, String link, Image image) {
		this.user = user;
		this.body = body;
		this.link = link;
		this.image = image;
	}
	
	public User getUser() {
		return user;
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
		return "Update [body=" + body + ", image=" + image + ", link=" + link + ", user=" + user + "]";
	}
}
