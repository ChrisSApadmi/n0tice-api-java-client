package com.n0tice.api.client.model;

import java.util.Date;

public class Content {

	final private String id;
	final private String apiUrl;
	final private String webUrl;
	final private String type;
	final private String headline;
	final private String place;
	final private String user;
	final private double latitude;
	final private double longitude;
	final private String noticeBoard;
	final private Date created;
	final private Date modified;
	
	public Content(String id, String apiUrl, String webUrl, String type,
			String headline, String place, String user, double latitude, double longitude, String noticeBoard,
			Date created, Date modified) {
		super();
		this.id = id;
		this.apiUrl = apiUrl;
		this.webUrl = webUrl;
		this.type = type;
		this.headline = headline;
		this.place = place;
		this.user = user;
		this.latitude = latitude;
		this.longitude = longitude;
		this.noticeBoard = noticeBoard;
		this.created = created;
		this.modified = modified;
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

	public String getPlace() {
		return place;
	}

	public String getUser() {
		return user;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
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

	@Override
	public String toString() {
		return "Content [apiUrl=" + apiUrl + ", created=" + created
				+ ", headline=" + headline + ", id=" + id + ", latitude="
				+ latitude + ", longitude=" + longitude + ", modified="
				+ modified + ", noticeBoard=" + noticeBoard + ", place="
				+ place + ", type=" + type + ", user=" + user + ", webUrl="
				+ webUrl + "]";
	}
	
}
