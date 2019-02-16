package com.geoImage.textSegmentation;

import java.util.List;

import com.geoImage.dao.MyLocationElement;

public class MySentence {

	private String sentenceText = null;
	private int doc_id;
	//这个句子在文章中的id，可以说是第几个句子
	private int sentenceId; 
	
	//句子的开头和结尾
	private int start;
	private int end;
	
	private List<MyLocationElement> locations = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

	public MySentence(){}
	
	public MySentence(String sentenceText, int doc_id, int sentenceId,
			int start, int end, List<MyLocationElement> locations) {
		this.sentenceText = sentenceText;
		this.doc_id = doc_id;
		this.sentenceId = sentenceId;
		this.start = start;
		this.end = end;
		this.locations = locations;
	}



	public String getSentenceText() {
		return sentenceText;
	}

	public void setSentenceText(String sentenceText) {
		this.sentenceText = sentenceText;
	}

	public int getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(int doc_id) {
		this.doc_id = doc_id;
	}

	public int getSentenceId() {
		return sentenceId;
	}

	public void setSentenceId(int sentenceId) {
		this.sentenceId = sentenceId;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public List<MyLocationElement> getLocations() {
		return locations;
	}

	public void setLocations(List<MyLocationElement> locations) {
		this.locations = locations;
	}
	

}
