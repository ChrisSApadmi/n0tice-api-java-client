package com.n0tice.api.client.model;

import java.io.InputStream;

public class ImageAttachment extends Attachment
{
	public ImageAttachment(InputStream data, String filename)
	{
		super(data, filename);
	}
}
