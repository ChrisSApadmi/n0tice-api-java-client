package com.n0tice.api.client.model;

import java.util.List;

public class ResultSet {

	final private int totalMatches;
	final private int startIndex;
	final private List<Content> content;
	
	public ResultSet(int totalMatches, int startIndex, List<Content> content) {
		this.totalMatches = totalMatches;
		this.startIndex = startIndex;
		this.content = content;
	}

	public List<Content> getContent() {
		return content;
	}

	public int getTotalMatches() {
		return totalMatches;
	}

	public int getStartIndex() {
		return startIndex;
	}

	@Override
	public String toString() {
		return "ResultSet [content=" + content + ", startIndex=" + startIndex
				+ ", totalMatches=" + totalMatches + "]";
	}
	
}
