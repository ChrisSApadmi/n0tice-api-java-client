package com.n0tice.api.client.model;

import java.util.Date;
import java.util.Set;

public class Noticeboard
{

	private final String domain;
	private final String name;
	private final String description;
	private final Image background;
	private final Image cover;
	private final Date endDate;
	private final Group group;
	private final Set<MediaType> supportedMediaTypes;
	private final int contributors;
	private final int contributions;
	private final boolean moderated;
	private final boolean featured;

	public Noticeboard(String domain, String name, String description, Image background, Image cover, Date endDate, Group group, Set<MediaType> supportedMediaTypes, int contributors, int contributions, boolean moderated, boolean featured)
	{
		this.domain = domain;
		this.name = name;
		this.description = description;
		this.background = background;
		this.cover = cover;
		this.endDate = endDate;
		this.group = group;
		this.supportedMediaTypes = supportedMediaTypes;
		this.contributors = contributors;
		this.contributions = contributions;
		this.moderated = moderated;
		this.featured = featured;
	}

	public String getDomain()
	{
		return domain;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public Image getBackground()
	{
		return background;
	}

	public Image getCover()
	{
		return cover;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public Group getGroup()
	{
		return group;
	}

	public Set<MediaType> getSupportedMediaTypes()
	{
		return supportedMediaTypes;
	}

	public int getContributors()
	{
		return contributors;
	}

	public int getContributions()
	{
		return contributions;
	}

	public boolean isModerated()
	{
		return moderated;
	}

	public boolean isFeatured()
	{
		return featured;
	}

	@Override
	public String toString()
	{
		return "Noticeboard [domain=" + domain + ", name=" + name + ", description=" + description + ", background=" + background + ", cover=" + cover + ", endDate=" + endDate + ", group=" + group + ", supportedMediaTypes=" + supportedMediaTypes
				+ ", contributors=" + contributors + ", moderated=" + moderated + "]";
	}

}
