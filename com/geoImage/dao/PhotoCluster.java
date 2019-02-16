package com.geoImage.dao;

/**
 * PhotoCluster entity. @author MyEclipse Persistence Tools
 */

public class PhotoCluster implements java.io.Serializable {

	// Fields

	private PhotoClusterId id;
	private GeotaggedData geotaggedData;
	private Double eradius2;
	private Integer clusterId;
	private Integer minPts;

	// Constructors

	/** default constructor */
	public PhotoCluster() {
	}

	/** minimal constructor */
	public PhotoCluster(PhotoClusterId id, GeotaggedData geotaggedData,
			Integer clusterId) {
		this.id = id;
		this.geotaggedData = geotaggedData;
		this.clusterId = clusterId;
	}

	/** full constructor */
	public PhotoCluster(PhotoClusterId id, GeotaggedData geotaggedData,
			Double eradius2, Integer clusterId, Integer minPts) {
		this.id = id;
		this.geotaggedData = geotaggedData;
		this.eradius2 = eradius2;
		this.clusterId = clusterId;
		this.minPts = minPts;
	}

	// Property accessors

	public PhotoClusterId getId() {
		return this.id;
	}

	public void setId(PhotoClusterId id) {
		this.id = id;
	}

	public GeotaggedData getGeotaggedData() {
		return this.geotaggedData;
	}

	public void setGeotaggedData(GeotaggedData geotaggedData) {
		this.geotaggedData = geotaggedData;
	}

	public Double getEradius2() {
		return this.eradius2;
	}

	public void setEradius2(Double eradius2) {
		this.eradius2 = eradius2;
	}

	public Integer getClusterId() {
		return this.clusterId;
	}

	public void setClusterId(Integer clusterId) {
		this.clusterId = clusterId;
	}

	public Integer getMinPts() {
		return this.minPts;
	}

	public void setMinPts(Integer minPts) {
		this.minPts = minPts;
	}

}