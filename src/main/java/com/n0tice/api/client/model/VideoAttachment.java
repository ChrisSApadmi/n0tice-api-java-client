package com.n0tice.api.client.model;

import java.io.InputStream;

public class VideoAttachment extends Attachment
{
	public VideoAttachment(InputStream data, String filename)
	{
		super(data, filename);
	}
}
