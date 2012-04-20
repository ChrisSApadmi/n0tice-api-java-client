package com.n0tice.api.client.model;

import java.util.ArrayList;
import java.util.List;

public class SearchQuery {

	private Integer page = null;
	private Integer limit = null;
	private List<String> tags = new ArrayList<String>();
	private String type = null;
	
	public SearchQuery limit(Integer limit) {
		this.limit = limit;
		return this;
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
	
	public String getType() {
		return type;
	}
	
}
