package com.n0tice.api.client.model;

public class Video
{

	private final String original;
	private final String youtubeId;
	private final String imageThumbnail;

	public Video(String aOriginal, String aYoutubeId, String aImageThumbnail)
	{
		original = aOriginal;
		youtubeId = aYoutubeId;
		imageThumbnail = aImageThumbnail;
	}

	public String getOriginal()
	{
		return original;
	}

	public String getYoutubeId()
	{
		return youtubeId;
	}

	public String getImageThumbnail()
	{
		return imageThumbnail;
	}

	@Override
	public String toString()
	{
		return "Video [original=" + original + " youtubeId=" + youtubeId + " imageThumbnail=" + imageThumbnail + "]";
	}

}
