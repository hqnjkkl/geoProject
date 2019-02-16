package com.geoImage.dao;

/**
 * MyLocationElement entity. @author MyEclipse Persistence Tools
 */

public class MyLocationElement implements java.io.Serializable {

	// Fields

	private MyLocationElementId id;
	private String locationName;
	private String locationOriginalText;
	private String type;
	private Integer confidence;
	private Integer start;
	private Integer end;
	private Double latitude;
	private Double longitude;
	private Integer snippetId;
	
	public void showMyLocationElement()
	{
		System.out.println("id:"+id+",locationName:"+locationName+
				",locationOriginalText:"+locationOriginalText+",start:"+start+
				",end:"+end+",snippetId:"+snippetId+",latitude:"+latitude+";");
	}

	// Constructors

	/** default constructor */
	public MyLocationElement() {
	}

	/** minimal constructor */
	public MyLocationElement(MyLocationElementId id) {
		this.id = id;
	}
	
	

	public MyLocationElement(MyLocationElementId id,
			String locationOriginalText, Integer start, Integer end) {
		this.id = id;
		this.locationOriginalText = locationOriginalText;
		this.start = start;
		this.end = end;
	}

	/** full constructor */
	public MyLocationElement(MyLocationElementId id, String locationName,
			String locationOriginalText, String type, Integer confidence,
			Integer start, Integer end, Double latitude, Double longitude,
			Integer snippetId) {
		this.id = id;
		this.locationName = locationName;
		this.locationOriginalText = locationOriginalText;
		this.type = type;
		this.confidence = confidence;
		this.start = start;
		this.end = end;
		this.latitude = latitude;
		this.longitude = longitude;
		this.snippetId = snippetId;
	}

	// Property accessors

	public MyLocationElementId getId() {
		return this.id;
	}

	public void setId(MyLocationElementId id) {
		this.id = id;
	}

	public String getLocationName() {
		return this.locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationOriginalText() {
		return this.locationOriginalText;
	}

	public void setLocationOriginalText(String locationOriginalText) {
		this.locationOriginalText = locationOriginalText;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getConfidence() {
		return this.confidence;
	}

	public void setConfidence(Integer confidence) {
		this.confidence = confidence;
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

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Integer getSnippetId() {
		return this.snippetId;
	}

	public void setSnippetId(Integer snippetId) {
		this.snippetId = snippetId;
	}
	

}