package com.geoImage.dao;

/**
 * MyLocationSenseId entity. @author MyEclipse Persistence Tools
 */

public class MyLocationSenseId implements java.io.Serializable {

	// Fields

	private Integer docId;
	private Integer wordId;
	private Integer locationSenseId;

	// Constructors

	/** default constructor */
	public MyLocationSenseId() {
	}

	/** full constructor */
	public MyLocationSenseId(Integer docId, Integer wordId,
			Integer locationSenseId) {
		this.docId = docId;
		this.wordId = wordId;
		this.locationSenseId = locationSenseId;
	}

	// Property accessors

	public Integer getDocId() {
		return this.docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getWordId() {
		return this.wordId;
	}

	public void setWordId(Integer wordId) {
		this.wordId = wordId;
	}

	public Integer getLocationSenseId() {
		return this.locationSenseId;
	}

	public void setLocationSenseId(Integer locationSenseId) {
		this.locationSenseId = locationSenseId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MyLocationSenseId))
			return false;
		MyLocationSenseId castOther = (MyLocationSenseId) other;

		return ((this.getDocId() == castOther.getDocId()) || (this.getDocId() != null
				&& castOther.getDocId() != null && this.getDocId().equals(
				castOther.getDocId())))
				&& ((this.getWordId() == castOther.getWordId()) || (this
						.getWordId() != null && castOther.getWordId() != null && this
						.getWordId().equals(castOther.getWordId())))
				&& ((this.getLocationSenseId() == castOther
						.getLocationSenseId()) || (this.getLocationSenseId() != null
						&& castOther.getLocationSenseId() != null && this
						.getLocationSenseId().equals(
								castOther.getLocationSenseId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getDocId() == null ? 0 : this.getDocId().hashCode());
		result = 37 * result
				+ (getWordId() == null ? 0 : this.getWordId().hashCode());
		result = 37
				* result
				+ (getLocationSenseId() == null ? 0 : this.getLocationSenseId()
						.hashCode());
		return result;
	}

}