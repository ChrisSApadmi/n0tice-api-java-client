package com.n0tice.api.client.parsers;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.Content;
import com.n0tice.api.client.model.Group;
import com.n0tice.api.client.model.Image;
import com.n0tice.api.client.model.Place;
import com.n0tice.api.client.model.Reoccurence;
import com.n0tice.api.client.model.ResultSet;
import com.n0tice.api.client.model.Tag;
import com.n0tice.api.client.model.Update;
import com.n0tice.api.client.model.User;
import com.n0tice.api.client.model.Video;

// TODO migrate to Jackson once we can prove that it is Android safe
public class SearchParser
{

	private static final String AWAITING_MODERATION = "awaitingModeration";
	private static final String END_DATE = "endDate";
	private static final String REOCCURS_TO = "reoccursTo";
	private static final String REOCCURS = "reoccurs";
	private static final String MESSAGE = "message";
	private static final String TIME_ZONE = "timeZone";
	private static final String NAME = "name";
	private static final String TAGS = "tags";
	private static final String UPDATES = "updates";
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
	private static final String START_DATE = "startDate";
	private static final String INTERESTING = "interesting";
	private static final String VOTES = "votes";
	private static final String REPOSTS = "reposts";
	private static final String SMALL = "small";
	private static final String MEDIUM = "medium";
	private static final String LARGE = "large";
	private static final String COUNTRY = "country";
	private static final String VIA = "via";
	private static final String ORIGINAL = "original";
	private static final String YOUTUBE_ID = "youTubeId";
	private static final String VIDEO_IMG = "image";
	private static final String MEDIUM_LANDSCAPE = "mediumlandscape";

	private static DateTimeFormatter dateFormatter = ISODateTimeFormat.dateTimeNoMillis().withOffsetParsed();

	public ResultSet parseSearchResults(String json) throws ParsingException
	{
		try
		{
			JSONObject searchResultsJSON = new JSONObject(json);
			final int totalMatches = searchResultsJSON.getInt("numberFound");
			final int startIndex = searchResultsJSON.getInt("startIndex");
			if (searchResultsJSON.has(RESULTS))
			{
				List<Content> contentItems = new ArrayList<Content>();

				JSONArray resultContentItems = searchResultsJSON.getJSONArray(RESULTS);
				for (int i = 0; i < resultContentItems.length(); i++)
				{
					JSONObject contentItem = resultContentItems.getJSONObject(i);
					contentItems.add(jsonToContentItem(contentItem));
				}
				return new ResultSet(totalMatches, startIndex, contentItems);
			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
			throw new ParsingException();
		}

		return null;
	}

	private Content jsonToContentItem(JSONObject contentItemJSON) throws JSONException
	{
		User user = null;
		if (contentItemJSON.has(USER))
		{
			JSONObject userJSON = contentItemJSON.getJSONObject(USER);
			user = new UserParser().jsonToUser(userJSON);	// TODO Make into a
															// field to avoid
															// excessive GCing
		}

		Place place = null;
		if (contentItemJSON.has(PLACE))
		{
			JSONObject placeJson = contentItemJSON.getJSONObject(PLACE);
			final String timezone = placeJson.has(TIME_ZONE) ? placeJson.getString(TIME_ZONE) : null;
			final String country = placeJson.has(COUNTRY) ? placeJson.getString(COUNTRY) : null;
			place = new Place(placeJson.getString(NAME), placeJson.getDouble(LATITUDE), placeJson.getDouble(LONGITUDE), timezone, country);
		}

		DateTime startDate = null;
		if (contentItemJSON.has(START_DATE))
		{
			startDate = parseDate(contentItemJSON.getString(START_DATE));
		}
		DateTime endDate = null;
		if (contentItemJSON.has(END_DATE))
		{
			endDate = parseDate(contentItemJSON.getString(END_DATE));
		}
		Reoccurence reoccurs = null;
		if (contentItemJSON.has(REOCCURS))
		{
			reoccurs = Reoccurence.valueOf(contentItemJSON.getString(REOCCURS));
		}
		DateTime reoccursTo = null;
		if (contentItemJSON.has(REOCCURS_TO))
		{
			reoccursTo = parseDate(contentItemJSON.getString(REOCCURS_TO));
		}

		int interestingVotes = 0;
		if (contentItemJSON.has(VOTES))
		{
			JSONObject votesJson = contentItemJSON.getJSONObject(VOTES);
			if (votesJson.has(INTERESTING))
			{
				interestingVotes = votesJson.getInt(INTERESTING);
			}
		}

		int reposts = 0;
		if (contentItemJSON.has(REPOSTS))
		{
			reposts = contentItemJSON.getInt(REPOSTS);
		}

		final Boolean awaitingModeration = contentItemJSON.has(AWAITING_MODERATION) ? contentItemJSON.getBoolean(AWAITING_MODERATION) : null;

		return new Content(contentItemJSON.getString(ID), contentItemJSON.getString(API_URL), contentItemJSON.getString(WEB_URL), contentItemJSON.getString(TYPE), contentItemJSON.getString(HEADLINE), place, user, getNoticeboardNameFromJSON(contentItemJSON),
				parseDate(contentItemJSON.getString("created")), parseDate(contentItemJSON.getString("modified")), parseTags(contentItemJSON), parseUpdates(contentItemJSON), startDate, endDate, reoccurs, reoccursTo, interestingVotes, reposts,
				awaitingModeration);
	}

	public Content parseReport(String json) throws ParsingException
	{
		try
		{
			JSONObject reportJSON = new JSONObject(json);
			return jsonToContentItem(reportJSON);

		}
		catch (JSONException e)
		{
			throw new ParsingException(e.getMessage());
		}
	}

	public Update parseUpdate(String json) throws ParsingException
	{
		try
		{
			return parseJsonUpdate(new JSONObject(json));
		}
		catch (JSONException e)
		{
			throw new ParsingException(e.getMessage());
		}
	}

	public Group parseGroupResult(String json) throws ParsingException
	{
		try
		{
			final JSONObject jsonObject = new JSONObject(json);
			return new Group(jsonObject.getString(ID), jsonObject.getString(NAME));
		}
		catch (JSONException e)
		{
			throw new ParsingException();
		}
	}

	public List<String> parseNotifications(String json) throws ParsingException
	{
		ArrayList<String> notifications = new ArrayList<String>();
		try
		{
			JSONArray jsonNotifications = new JSONArray(json);
			for (int i = 0; i < jsonNotifications.length(); i++)
			{
				JSONObject notification = jsonNotifications.getJSONObject(i);
				notifications.add(notification.getString(MESSAGE));
			}
			return notifications;
		}
		catch (JSONException e)
		{
			throw new ParsingException();
		}
	}

	public int parseVotes(String json) throws ParsingException
	{
		try
		{
			JSONArray votesJson = new JSONArray(json);
			return votesJson.length();

		}
		catch (JSONException e)
		{
			throw new ParsingException();
		}
	}

	private String getNoticeboardNameFromJSON(JSONObject contentItem) throws JSONException
	{
		if (contentItem.has(NOTICEBOARD))
		{
			return contentItem.getString(NOTICEBOARD);
		}
		return null;
	}

	private DateTime parseDate(String dateString)
	{
		return dateFormatter.parseDateTime(dateString);
	}

	private List<Tag> parseTags(JSONObject contentItemJSON) throws JSONException
	{
		if (contentItemJSON.has(TAGS))
		{
			List<Tag> tags = new ArrayList<Tag>();
			JSONArray jsonTags = contentItemJSON.getJSONArray(TAGS);
			for (int i = 0; i < jsonTags.length(); i++)
			{
				JSONObject jsonTag = jsonTags.getJSONObject(i);
				tags.add(new Tag(jsonTag.getString("id"), jsonTag.has(NAME) ? jsonTag.getString(NAME) : null));
			}
			return tags;
		}
		return null;
	}

	private List<Update> parseUpdates(JSONObject contentItemJSON) throws JSONException
	{	// TODO Test coverage
		ArrayList<Update> updates = new ArrayList<Update>();
		if (contentItemJSON.has(UPDATES))
		{
			JSONArray jsonUpdates = contentItemJSON.getJSONArray(UPDATES);
			for (int i = 0; i < jsonUpdates.length(); i++)
			{
				JSONObject jsonUpdate = jsonUpdates.getJSONObject(i);
				updates.add(parseJsonUpdate(jsonUpdate));
			}
		}
		return updates;
	}

	private Update parseJsonUpdate(JSONObject jsonUpdate) throws JSONException
	{
		final String id = jsonUpdate.getString("id");
		final String body = jsonUpdate.has("body") ? jsonUpdate.getString("body") : null;
		final String link = jsonUpdate.has("link") ? jsonUpdate.getString("link") : null;
		final String via = jsonUpdate.has(VIA) ? jsonUpdate.getString(VIA) : null;
		Image image = null;
		if (jsonUpdate.has("image"))
		{
			image = parseImage(jsonUpdate.getJSONObject("image"));
		}
		Video video = null;
		if (jsonUpdate.has("video"))
		{
			video = parseVideo(jsonUpdate.getJSONObject("video"));
		}
		User user = null;
		if (jsonUpdate.has(USER))
		{
			user = new UserParser().jsonToUser(jsonUpdate.getJSONObject(USER));
		}
		final DateTime created = parseDate(jsonUpdate.getString("created"));
		final DateTime modified = parseDate(jsonUpdate.getString("modified"));
		return new Update(id, user, body, link, image, video, created, modified, via);
	}

	private Image parseImage(JSONObject imageJson) throws JSONException
	{
		return new Image(imageJson.getString(SMALL), imageJson.getString(MEDIUM), imageJson.getString(LARGE));
	}

	private Video parseVideo(JSONObject videoJson) throws JSONException
	{
		String original = videoJson.optString(ORIGINAL);
		String youtubeId = videoJson.optString(YOUTUBE_ID);
		String thumbnailUrl = videoJson.optJSONObject(VIDEO_IMG) != null ? videoJson.optJSONObject(VIDEO_IMG).optString(MEDIUM_LANDSCAPE) : null;

		return new Video(original, youtubeId, thumbnailUrl);
	}

}
