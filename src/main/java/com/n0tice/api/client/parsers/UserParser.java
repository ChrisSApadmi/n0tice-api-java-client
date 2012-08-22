package com.n0tice.api.client.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.n0tice.api.client.exceptions.ParsingException;
import com.n0tice.api.client.model.AccessToken;
import com.n0tice.api.client.model.Image;
import com.n0tice.api.client.model.NewUserResponse;
import com.n0tice.api.client.model.Noticeboard;
import com.n0tice.api.client.model.User;

public class UserParser {
	
	private static final String USERS = "users";
	private static final String DOMAIN = "domain";
	private static final String FOLLOWING = "following";
	private static final String NOTICEBOARDS = "noticeboards";
	private static final String USERNAME = "username";	
	private static final String DISPLAY_NAME = "displayName";
	private static final String BIO = "bio";
	private static final String PROFILE_IMAGE = "image";
	private static final String SMALL = "small";

	public User parseUserProfile(String json) throws ParsingException {
		try {
			JSONObject userJson = new JSONObject(json);
			return jsonToUser(userJson);
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ParsingException();
		}		
	}
	
	public List<User> parseUserProfiles(String json) throws ParsingException {
		List<User> users = new ArrayList<User>();
		JSONArray usersJSON;
		try {
			usersJSON = new JSONArray(json);
			for (int i = 0; i < usersJSON.length(); i++) {
				users.add(jsonToUser(usersJSON.getJSONObject(i)));
			}
			return users;
		} catch (JSONException e) {			
			e.printStackTrace();
			throw new ParsingException();
		}
	}
	
	public NewUserResponse parseNewUserResponse(String repsonseBody) throws ParsingException {
		try {
			final JSONObject response = new JSONObject(repsonseBody);
			User user = jsonToUser(response.getJSONObject("user"));
			final JSONObject accessTokenJson = response.getJSONObject("accessToken");
			
			final String token = accessTokenJson.getString("token");
			final String secret = accessTokenJson.getString("secret");			
			return new NewUserResponse(user, new AccessToken(token, secret));
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ParsingException();
		}
	}
		
	public AccessToken parseAuthUserResponse(String repsonseBody) throws ParsingException {
		try {
			final JSONObject response = new JSONObject(repsonseBody);
			final JSONObject accessTokenJson = response.getJSONObject("accessToken");
			final String token = accessTokenJson.getString("token");
			final String secret = accessTokenJson.getString("secret");			
			return new AccessToken(token, secret);
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ParsingException();
		}
	}
	
	public User jsonToUser(JSONObject userJSON) throws JSONException {
		String displayName = null;
		String bio = null;
		Image profileImage = null;
		if (userJSON.has(DISPLAY_NAME)) {
			displayName = userJSON.getString(DISPLAY_NAME);
		}
		if (userJSON.has(BIO)) {
			bio = userJSON.getString(BIO);
		}
		if (userJSON.has(PROFILE_IMAGE)) {
			JSONObject imageJSON = userJSON.getJSONObject(PROFILE_IMAGE);
			profileImage = new Image(imageJSON.getString(SMALL));
		}
		
		Integer noticeboards = null;
		if (userJSON.has(NOTICEBOARDS)) {
			noticeboards = userJSON.getInt(NOTICEBOARDS);
		}
		
		Integer followedNoticeboards = null;
		Integer followedUsers = null;
		if (userJSON.has(FOLLOWING)) {
			JSONObject followsJSON = userJSON.getJSONObject(FOLLOWING);
			followedNoticeboards = followsJSON.getInt(NOTICEBOARDS);		
			followedUsers = followsJSON.getInt(USERS);
		}
		return new User(userJSON.getString(USERNAME), displayName, bio, profileImage, noticeboards, followedNoticeboards, followedUsers);
	}
	
	public List<Noticeboard> parseNoticeboards(String json) throws ParsingException {
		List<Noticeboard> noticeboards = new ArrayList<Noticeboard>();		
		try {
			final JSONArray noticeboardsJSON = new JSONArray(json);
			for (int i = 0; i < noticeboardsJSON.length(); i++) {
				JSONObject jsonNoticeboard = noticeboardsJSON.getJSONObject(i);
				noticeboards.add(new Noticeboard(jsonNoticeboard.getString(DOMAIN), jsonNoticeboard.getString("name"), jsonNoticeboard.getString("description")));
			}
			return noticeboards;
		} catch (JSONException e) {
			e.printStackTrace();
			throw new ParsingException();
		}
	}
	
}
