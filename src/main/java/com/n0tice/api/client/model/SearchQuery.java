package com.n0tice.api.client.model;

import java.util.ArrayList;
import java.util.List;

public class SearchQuery {

	private Integer limit = null;
	private List<String> tags = new ArrayList<String>();
	
	public SearchQuery limit(Integer limit) {
		this.limit = limit;
		return this;
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
	
}
