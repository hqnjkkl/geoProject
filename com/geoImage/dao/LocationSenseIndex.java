package com.geoImage.dao;

/**
 * LocationSenseIndex entity. @author MyEclipse Persistence Tools
 */

public class LocationSenseIndex implements java.io.Serializable {

	// Fields

	private Integer senseId;
	private String senseString;

	// Constructors

	/** default constructor */
	public LocationSenseIndex() {
	}

	/** minimal constructor */
	public LocationSenseIndex(Integer senseId) {
		this.senseId = senseId;
	}

	/** full constructor */
	public LocationSenseIndex(Integer senseId, String senseString) {
		this.senseId = senseId;
		this.senseString = senseString;
	}

	// Property accessors

	public Integer getSenseId() {
		return this.senseId;
	}

	public void setSenseId(Integer senseId) {
		this.senseId = senseId;
	}

	public String getSenseString() {
		return this.senseString;
	}

	public void setSenseString(String senseString) {
		this.senseString = senseString;
	}

}