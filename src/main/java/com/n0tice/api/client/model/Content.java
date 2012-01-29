package com.n0tice.api.client.model;

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
	
	public Content(String id, String apiUrl, String webUrl, String type,
			String headline, String place, String user, double latitude, double longitude, String noticeBoard) {
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

	@Override
	public String toString() {
		return "Content [id=" + id + ", apiUrl=" + apiUrl + ", webUrl="
				+ webUrl + ", type=" + type + ", headline=" + headline
				+ ", place=" + place + ", user=" + user + ", latitude="
				+ latitude + ", longitude=" + longitude + ", noticeBoard="
				+ noticeBoard + "]";
	}
	
}
