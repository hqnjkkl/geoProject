package com.geoImage.dao;

import java.util.List;

/**
 * TravelogueSnippet entity. @author MyEclipse Persistence Tools
 */

public class TravelogueSnippet implements java.io.Serializable {

	// Fields

	private TravelogueSnippetId id;
	private Travelogue travelogue;
	private String snippetText;
	private Integer locationWordCount;
	private Integer start;
	private Integer end;
	private Integer primaryLocationId;
	
	public List<MyLocationElement> wordList;

	// Constructors

	/** default constructor */
	public TravelogueSnippet() {
	}
	

	/** minimal constructor */
	public TravelogueSnippet(TravelogueSnippetId id, Travelogue travelogue,
			Integer locationWordCount) {
		this.id = id;
		this.travelogue = travelogue;
		this.locationWordCount = locationWordCount;
	}

	/** full constructor */
	public TravelogueSnippet(TravelogueSnippetId id, Travelogue travelogue,
			String snippetText, Integer locationWordCount, Integer start,
			Integer end, Integer primaryLocationId) {
		this.id = id;
		this.travelogue = travelogue;
		this.snippetText = snippetText;
		this.locationWordCount = locationWordCount;
		this.start = start;
		this.end = end;
		this.primaryLocationId = primaryLocationId;
	}

	// Property accessors

	public TravelogueSnippetId getId() {
		return this.id;
	}

	public void setId(TravelogueSnippetId id) {
		this.id = id;
	}

	public Travelogue getTravelogue() {
		return this.travelogue;
	}

	public void setTravelogue(Travelogue travelogue) {
		this.travelogue = travelogue;
	}

	public String getSnippetText() {
		return this.snippetText;
	}

	public void setSnippetText(String snippetText) {
		this.snippetText = snippetText;
	}

	public Integer getLocationWordCount() {
		return this.locationWordCount;
	}

	public void setLocationWordCount(Integer locationWordCount) {
		this.locationWordCount = locationWordCount;
	}

	public Integer getStart() {
		return this.start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return this.end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public Integer getPrimaryLocationId() {
		return this.primaryLocationId;
	}

	public void setPrimaryLocationId(Integer primaryLocationId) {
		this.primaryLocationId = primaryLocationId;
	}

}