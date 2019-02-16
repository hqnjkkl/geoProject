package com.geoImage.dao;

/**
 * Travelogue entity. @author MyEclipse Persistence Tools
 */

public class Travelogue implements java.io.Serializable {

	// Fields

	private Integer docId;
	private String country;
	private String geoInfo;
	private String title;
	private String source;
	private String author;
	private String date;
	private String text;
	private String url;
	private Integer wordCount;
	private Integer photoCount;
	private String photoIdList;

	// Constructors

	/** default constructor */
	public Travelogue() {
	}
	/** minimal constructor */
	public Travelogue(Integer docId) {
		this.docId = docId;
	}
	
	/** full constructor */
	public Travelogue(String country, String geoInfo, String title,
			String source, String author, String date, String text, String url,
			Integer wordCount, Integer photoCount, String photoIdList) {
		this.country = country;
		this.geoInfo = geoInfo;
		this.title = title;
		this.source = source;
		this.author = author;
		this.date = date;
		this.text = text;
		this.url = url;
		this.wordCount = wordCount;
		this.photoCount = photoCount;
		this.photoIdList = photoIdList;
	}

	// Property accessors

	public Integer getDocId() {
		return this.docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getGeoInfo() {
		return this.geoInfo;
	}

	public void setGeoInfo(String geoInfo) {
		this.geoInfo = geoInfo;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getWordCount() {
		return this.wordCount;
	}

	public void setWordCount(Integer wordCount) {
		this.wordCount = wordCount;
	}

	public Integer getPhotoCount() {
		return this.photoCount;
	}

	public void setPhotoCount(Integer photoCount) {
		this.photoCount = photoCount;
	}

	public String getPhotoIdList() {
		return this.photoIdList;
	}

	public void setPhotoIdList(String photoIdList) {
		this.photoIdList = photoIdList;
	}

}