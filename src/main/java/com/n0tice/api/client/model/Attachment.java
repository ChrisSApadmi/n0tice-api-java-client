package com.n0tice.api.client.model;

import java.io.InputStream;

public class Attachment
{
	private final InputStream data;
	private final String filename;

	public Attachment(InputStream aData, String aFilename)
	{
		data = aData;
		filename = aFilename;
	}

	public InputStream getData()
	{
		return data;
	}

	public String getFilename()
	{
		return filename;
	}

}