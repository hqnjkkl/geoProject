package com.geoImage.dao;

/**
 * MyLocationSense entity. @author MyEclipse Persistence Tools
 */

public class MyLocationSense implements java.io.Serializable {

	// Fields

	private MyLocationSenseId id;
	private MyLocationElement myLocationElement;
	private String locationSenseName;
	private Double latitude;
	private Double longitude;
	private Integer locationSenseTime;
	private Double locationSensePriorProbability;
	private String senseFeature;
	private String senseFeatureDetails;

	// Constructors

	/** default constructor */
	public MyLocationSense() {
	}

	/** minimal constructor */
	public MyLocationSense(MyLocationSenseId id,
			MyLocationElement myLocationElement) {
		this.id = id;
		this.myLocationElement = myLocationElement;
	}

	/** full constructor */
	public MyLocationSense(MyLocationSenseId id,
			MyLocationElement myLocationElement, String locationSenseName,
			Double latitude, Double longitude, Integer locationSenseTime,
			Double locationSensePriorProbability, String senseFeature,
			String senseFeatureDetails) {
		this.id = id;
		this.myLocationElement = myLocationElement;
		this.locationSenseName = locationSenseName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.locationSenseTime = locationSenseTime;
		this.locationSensePriorProbability = locationSensePriorProbability;
		this.senseFeature = senseFeature;
		this.senseFeatureDetails = senseFeatureDetails;
	}

	// Property accessors

	public MyLocationSenseId getId() {
		return this.id;
	}

	public void setId(MyLocationSenseId id) {
		this.id = id;
	}

	public MyLocationElement getMyLocationElement() {
		return this.myLocationElement;
	}

	public void setMyLocationElement(MyLocationElement myLocationElement) {
		this.myLocationElement = myLocationElement;
	}

	public String getLocationSenseName() {
		return this.locationSenseName;
	}

	public void setLocationSenseName(String locationSenseName) {
		this.locationSenseName = locationSenseName;
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

	public Integer getLocationSenseTime() {
		return this.locationSenseTime;
	}

	public void setLocationSenseTime(Integer locationSenseTime) {
		this.locationSenseTime = locationSenseTime;
	}

	public Double getLocationSensePriorProbability() {
		return this.locationSensePriorProbability;
	}

	public void setLocationSensePriorProbability(
			Double locationSensePriorProbability) {
		this.locationSensePriorProbability = locationSensePriorProbability;
	}

	public String getSenseFeature() {
		return this.senseFeature;
	}

	public void setSenseFeature(String senseFeature) {
		this.senseFeature = senseFeature;
	}

	public String getSenseFeatureDetails() {
		return this.senseFeatureDetails;
	}

	public void setSenseFeatureDetails(String senseFeatureDetails) {
		this.senseFeatureDetails = senseFeatureDetails;
	}

}