package com.n0tice.api.client.model;

import java.util.List;

import org.joda.time.DateTime;

public class Content {

	private final String id;
	private final String apiUrl;
	private final String webUrl;
	private final String type;
	private final String headline;
	private final Place place;
	private final User user;
	private final String noticeboard;
	private final DateTime created;
	private final DateTime modified;
	private final List<Tag> tags;
	private final List<Update> updates;
	
	private final DateTime startDate;
	private final DateTime endDate;
	private final Reoccurence reoccurence;
	private final DateTime reoccursTo;	
	private final int interestingVotes;
	private final int reposts;
	private final Boolean awaitingModeration;
	
	public Content(String id, String apiUrl, String webUrl, String type,
			String headline, Place place, User user, String noticeboard,
			DateTime created, DateTime modified, List<Tag> tags, List<Update> updates, DateTime startDate, DateTime endDate, 
			Reoccurence reoccurence, DateTime reoccursTo, int interestingVotes, int reposts, Boolean awaitingModeration) {
		this.id = id;
		this.apiUrl = apiUrl;
		this.webUrl = webUrl;
		this.type = type;
		this.headline = headline;
		this.place = place;
		this.user = user;
		this.noticeboard = noticeboard;
		this.created = created;
		this.modified = modified;
		this.tags = tags;
		this.updates = updates;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reoccurence = reoccurence;
		this.reoccursTo = reoccursTo;
		this.interestingVotes = interestingVotes;
		this.reposts = reposts;
		this.awaitingModeration = awaitingModeration;
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
	
	public String getNoticeboard() {
		return noticeboard;
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
	
	public Reoccurence getReoccurence() {
		return reoccurence;
	}

	public DateTime getReoccursTo() {
		return reoccursTo;
	}

	public int getInterestingVotes() {
		return interestingVotes;
	}
	
	public int getReposts() {
		return reposts;
	}
	
	public Boolean isAwaitingModeration() {
		return awaitingModeration;
	}

	@Override
	public String toString() {
		return "Content [apiUrl=" + apiUrl + ", awaitingModeration="
				+ awaitingModeration + ", created=" + created + ", endDate="
				+ endDate + ", headline=" + headline + ", id=" + id
				+ ", interestingVotes=" + interestingVotes + ", modified="
				+ modified + ", noticeboard=" + noticeboard + ", place="
				+ place + ", reoccurence=" + reoccurence + ", reoccursTo="
				+ reoccursTo + ", reposts=" + reposts + ", startDate="
				+ startDate + ", tags=" + tags + ", type=" + type
				+ ", updates=" + updates + ", user=" + user + ", webUrl="
				+ webUrl + "]";
	}
	
}
