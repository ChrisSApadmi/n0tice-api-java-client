package com.n0tice.api.client.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.n0tice.api.client.model.Image;

public class ImageParser
{
	private static final String ORIENTATION = "orientation";

	private static final String TINY = "tiny";
	private static final String TINY_DOUBLE = "tinydouble";
	private static final String SMALL = "small";
	private static final String SMALL_DOUBLE = "smalldouble";
	private static final String MEDIUM = "medium";
	private static final String MEDIUM_DOUBLE = "mediumdouble";
	private static final String MEDIUM_LANDSCAPE_CROP = "mediumlandscapecrop";
	private static final String MEDIUM_LANDSCAPE_CROP_DOUBLE = "mediumlandscapecropdouble";
	private static final String MEDIUM_ORIGINAL_ASPECT_DOUBLE = "mediumoriginalaspectdouble";
	private static final String LARGE = "large";
	private static final String EXTRALARGE = "extralarge";

	public Image parseImage(JSONObject imageJson) throws JSONException
	{
		return new Image(imageJson.optString(TINY), imageJson.optString(TINY_DOUBLE, null), imageJson.optString(SMALL, null), imageJson.optString(SMALL_DOUBLE, null), imageJson.optString(MEDIUM, null), imageJson.optString(MEDIUM_DOUBLE, null),
				imageJson.optString(MEDIUM_LANDSCAPE_CROP, null), imageJson.optString(MEDIUM_LANDSCAPE_CROP_DOUBLE, null), imageJson.optString(MEDIUM_ORIGINAL_ASPECT_DOUBLE, null), imageJson.optString(LARGE, null), imageJson.optString(EXTRALARGE, null),
				imageJson.optString(ORIENTATION, null));
	}

}
