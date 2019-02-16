package com.geoImage.dao;

/**
 * MyGeoInfo entity. @author MyEclipse Persistence Tools
 */

public class MyGeoInfo implements java.io.Serializable {

	// Fields

	private String geoInfoOriginalName;
	private String geoInfoSense;
	private Double latitude;
	private Double longitude;
	private String geoInfoFeature;
	private String geoInfoFeatureDetails;

	// Constructors

	/** default constructor */
	public MyGeoInfo() {
	}

	/** minimal constructor */
	public MyGeoInfo(String geoInfoOriginalName, String geoInfoSense) {
		this.geoInfoOriginalName = geoInfoOriginalName;
		this.geoInfoSense = geoInfoSense;
	}

	/** full constructor */
	public MyGeoInfo(String geoInfoOriginalName, String geoInfoSense,
			Double latitude, Double longitude, String geoInfoFeature,
			String geoInfoFeatureDetails) {
		this.geoInfoOriginalName = geoInfoOriginalName;
		this.geoInfoSense = geoInfoSense;
		this.latitude = latitude;
		this.longitude = longitude;
		this.geoInfoFeature = geoInfoFeature;
		this.geoInfoFeatureDetails = geoInfoFeatureDetails;
	}

	// Property accessors

	public String getGeoInfoOriginalName() {
		return this.geoInfoOriginalName;
	}

	public void setGeoInfoOriginalName(String geoInfoOriginalName) {
		this.geoInfoOriginalName = geoInfoOriginalName;
	}

	public String getGeoInfoSense() {
		return this.geoInfoSense;
	}

	public void setGeoInfoSense(String geoInfoSense) {
		this.geoInfoSense = geoInfoSense;
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

	public String getGeoInfoFeature() {
		return this.geoInfoFeature;
	}

	public void setGeoInfoFeature(String geoInfoFeature) {
		this.geoInfoFeature = geoInfoFeature;
	}

	public String getGeoInfoFeatureDetails() {
		return this.geoInfoFeatureDetails;
	}

	public void setGeoInfoFeatureDetails(String geoInfoFeatureDetails) {
		this.geoInfoFeatureDetails = geoInfoFeatureDetails;
	}

}