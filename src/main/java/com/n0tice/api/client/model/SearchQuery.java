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
	
}
