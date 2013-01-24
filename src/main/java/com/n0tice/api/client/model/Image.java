package com.n0tice.api.client.model;

public class Image
{

	private final String tiny;
	private final String tinyDouble;
	private final String small;
	private final String smallDouble;
	private final String medium;
	private final String mediumDouble;
	private final String mediumLandscapeCrop;
	private final String mediumLandscapeCropDouble;
	private final String mediumOriginalAspectDouble;
	private final String large;
	private final String extralarge;
	private final String orientation;

	public Image(String tiny, String tinyDouble, String small, String smallDouble, String medium, String mediumDouble, String mediumLandscapeCrop, String mediumLandscapeCropDouble, String mediumOriginalAspectDouble, String large, String extralarge,
			String orientation)
	{
		this.tiny = tiny;
		this.tinyDouble = tinyDouble;
		this.small = small;
		this.smallDouble = smallDouble;
		this.medium = medium;
		this.mediumDouble = mediumDouble;
		this.mediumLandscapeCrop = mediumLandscapeCrop;
		this.mediumLandscapeCropDouble = mediumLandscapeCropDouble;
		this.mediumOriginalAspectDouble = mediumOriginalAspectDouble;
		this.large = large;
		this.extralarge = extralarge;
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

	public String getMediumLandscapeCrop()
	{
		return mediumLandscapeCrop;
	}

	public String getMediumLandscapeCropDouble()
	{
		return mediumLandscapeCropDouble;
	}

	public String getMediumOriginalAspectDouble()
	{
		return mediumOriginalAspectDouble;
	}

	public String getLarge()
	{
		return large;
	}

	public String getExtralarge()
	{
		return extralarge;
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
