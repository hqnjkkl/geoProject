package com.geoImage.dao;

/**
 * OrderedFileId entity. @author MyEclipse Persistence Tools
 */

public class OrderedFileId implements java.io.Serializable {

	// Fields

	private Integer orderedFilePhotoId;
	private Integer opticsOrder;

	// Constructors

	/** default constructor */
	public OrderedFileId() {
	}

	/** full constructor */
	public OrderedFileId(Integer orderedFilePhotoId, Integer opticsOrder) {
		this.orderedFilePhotoId = orderedFilePhotoId;
		this.opticsOrder = opticsOrder;
	}

	// Property accessors

	public Integer getOrderedFilePhotoId() {
		return this.orderedFilePhotoId;
	}

	public void setOrderedFilePhotoId(Integer orderedFilePhotoId) {
		this.orderedFilePhotoId = orderedFilePhotoId;
	}

	public Integer getOpticsOrder() {
		return this.opticsOrder;
	}

	public void setOpticsOrder(Integer opticsOrder) {
		this.opticsOrder = opticsOrder;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OrderedFileId))
			return false;
		OrderedFileId castOther = (OrderedFileId) other;

		return ((this.getOrderedFilePhotoId() == castOther
				.getOrderedFilePhotoId()) || (this.getOrderedFilePhotoId() != null
				&& castOther.getOrderedFilePhotoId() != null && this
				.getOrderedFilePhotoId().equals(
						castOther.getOrderedFilePhotoId())))
				&& ((this.getOpticsOrder() == castOther.getOpticsOrder()) || (this
						.getOpticsOrder() != null
						&& castOther.getOpticsOrder() != null && this
						.getOpticsOrder().equals(castOther.getOpticsOrder())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getOrderedFilePhotoId() == null ? 0 : this
						.getOrderedFilePhotoId().hashCode());
		result = 37
				* result
				+ (getOpticsOrder() == null ? 0 : this.getOpticsOrder()
						.hashCode());
		return result;
	}

}