package com.n0tice.api.client.parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;

public class SearchParser {

	private static final String TAGS = "tags";
	private static final String USER = "user";
	private static final String TYPE = "type";
	private static final String WEB_URL = "webUrl";
	private static final String API_URL = "apiUrl";
	private static final String PLACE = "place";
	private static final String LONGITUDE = "longitude";
	private static final String LATITUDE = "latitude";
	private static final String ID = "id";
	private static final String RESULTS = "results";
	private static final String HEADLINE = "headline";
	private static final String NOTICEBOARD = "noticeboard";

	public List<Content> parseSearchResults(String json) throws ParsingException {
		try {
			JSONObject searchResultsJSON = new JSONObject(json);			
			if (searchResultsJSON.has(RESULTS)) {
				List<Content> contentItems = new ArrayList<Content>();
				
				JSONArray resultContentItems = searchResultsJSON.getJSONArray(RESULTS);
				for (int i = 0; i < resultContentItems.length(); i++) {
					JSONObject contentItem = resultContentItems.getJSONObject(i);					
					contentItems.add(jsonToContentItem(contentItem));
				}
				return contentItems;
			}			
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ParsingException();
		}
		
		return null;
	}

	private Content jsonToContentItem(JSONObject contentItemJSON) throws JSONException {		
		return new Content(contentItemJSON.getString(ID), 
				contentItemJSON.getString(API_URL), 
				contentItemJSON.getString(WEB_URL), 
				contentItemJSON.getString(TYPE), 
				contentItemJSON.getString(HEADLINE), 
				contentItemJSON.getString(PLACE), 
				contentItemJSON.getString(USER), 
				contentItemJSON.getDouble(LATITUDE), 
				contentItemJSON.getDouble(LONGITUDE),
				getNoticeBoardFromJSON(contentItemJSON),
				parseDate(contentItemJSON.getString("created")),
				parseDate(contentItemJSON.getString("modified")),
				parseTags(contentItemJSON)
				);
	}
	
	public Content parseReport(String json) throws ParsingException {
		try {
			JSONObject reportJSON = new JSONObject(json);			
			Content report = new Content(reportJSON.getString("report_id"),	// TODO not consistent with search results format
					null,													// TODO api url not shown when loading from the api url - probably a fair assumption
					reportJSON.getString(WEB_URL),
					reportJSON.getString(TYPE),
					reportJSON.getString(HEADLINE), 
					reportJSON.getString(PLACE),
					reportJSON.getString("user"),
					reportJSON.getDouble("latitude"),
					reportJSON.getDouble("longitude"),
					getNoticeBoardFromJSON(reportJSON),
					parseDate(reportJSON.getString("created")),
					parseDate(reportJSON.getString("modified")),
					parseTags(reportJSON)
					);
			return report;
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ParsingException();
		}
	}
	
	private String getNoticeBoardFromJSON(JSONObject contentItem) throws JSONException {
		if (contentItem.has(NOTICEBOARD)) {
			return contentItem.getString(NOTICEBOARD);
		}
		return null;
	}
	
	private Date parseDate(String dateString) {
		return DateTime.parse(dateString).toDate();
	}
	
	private List<String> parseTags(JSONObject contentItemJSON) throws JSONException {
		if (contentItemJSON.has(TAGS)) {
			List<String> tags = new ArrayList<String>();
			JSONArray jsonTags = contentItemJSON.getJSONArray(TAGS);
			for (int i = 0; i < jsonTags.length(); i++) {
				tags.add(jsonTags.getString(i));				
			}
			return tags;
		}
		return null;
	}
	
}
