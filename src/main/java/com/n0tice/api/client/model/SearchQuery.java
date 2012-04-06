package com.n0tice.api.client.model;

public class SearchQuery {

	private Integer limit = null;
	
	public Integer getLimit() {
		return limit;
	}

	public SearchQuery limit(Integer limit) {
		this.limit = limit;
		return this;
	}
	
}
