package com.geoImage.dao;

/**
 * UserItemId entity. @author MyEclipse Persistence Tools
 */

public class UserItemId implements java.io.Serializable {

	// Fields

	private Integer userId;
	private Integer itemId;

	// Constructors

	/** default constructor */
	public UserItemId() {
	}

	/** full constructor */
	public UserItemId(Integer userId, Integer itemId) {
		this.userId = userId;
		this.itemId = itemId;
	}

	// Property accessors

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserItemId))
			return false;
		UserItemId castOther = (UserItemId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null && castOther.getUserId() != null && this
				.getUserId().equals(castOther.getUserId())))
				&& ((this.getItemId() == castOther.getItemId()) || (this
						.getItemId() != null && castOther.getItemId() != null && this
						.getItemId().equals(castOther.getItemId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getItemId() == null ? 0 : this.getItemId().hashCode());
		return result;
	}

}