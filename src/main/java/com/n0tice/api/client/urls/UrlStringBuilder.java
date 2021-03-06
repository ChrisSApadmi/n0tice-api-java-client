package com.n0tice.api.client.urls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlStringBuilder
{

	private static final String UTF_8 = "UTF-8";

	private final StringBuilder output;
	private String joiner;

	public UrlStringBuilder()
	{
		this.output = new StringBuilder();
		this.joiner = "?";
	}

	public void append(String text)
	{
		output.append(text);
	}

	@Override
	public String toString()
	{
		return output.toString();
	}

	public void appendParameter(String parameter, String value) throws UnsupportedEncodingException
	{
		output.append(joiner);
		output.append(parameter + "=" + urlEncode(value));
		joiner = "&";
	}

	private String urlEncode(String value) throws UnsupportedEncodingException
	{
		return URLEncoder.encode(value, UTF_8);
	}

}
