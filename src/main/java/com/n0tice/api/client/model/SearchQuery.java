package com.n0tice.api.client.model;

import java.util.ArrayList;
import java.util.List;

public class SearchQuery {

	private String q = null;
	private Integer page = null;
	private Integer limit = null;
	private List<String> tags = new ArrayList<String>();
	private String type = null;
	private String noticeBoard = null;
	private String location = null;
	private String user = null;
	private Double latitude;
	private Double longitude;
	private Double radius;
	private String country;
	private String via;
	
	public SearchQuery q(String q) {
		this.q = q;
		return this;
	}
	
	public SearchQuery limit(Integer limit) {
		this.limit = limit;
		return this;
	}
	
	public String getQ() {
		return q;
	}

	public Integer getPage() {
		return page;
	}
	
	public Integer getLimit() {
		return limit;
	}
	
	public SearchQuery tag(String tag) {
		tags.add(tag);
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public SearchQuery page(int page) {
		this.page = page;
		return this;
	}

	public SearchQuery type(String type) {
		this.type = type;
		return this;
	}
	
	public SearchQuery user(String user) {
		this.user = user;
		return this;
	}
	
	public SearchQuery latitude(double latitude) {
		this.latitude = latitude;
		return this;
	}
	
	public SearchQuery longitude(double longitude) {
		this.longitude = longitude;
		return this;
	}
	
	public SearchQuery radius(double radius) {
		this.radius = radius;
		return this;
	}
	
	public SearchQuery country(String country) {
		this.country = country;
		return this;
	}
	
	public SearchQuery via(String via) {
		this.via = via;
		return this;
	}
	
	public String getType() {
		return type;
	}
	
	public SearchQuery noticeBoard(String noticeBoard) {
		this.noticeBoard = noticeBoard;
		return this;
	}
	
	public String getNoticeBoard() {
		return noticeBoard;
	}
	
	public SearchQuery location(String location) {
		this.location = location;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public String getUser() {
		return user;
	}

	public Double getLatitude() {
		return latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}

	public Double getRadius() {
		return radius;
	}
	
	public String getCountry() {
		return country;
	}

	public String getVia() {
		return via;
	}

	@Override
	public String toString() {
		return "SearchQuery [q=" + q + ", page=" + page + ", limit=" + limit
				+ ", tags=" + tags + ", type=" + type + ", noticeBoard="
				+ noticeBoard + ", location=" + location + ", user=" + user
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", radius=" + radius + ", country=" + country + ", via="
				+ via + "]";
	}
	
}
