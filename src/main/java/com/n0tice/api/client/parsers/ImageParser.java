package com.n0tice.api.client.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.n0tice.api.client.model.Image;

public class ImageParser
{

	private static final String SMALL = "small";
	private static final String MEDIUM = "medium";
	private static final String MEDIUMDOUBLE = "mediumdouble";
	private static final String MEDIUMLANDSCAPE = "mediumlandscape";
	private static final String MEDIUMLANDSCAPEDOUBLE = "mediumlandscapedouble";
	private static final String LARGE = "large";
	private static final String ORIENTATION = "orientation";

	public Image parseImage(JSONObject imageJson) throws JSONException
	{
		return new Image(imageJson.getString(SMALL), imageJson.getString(MEDIUM), imageJson.optString(MEDIUMDOUBLE), imageJson.optString(MEDIUMLANDSCAPE), imageJson.optString(MEDIUMLANDSCAPEDOUBLE), imageJson.getString(LARGE),
				imageJson.has(ORIENTATION) ? imageJson.getString(ORIENTATION) : null);
	}

}
