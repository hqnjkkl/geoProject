package com.geoImage.dao;

/**
 * TravelogueSnippetId entity. @author MyEclipse Persistence Tools
 */

public class TravelogueSnippetId implements java.io.Serializable {

	// Fields

	private Integer docId;
	private Integer snippetId;

	// Constructors

	/** default constructor */
	public TravelogueSnippetId() {
	}

	/** full constructor */
	public TravelogueSnippetId(Integer docId, Integer snippetId) {
		this.docId = docId;
		this.snippetId = snippetId;
	}

	// Property accessors

	public Integer getDocId() {
		return this.docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getSnippetId() {
		return this.snippetId;
	}

	public void setSnippetId(Integer snippetId) {
		this.snippetId = snippetId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TravelogueSnippetId))
			return false;
		TravelogueSnippetId castOther = (TravelogueSnippetId) other;

		return ((this.getDocId() == castOther.getDocId()) || (this.getDocId() != null
				&& castOther.getDocId() != null && this.getDocId().equals(
				castOther.getDocId())))
				&& ((this.getSnippetId() == castOther.getSnippetId()) || (this
						.getSnippetId() != null
						&& castOther.getSnippetId() != null && this
						.getSnippetId().equals(castOther.getSnippetId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getDocId() == null ? 0 : this.getDocId().hashCode());
		result = 37 * result
				+ (getSnippetId() == null ? 0 : this.getSnippetId().hashCode());
		return result;
	}

}