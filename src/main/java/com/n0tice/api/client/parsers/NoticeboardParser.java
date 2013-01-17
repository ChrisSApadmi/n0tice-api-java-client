package com.n0tice.api.client.parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

public class NoticeboardParser
{

	private static final String MODERATED = "moderated";
	private static final String CONTRIBUTORS = "contributors";
	private static final String CONTRIBUTIONS = "contributions";
	private static final String SUPPORTED_MEDIA_TYPES = "supportedMediaTypes";
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
	private static final String FEATURED = "featured";

	public List<Noticeboard> parseNoticeboards(String json) throws ParsingException
	{
		List<Noticeboard> noticeboards = new ArrayList<Noticeboard>();
		try
		{
			final JSONArray noticeboardsJSON = new JSONArray(json);
			for (int i = 0; i < noticeboardsJSON.length(); i++)
			{
				noticeboards.add(parseNoticeboardResult(noticeboardsJSON.getJSONObject(i)));
			}
			return noticeboards;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			throw new ParsingException();
		}
	}

	public Noticeboard parseNoticeboardResult(String json) throws ParsingException
	{
		try
		{
			return parseNoticeboardResult(new JSONObject(json));
		}
		catch (ParsingException e)
		{
			throw new ParsingException(e.getMessage());
		}
		catch (JSONException e)
		{
			throw new ParsingException(e.getMessage());
		}
	}

	public Noticeboard parseNoticeboardResult(JSONObject noticeboardJsonObject) throws ParsingException
	{
		try
		{
			final Image background = noticeboardJsonObject.has(BACKGROUND) ? parseImage(noticeboardJsonObject.getJSONObject(BACKGROUND)) : null;
			final Image cover = noticeboardJsonObject.has(COVER) ? parseImage(noticeboardJsonObject.getJSONObject(COVER)) : null;
			final String description = noticeboardJsonObject.has("description") ? noticeboardJsonObject.getString("description") : null;
			Date endDate = null;
			if (noticeboardJsonObject.has(END_DATE))
			{
				endDate = ISODateTimeFormat.dateTimeNoMillis().parseDateTime(noticeboardJsonObject.getString(END_DATE)).toDate();
			}

			Group group = null;
			if (noticeboardJsonObject.has(GROUP))
			{
				final JSONObject groupJson = noticeboardJsonObject.getJSONObject(GROUP);
				group = new Group(groupJson.getString(ID), groupJson.getString(NAME));
			}

			final Set<MediaType> supportedMediaTypes = new HashSet<MediaType>();
			if (noticeboardJsonObject.has(SUPPORTED_MEDIA_TYPES))
			{
				JSONArray mediaTypesJson = noticeboardJsonObject.getJSONArray(SUPPORTED_MEDIA_TYPES);
				for (int i = 0; i < mediaTypesJson.length(); i++)
				{
					supportedMediaTypes.add(MediaType.valueOf(mediaTypesJson.getString(i)));
				}
			}

			int contributors = 0;
			if (noticeboardJsonObject.has(CONTRIBUTORS))
			{
				contributors = noticeboardJsonObject.getInt(CONTRIBUTORS);
			}

			boolean featured = false;
			if (noticeboardJsonObject.has(FEATURED))
			{
				featured = noticeboardJsonObject.getBoolean(FEATURED);
			}

			final boolean moderated = noticeboardJsonObject.getBoolean(MODERATED);

			return new Noticeboard(noticeboardJsonObject.getString(DOMAIN), noticeboardJsonObject.getString(NAME), description, background, cover, endDate, group, supportedMediaTypes, contributors, noticeboardJsonObject.optInt(CONTRIBUTIONS), moderated,
					featured);

		}
		catch (JSONException e)
		{
			throw new ParsingException(e.getMessage());
		}
	}

	// TODO duplication
	private Image parseImage(JSONObject imageJson) throws JSONException
	{
		return new Image(imageJson.getString(SMALL), imageJson.getString(MEDIUM), imageJson.getString(LARGE));
	}

}
