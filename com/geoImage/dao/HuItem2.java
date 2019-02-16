package com.geoImage.dao;

/**
 * HuItem2 entity. @author MyEclipse Persistence Tools
 */

public class HuItem2 implements java.io.Serializable {

	// Fields

	private Integer itemId;
	private String itemRoutes;
	private String itemDocs;
	private String userIds;
	private Integer itemRouteNumber;
	private Integer itemDocNumber;
	private Integer itemUserNumber;

	// Constructors

	/** default constructor */
	public HuItem2() {
	}

	/** full constructor */
	public HuItem2(String itemRoutes, String itemDocs, String userIds,
			Integer itemRouteNumber, Integer itemDocNumber,
			Integer itemUserNumber) {
		this.itemRoutes = itemRoutes;
		this.itemDocs = itemDocs;
		this.userIds = userIds;
		this.itemRouteNumber = itemRouteNumber;
		this.itemDocNumber = itemDocNumber;
		this.itemUserNumber = itemUserNumber;
	}

	// Property accessors

	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemRoutes() {
		return this.itemRoutes;
	}

	public void setItemRoutes(String itemRoutes) {
		this.itemRoutes = itemRoutes;
	}

	public String getItemDocs() {
		return this.itemDocs;
	}

	public void setItemDocs(String itemDocs) {
		this.itemDocs = itemDocs;
	}

	public String getUserIds() {
		return this.userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public Integer getItemRouteNumber() {
		return this.itemRouteNumber;
	}

	public void setItemRouteNumber(Integer itemRouteNumber) {
		this.itemRouteNumber = itemRouteNumber;
	}

	public Integer getItemDocNumber() {
		return this.itemDocNumber;
	}

	public void setItemDocNumber(Integer itemDocNumber) {
		this.itemDocNumber = itemDocNumber;
	}

	public Integer getItemUserNumber() {
		return this.itemUserNumber;
	}

	public void setItemUserNumber(Integer itemUserNumber) {
		this.itemUserNumber = itemUserNumber;
	}

}