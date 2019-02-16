package com.geoImage.dao;

/**
 * UserItem2 entity. @author MyEclipse Persistence Tools
 */

public class UserItem2 implements java.io.Serializable {

	// Fields

	private UserItem2Id id;
	private Double similarity;

	// Constructors

	/** default constructor */
	public UserItem2() {
	}

	/** minimal constructor */
	public UserItem2(UserItem2Id id) {
		this.id = id;
	}

	/** full constructor */
	public UserItem2(UserItem2Id id, Double similarity) {
		this.id = id;
		this.similarity = similarity;
	}

	// Property accessors

	public UserItem2Id getId() {
		return this.id;
	}

	public void setId(UserItem2Id id) {
		this.id = id;
	}

	public Double getSimilarity() {
		return this.similarity;
	}

	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}

}