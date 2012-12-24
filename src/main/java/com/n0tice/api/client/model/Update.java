package com.n0tice.api.client.model;

public class Update {
	
	private String body;
	private String link;
	private Image image;
	private final User user;
	private final String via;
	
	public Update(User user, String body, String link, Image image, String via) {
		this.user = user;
		this.body = body;
		this.link = link;
		this.image = image;
		this.via = via;
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
	
	public String getVia() {
		return via;
	}

	@Override
	public String toString() {
		return "Update [body=" + body + ", image=" + image + ", link=" + link
				+ ", user=" + user + ", via=" + via + "]";
	}
	
}
