package com.geoImage.dao;

/**
 * FreSenseThr5 entity. @author MyEclipse Persistence Tools
 */

public class FreSenseThr5 implements java.io.Serializable {

	// Fields

	private Integer routeId;
	private Travelogue travelogue;
	private Integer routeLocationNumber;
	private Integer routeOccurence;
	private String routeText;
	private String routeWordIds;
	private Integer routeThreshold;

	// Constructors

	/** default constructor */
	public FreSenseThr5() {
	}

	/** minimal constructor */
	public FreSenseThr5(Integer routeId) {
		this.routeId = routeId;
	}

	/** full constructor */
	public FreSenseThr5(Integer routeId, Travelogue travelogue,
			Integer routeLocationNumber, Integer routeOccurence,
			String routeText, String routeWordIds, Integer routeThreshold) {
		this.routeId = routeId;
		this.travelogue = travelogue;
		this.routeLocationNumber = routeLocationNumber;
		this.routeOccurence = routeOccurence;
		this.routeText = routeText;
		this.routeWordIds = routeWordIds;
		this.routeThreshold = routeThreshold;
	}

	// Property accessors

	public Integer getRouteId() {
		return this.routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Travelogue getTravelogue() {
		return this.travelogue;
	}

	public void setTravelogue(Travelogue travelogue) {
		this.travelogue = travelogue;
	}

	public Integer getRouteLocationNumber() {
		return this.routeLocationNumber;
	}

	public void setRouteLocationNumber(Integer routeLocationNumber) {
		this.routeLocationNumber = routeLocationNumber;
	}

	public Integer getRouteOccurence() {
		return this.routeOccurence;
	}

	public void setRouteOccurence(Integer routeOccurence) {
		this.routeOccurence = routeOccurence;
	}

	public String getRouteText() {
		return this.routeText;
	}

	public void setRouteText(String routeText) {
		this.routeText = routeText;
	}

	public String getRouteWordIds() {
		return this.routeWordIds;
	}

	public void setRouteWordIds(String routeWordIds) {
		this.routeWordIds = routeWordIds;
	}

	public Integer getRouteThreshold() {
		return this.routeThreshold;
	}

	public void setRouteThreshold(Integer routeThreshold) {
		this.routeThreshold = routeThreshold;
	}

}