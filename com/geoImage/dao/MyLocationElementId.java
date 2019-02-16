package com.geoImage.dao;

/**
 * MyLocationElementId entity. @author MyEclipse Persistence Tools
 */

public class MyLocationElementId implements java.io.Serializable {

	// Fields

	private Integer docId;
	private Integer wordId;

	// Constructors

	/** default constructor */
	public MyLocationElementId() {
	}

	/** full constructor */
	public MyLocationElementId(Integer docId, Integer wordId) {
		this.docId = docId;
		this.wordId = wordId;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MyLocationElementId))
			return false;
		MyLocationElementId castOther = (MyLocationElementId) other;

		return ((this.getDocId() == castOther.getDocId()) || (this.getDocId() != null
				&& castOther.getDocId() != null && this.getDocId().equals(
				castOther.getDocId())))
				&& ((this.getWordId() == castOther.getWordId()) || (this
						.getWordId() != null && castOther.getWordId() != null && this
						.getWordId().equals(castOther.getWordId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getDocId() == null ? 0 : this.getDocId().hashCode());
		result = 37 * result
				+ (getWordId() == null ? 0 : this.getWordId().hashCode());
		return result;
	}
	
	public String toString()
	{
		
		return "docId:"+docId+",wordId:"+wordId;
		
	}

}