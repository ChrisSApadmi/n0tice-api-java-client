package com.n0tice.api.client.model;

public class Image
{

	private final String small;
	private final String medium;
	private final String mediumDouble;
	private final String large;
	private final String orientation;
	private final String mediumLandscape;
	private final String mediumLandscapeDouble;

	public Image(String small, String medium, String mediumDouble, String mediumLandscape, String mediumLandscapeDouble, String large, String orientation)
	{
		this.small = small;
		this.medium = medium;
		this.mediumDouble = mediumDouble;
		this.mediumLandscape = mediumLandscape;
		this.mediumLandscapeDouble = mediumLandscapeDouble;
		this.large = large;
		this.orientation = orientation;
	}

	public String getSmall()
	{
		return small;
	}

	public String getMedium()
	{
		return medium;
	}

	public String getMediumDouble()
	{
		return mediumDouble;
	}

	public String getMediumLandscape()
	{
		return mediumLandscape;
	}

	public String getMediumLandscapeDouble()
	{
		return mediumLandscapeDouble;
	}

	public String getLarge()
	{
		return large;
	}

	public String getOrientation()
	{
		return orientation;
	}

	@Override
	public String toString()
	{
		return "Image [large=" + large + ", medium=" + medium + ",mediumDouble=" + mediumDouble + ", orientation=" + orientation + ", small=" + small + "]";
	}

}
