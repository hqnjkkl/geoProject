package com.geoImage.dao;

/**
 * OrderedFile entity. @author MyEclipse Persistence Tools
 */

public class OrderedFile implements java.io.Serializable {

	// Fields

	private OrderedFileId id;
	private GeotaggedData geotaggedData;
	private Double coreDistance;
	private Double reachabilityDistance;

	// Constructors

	/** default constructor */
	public OrderedFile() {
	}

	/** minimal constructor */
	public OrderedFile(OrderedFileId id, GeotaggedData geotaggedData) {
		this.id = id;
		this.geotaggedData = geotaggedData;
	}
	/**
	 * my Constructor for hqn in MyOptics.startCluster
	 * @param id
	 * @param geotaggedData
	 */
	public OrderedFile(OrderedFileId id,Double coreDistance, Double reachabilityDistance) {
		this.id = id;
		this.coreDistance = coreDistance;
		this.reachabilityDistance = reachabilityDistance;
	}
	
	/** full constructor */
	public OrderedFile(OrderedFileId id, GeotaggedData geotaggedData,
			Double coreDistance, Double reachabilityDistance) {
		this.id = id;
		this.geotaggedData = geotaggedData;
		this.coreDistance = coreDistance;
		this.reachabilityDistance = reachabilityDistance;
	}

	// Property accessors

	public OrderedFileId getId() {
		return this.id;
	}

	public void setId(OrderedFileId id) {
		this.id = id;
	}

	public GeotaggedData getGeotaggedData() {
		return this.geotaggedData;
	}

	public void setGeotaggedData(GeotaggedData geotaggedData) {
		this.geotaggedData = geotaggedData;
	}

	public Double getCoreDistance() {
		return this.coreDistance;
	}

	public void setCoreDistance(Double coreDistance) {
		this.coreDistance = coreDistance;
	}

	public Double getReachabilityDistance() {
		return this.reachabilityDistance;
	}

	public void setReachabilityDistance(Double reachabilityDistance) {
		this.reachabilityDistance = reachabilityDistance;
	}

}