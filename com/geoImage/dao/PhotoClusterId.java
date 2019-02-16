package com.geoImage.dao;

/**
 * PhotoClusterId entity. @author MyEclipse Persistence Tools
 */

public class PhotoClusterId implements java.io.Serializable {

	// Fields

	private Integer clusterPhotoId;
	private Integer clusterOrder;

	// Constructors

	/** default constructor */
	public PhotoClusterId() {
	}

	/** full constructor */
	public PhotoClusterId(Integer clusterPhotoId, Integer clusterOrder) {
		this.clusterPhotoId = clusterPhotoId;
		this.clusterOrder = clusterOrder;
	}

	// Property accessors

	public Integer getClusterPhotoId() {
		return this.clusterPhotoId;
	}

	public void setClusterPhotoId(Integer clusterPhotoId) {
		this.clusterPhotoId = clusterPhotoId;
	}

	public Integer getClusterOrder() {
		return this.clusterOrder;
	}

	public void setClusterOrder(Integer clusterOrder) {
		this.clusterOrder = clusterOrder;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PhotoClusterId))
			return false;
		PhotoClusterId castOther = (PhotoClusterId) other;

		return ((this.getClusterPhotoId() == castOther.getClusterPhotoId()) || (this
				.getClusterPhotoId() != null
				&& castOther.getClusterPhotoId() != null && this
				.getClusterPhotoId().equals(castOther.getClusterPhotoId())))
				&& ((this.getClusterOrder() == castOther.getClusterOrder()) || (this
						.getClusterOrder() != null
						&& castOther.getClusterOrder() != null && this
						.getClusterOrder().equals(castOther.getClusterOrder())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getClusterPhotoId() == null ? 0 : this.getClusterPhotoId()
						.hashCode());
		result = 37
				* result
				+ (getClusterOrder() == null ? 0 : this.getClusterOrder()
						.hashCode());
		return result;
	}

}