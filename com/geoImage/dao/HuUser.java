package com.geoImage.dao;

/**
 * HuUser entity. @author MyEclipse Persistence Tools
 */

public class HuUser implements java.io.Serializable {

	// Fields

	private Integer userId;
	private String userName;
	private Integer userNumber;
	private String userText;

	// Constructors

	/** default constructor */
	public HuUser() {
	}

	/** full constructor */
	public HuUser(String userName, Integer userNumber, String userText) {
		this.userName = userName;
		this.userNumber = userNumber;
		this.userText = userText;
	}

	// Property accessors

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserNumber() {
		return this.userNumber;
	}

	public void setUserNumber(Integer userNumber) {
		this.userNumber = userNumber;
	}

	public String getUserText() {
		return this.userText;
	}

	public void setUserText(String userText) {
		this.userText = userText;
	}

}