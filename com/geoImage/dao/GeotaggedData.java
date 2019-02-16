package com.geoImage.dao;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * GeotaggedData entity. @author MyEclipse Persistence Tools
 */

public class GeotaggedData implements java.io.Serializable {

	// Fields

	private Integer photoId;
	private String photoTags;
	private String ownerName;
	private Timestamp uploadDate;
	private Double longitude;
	private Double latitude;
	private Double height;
	private Double width;
	private String photoTitle;
	private String ownerUrl;
	private Integer ownerId;
	private String photoFileUrl;
	private String photoUrl;
	private Set orderedFiles = new HashSet(0);
	private Set photoClusters = new HashSet(0);

	// Constructors

	/** default constructor */
	public GeotaggedData() {
	}

	/** minimal constructor */
	public GeotaggedData(Integer photoId) {
		this.photoId = photoId;
	}
	
	
  public GeotaggedData(Integer photoId,Double longitude, Double latitude)
  {
  	 this.photoId = photoId;
       this.longitude = longitude;
       this.latitude = latitude;
  }

	/** full constructor */
	public GeotaggedData(Integer photoId, String photoTags, String ownerName,
			Timestamp uploadDate, Double longitude, Double latitude,
			Double height, Double width, String photoTitle, String ownerUrl,
			Integer ownerId, String photoFileUrl, String photoUrl,
			Set orderedFiles, Set photoClusters) {
		this.photoId = photoId;
		this.photoTags = photoTags;
		this.ownerName = ownerName;
		this.uploadDate = uploadDate;
		this.longitude = longitude;
		this.latitude = latitude;
		this.height = height;
		this.width = width;
		this.photoTitle = photoTitle;
		this.ownerUrl = ownerUrl;
		this.ownerId = ownerId;
		this.photoFileUrl = photoFileUrl;
		this.photoUrl = photoUrl;
		this.orderedFiles = orderedFiles;
		this.photoClusters = photoClusters;
	}

	// Property accessors

	public Integer getPhotoId() {
		return this.photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	public String getPhotoTags() {
		return this.photoTags;
	}

	public void setPhotoTags(String photoTags) {
		this.photoTags = photoTags;
	}

	public String getOwnerName() {
		return this.ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Timestamp getUploadDate() {
		return this.uploadDate;
	}

	public void setUploadDate(Timestamp uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getHeight() {
		return this.height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getWidth() {
		return this.width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public String getPhotoTitle() {
		return this.photoTitle;
	}

	public void setPhotoTitle(String photoTitle) {
		this.photoTitle = photoTitle;
	}

	public String getOwnerUrl() {
		return this.ownerUrl;
	}

	public void setOwnerUrl(String ownerUrl) {
		this.ownerUrl = ownerUrl;
	}

	public Integer getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getPhotoFileUrl() {
		return this.photoFileUrl;
	}

	public void setPhotoFileUrl(String photoFileUrl) {
		this.photoFileUrl = photoFileUrl;
	}

	public String getPhotoUrl() {
		return this.photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Set getOrderedFiles() {
		return this.orderedFiles;
	}

	public void setOrderedFiles(Set orderedFiles) {
		this.orderedFiles = orderedFiles;
	}

	public Set getPhotoClusters() {
		return this.photoClusters;
	}

	public void setPhotoClusters(Set photoClusters) {
		this.photoClusters = photoClusters;
	}

}