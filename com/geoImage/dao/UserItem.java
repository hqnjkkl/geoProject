package com.geoImage.dao;

/**
 * UserItem entity. @author MyEclipse Persistence Tools
 */

public class UserItem implements java.io.Serializable {

	// Fields

	private UserItemId id;
	private Double similarity;

	// Constructors

	/** default constructor */
	public UserItem() {
	}

	/** minimal constructor */
	public UserItem(UserItemId id) {
		this.id = id;
	}

	/** full constructor */
	public UserItem(UserItemId id, Double similarity) {
		this.id = id;
		this.similarity = similarity;
	}

	// Property accessors

	public UserItemId getId() {
		return this.id;
	}

	public void setId(UserItemId id) {
		this.id = id;
	}

	public Double getSimilarity() {
		return this.similarity;
	}

	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}

}