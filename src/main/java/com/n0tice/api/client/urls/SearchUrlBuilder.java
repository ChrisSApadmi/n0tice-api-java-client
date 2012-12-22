package com.n0tice.api.client.urls;

import com.google.common.base.Joiner;
import com.n0tice.api.client.model.SearchQuery;

public class SearchUrlBuilder {

	private static final String SEARCH = "/search";
   
	private static Joiner COMMA_JOINER = Joiner.on(",");
   
	final private String apiUrl;
	
	public SearchUrlBuilder(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	public String toUrl(SearchQuery searchQuery) {
		final UrlStringBuilder url = new UrlStringBuilder();
		url.append(apiUrl);
		url.append(SEARCH);
		
		if (searchQuery.getQ() != null) {
			url.appendParameter("q", searchQuery.getQ());		
		}		
		if (searchQuery.getPage() != null) {		
			url.appendParameter("page", Integer.toString(searchQuery.getPage()));
		}
		if (searchQuery.getLimit() != null) {		
			url.appendParameter("limit", Integer.toString(searchQuery.getLimit()));
		}
		if (searchQuery.getType() != null) {
			url.appendParameter("type", searchQuery.getType());		

		}
		if (searchQuery.getNoticeBoard() != null) {
			url.appendParameter("noticeboard", searchQuery.getNoticeBoard());
		}
		if (searchQuery.getUser() != null) {
			url.appendParameter("user", searchQuery.getUser());
		}		
		if (!searchQuery.getTags().isEmpty()) {
			url.appendParameter("tags", COMMA_JOINER.join(searchQuery.getTags()));
		}
		if (searchQuery.getLocation() != null) {
			url.appendParameter("location", searchQuery.getLocation());
		}
		if (searchQuery.getLatitude() != null) {
			url.appendParameter("latitude", Double.toString(searchQuery.getLatitude()));

		}
		if (searchQuery.getLongitude() != null) {
			url.appendParameter("longitude", Double.toString(searchQuery.getLongitude()));
		}
		if (searchQuery.getRadius() != null) {
			url.appendParameter("radius", Double.toString(searchQuery.getRadius()));
		}
		if (searchQuery.getCountry() != null) {
			url.appendParameter("country", searchQuery.getCountry());
		}
		if (searchQuery.getVia() != null) {
			url.appendParameter("via", searchQuery.getVia());
		}
		return url.toString();
	}
	
}
