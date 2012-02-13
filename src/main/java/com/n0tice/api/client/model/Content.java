package com.n0tice.api.client.model;

import java.util.Date;
import java.util.List;

public class Content {

	final private String id;
	final private String apiUrl;
	final private String webUrl;
	final private String type;
	final private String headline;
	final private Place place;
	final private User user;
	final private String noticeBoard;
	final private Date created;
	final private Date modified;
	final private List<Tag> tags;
	
	public Content(String id, String apiUrl, String webUrl, String type,
			String headline, Place place, User user, String noticeBoard,
			Date created, Date modified, List<Tag> tags) {
		super();
		this.id = id;
		this.apiUrl = apiUrl;
		this.webUrl = webUrl;
		this.type = type;
		this.headline = headline;
		this.place = place;
		this.user = user;
		this.noticeBoard = noticeBoard;
		this.created = created;
		this.modified = modified;
		this.tags = tags;
	}

	public String getId() {
		return id;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public String getType() {
		return type;
	}

	public String getHeadline() {
		return headline;
	}

	public Place getPlace() {
		return place;
	}

	public User getUser() {
		return user;
	}
	
	public String getNoticeBoard() {
		return noticeBoard;
	}
	
	public Date getCreated() {
		return created;
	}

	public Date getModified() {
		return modified;
	}

	public List<Tag> getTags() {
		return tags;
	}
	
	@Override
	public String toString() {
		return "Content [apiUrl=" + apiUrl + ", created=" + created
				+ ", headline=" + headline + ", id=" + id + ", modified="
				+ modified + ", noticeBoard=" + noticeBoard + ", place="
				+ place + ", type=" + type + ", user=" + user + ", webUrl="
				+ webUrl + "]";
	}
	
}
