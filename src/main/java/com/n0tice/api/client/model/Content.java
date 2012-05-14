package com.n0tice.api.client.model;

import java.util.List;

import org.joda.time.DateTime;

public class Content {

	final private String id;
	final private String apiUrl;
	final private String webUrl;
	final private String type;
	final private String headline;
	final private Place place;
	final private User user;
	final private String noticeBoard;
	final private DateTime created;
	final private DateTime modified;
	final private List<Tag> tags;
	private final List<Update> updates;
	private final DateTime startDate;
	private final DateTime endDate;
	private final int interestingVotes;
	
	public Content(String id, String apiUrl, String webUrl, String type,
			String headline, Place place, User user, String noticeBoard,
			DateTime created, DateTime modified, List<Tag> tags, List<Update> updates, DateTime startDate, DateTime endDate, int interestingVotes) {
		this.id = id;
		this.apiUrl = apiUrl;
		this.webUrl = webUrl;
		this.type = type;
		this.headline = headline;
		this.place = place;
		this.user = user;
		this.noticeBoard = noticeBoard;
		this.created = created;
		this.modified = modified;
		this.tags = tags;
		this.updates = updates;
		this.startDate = startDate;
		this.endDate = endDate;
		this.interestingVotes = interestingVotes;
	}

	public String getId() {
		return id;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public String getType() {
		return type;
	}

	public String getHeadline() {
		return headline;
	}

	public Place getPlace() {
		return place;
	}

	public User getUser() {
		return user;
	}
	
	public String getNoticeBoard() {
		return noticeBoard;
	}
	
	public DateTime getCreated() {
		return created;
	}

	public DateTime getModified() {
		return modified;
	}

	public List<Tag> getTags() {
		return tags;
	}
	
	public List<Update> getUpdates() {
		return updates;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}
	
	public int getInterestingVotes() {
		return interestingVotes;
	}

	@Override
	public String toString() {
		return "Content [apiUrl=" + apiUrl + ", created=" + created
				+ ", headline=" + headline + ", id=" + id + ", modified="
				+ modified + ", noticeBoard=" + noticeBoard + ", place="
				+ place + ", type=" + type + ", user=" + user + ", webUrl="
				+ webUrl + "]";
	}
	
}
