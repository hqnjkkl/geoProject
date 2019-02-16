package com.geoImage.dao;

import java.util.Date;

/**
 * UscountryArea entity. @author MyEclipse Persistence Tools
 */

public class UscountryArea implements java.io.Serializable {

	// Fields

	private Long geonameid;
	private String name;
	private String asciiname;
	private String alternatename;
	private Double latitude;
	private Double longitude;
	private String featureClass;
	private String featureCode;
	private String countryCode;
	private String cc2;
	private String admin1Code;
	private String admin2Code;
	private String admin3Code;
	private String admin4Code;
	private Long population;
	private Integer elevation;
	private Integer dem;
	private String timezone;
	private Date modificationDate;

	// Constructors

	/** default constructor */
	public UscountryArea() {
	}

	/** full constructor */
	public UscountryArea(String name, String asciiname, String alternatename,
			Double latitude, Double longitude, String featureClass,
			String featureCode, String countryCode, String cc2,
			String admin1Code, String admin2Code, String admin3Code,
			String admin4Code, Long population, Integer elevation, Integer dem,
			String timezone, Date modificationDate) {
		this.name = name;
		this.asciiname = asciiname;
		this.alternatename = alternatename;
		this.latitude = latitude;
		this.longitude = longitude;
		this.featureClass = featureClass;
		this.featureCode = featureCode;
		this.countryCode = countryCode;
		this.cc2 = cc2;
		this.admin1Code = admin1Code;
		this.admin2Code = admin2Code;
		this.admin3Code = admin3Code;
		this.admin4Code = admin4Code;
		this.population = population;
		this.elevation = elevation;
		this.dem = dem;
		this.timezone = timezone;
		this.modificationDate = modificationDate;
	}

	// Property accessors

	public Long getGeonameid() {
		return this.geonameid;
	}

	public void setGeonameid(Long geonameid) {
		this.geonameid = geonameid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAsciiname() {
		return this.asciiname;
	}

	public void setAsciiname(String asciiname) {
		this.asciiname = asciiname;
	}

	public String getAlternatename() {
		return this.alternatename;
	}

	public void setAlternatename(String alternatename) {
		this.alternatename = alternatename;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getFeatureClass() {
		return this.featureClass;
	}

	public void setFeatureClass(String featureClass) {
		this.featureClass = featureClass;
	}

	public String getFeatureCode() {
		return this.featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCc2() {
		return this.cc2;
	}

	public void setCc2(String cc2) {
		this.cc2 = cc2;
	}

	public String getAdmin1Code() {
		return this.admin1Code;
	}

	public void setAdmin1Code(String admin1Code) {
		this.admin1Code = admin1Code;
	}

	public String getAdmin2Code() {
		return this.admin2Code;
	}

	public void setAdmin2Code(String admin2Code) {
		this.admin2Code = admin2Code;
	}

	public String getAdmin3Code() {
		return this.admin3Code;
	}

	public void setAdmin3Code(String admin3Code) {
		this.admin3Code = admin3Code;
	}

	public String getAdmin4Code() {
		return this.admin4Code;
	}

	public void setAdmin4Code(String admin4Code) {
		this.admin4Code = admin4Code;
	}

	public Long getPopulation() {
		return this.population;
	}

	public void setPopulation(Long population) {
		this.population = population;
	}

	public Integer getElevation() {
		return this.elevation;
	}

	public void setElevation(Integer elevation) {
		this.elevation = elevation;
	}

	public Integer getDem() {
		return this.dem;
	}

	public void setDem(Integer dem) {
		this.dem = dem;
	}

	public String getTimezone() {
		return this.timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public Date getModificationDate() {
		return this.modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

}