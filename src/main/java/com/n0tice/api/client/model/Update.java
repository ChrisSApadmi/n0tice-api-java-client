package com.n0tice.api.client.model;

import org.joda.time.DateTime;

public class Update {
		
	private String body;
	private String link;
	private Image image;
	private Video video;
	private final User user;
	private final DateTime created;
	private final DateTime modified;
	private final String via;
	
	public Update(User user, String body, String link, Image image, Video video, DateTime created, DateTime modified, String via) {
		this.user = user;
		this.body = body;
		this.link = link;
		this.image = image;
		this.video = video;
		this.created = created;
		this.modified = modified;
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
	
	public Video getVideo() {
		return video;
	}

	public DateTime getCreated() {
		return created;
	}

	public DateTime getModified() {
		return modified;
	}

	public String getVia() {
		return via;
	}

	@Override
	public String toString() {
		return "Update [body=" + body + ", link=" + link + ", image=" + image
				+ ", video=" + video + ", user=" + user + ", created="
				+ created + ", modified=" + modified + ", via=" + via + "]";
	}
	
}
