package com.geoImage.dao;

/**
 * LocationSenseDuplicate entity. @author MyEclipse Persistence Tools
 */

public class LocationSenseDuplicate implements java.io.Serializable {

	// Fields

	private LocationSenseDuplicateId id;
	private MyLocationElement myLocationElement;
	private Integer wordIdDuplicate;
	private Integer docIdDuplicate;
	private String locationName;

	// Constructors

	/** default constructor */
	public LocationSenseDuplicate() {
	}

	/** minimal constructor */
	public LocationSenseDuplicate(LocationSenseDuplicateId id,
			MyLocationElement myLocationElement, Integer wordIdDuplicate,
			Integer docIdDuplicate) {
		this.id = id;
		this.myLocationElement = myLocationElement;
		this.wordIdDuplicate = wordIdDuplicate;
		this.docIdDuplicate = docIdDuplicate;
	}

	/** full constructor */
	public LocationSenseDuplicate(LocationSenseDuplicateId id,
			MyLocationElement myLocationElement, Integer wordIdDuplicate,
			Integer docIdDuplicate, String locationName) {
		this.id = id;
		this.myLocationElement = myLocationElement;
		this.wordIdDuplicate = wordIdDuplicate;
		this.docIdDuplicate = docIdDuplicate;
		this.locationName = locationName;
	}

	// Property accessors

	public LocationSenseDuplicateId getId() {
		return this.id;
	}

	public void setId(LocationSenseDuplicateId id) {
		this.id = id;
	}

	public MyLocationElement getMyLocationElement() {
		return this.myLocationElement;
	}

	public void setMyLocationElement(MyLocationElement myLocationElement) {
		this.myLocationElement = myLocationElement;
	}

	public Integer getWordIdDuplicate() {
		return this.wordIdDuplicate;
	}

	public void setWordIdDuplicate(Integer wordIdDuplicate) {
		this.wordIdDuplicate = wordIdDuplicate;
	}

	public Integer getDocIdDuplicate() {
		return this.docIdDuplicate;
	}

	public void setDocIdDuplicate(Integer docIdDuplicate) {
		this.docIdDuplicate = docIdDuplicate;
	}

	public String getLocationName() {
		return this.locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

}