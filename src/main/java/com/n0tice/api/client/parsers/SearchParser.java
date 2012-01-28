package com.n0tice.api.client.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;


public class SearchParser {

	private static final String RESULTS = "results";

	public List<Content> parse(String json) throws ParsingException {
		try {
			JSONObject stopBoardJSON = new JSONObject(json);			
			if (stopBoardJSON.has(RESULTS)) {
				List<Content> contentItems = new ArrayList<Content>();
				
				JSONArray resultContentItems = stopBoardJSON.getJSONArray(RESULTS);
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

	private Content jsonToContentItem(JSONObject contentItem) throws JSONException {
		return new Content(contentItem.getString("id"), 
				contentItem.getString("apiUrl"), 
				contentItem.getString("webUrl"), 
				contentItem.getString("type"), 
				contentItem.getString("headline"), 
				contentItem.getString("place"), 
				contentItem.getString("user"), 
				contentItem.getDouble("latitude"), 
				contentItem.getDouble("longitude"));
	}

}
