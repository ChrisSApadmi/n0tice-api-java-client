package com.n0tice.api.client.parsers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Group;
import com.n0tice.api.client.model.Image;
import com.n0tice.api.client.model.MediaType;
import com.n0tice.api.client.model.Noticeboard;

public class NoticeboardParser {

	private static final String BACKGROUND = "background";
	private static final String COVER = "cover";
	private static final String GROUP = "group";
	private static final String DOMAIN = "domain";
	private static final String END_DATE = "endDate";
	private static final String NAME = "name";
	private static final String ID = "id";
	private static final String SMALL = "small";
	private static final String MEDIUM = "medium";
	private static final String LARGE = "large";
	
	public Noticeboard parseNoticeboardResult(String json) throws ParsingException {
		try {
			final JSONObject jsonObject = new JSONObject(json);
			final Image background = jsonObject.has(BACKGROUND) ? parseImage(jsonObject.getJSONObject(BACKGROUND)) : null;
			final Image cover =  jsonObject.has(COVER) ? parseImage(jsonObject.getJSONObject(COVER)) : null;
			final String description = jsonObject.has("description") ? jsonObject.getString("description") : null;
			Date endDate = null;
			if (jsonObject.has(END_DATE)) {
				endDate = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(jsonObject.getString(END_DATE)).toDate();
			}
			
			Group group = null;
			if (jsonObject.has(GROUP)) {
				final JSONObject groupJson = jsonObject.getJSONObject(GROUP);
				group = new Group(groupJson.getString(ID), groupJson.getString(NAME));
			}
			Set<MediaType> supportedMediaTypes = new HashSet<MediaType>();
			if (jsonObject.has("supportedMediaTypes")) {
				JSONArray mediaTypesJson = jsonObject.getJSONArray("supportedMediaTypes");
				for (int i = 0; i < mediaTypesJson.length(); i++) {
					String mediaType = (String) mediaTypesJson.get(i);
					supportedMediaTypes.add(MediaType.valueOf(mediaType));					
				}
			}
			return new Noticeboard(jsonObject.getString(DOMAIN), jsonObject.getString(NAME), description, background, cover, endDate, group, supportedMediaTypes);
			
		} catch (JSONException e) {
			throw new ParsingException();
		}
	}
	
	// TODO duplication
	private Image parseImage(JSONObject imageJson) throws JSONException {
		return new Image(imageJson.getString(SMALL), imageJson.getString(MEDIUM), imageJson.getString(LARGE));
	}
	
}
