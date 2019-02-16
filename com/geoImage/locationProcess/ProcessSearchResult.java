package com.geoImage.locationProcess;

import com.geoImage.tools.IOTools;

public class ProcessSearchResult {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		insertInto();
	}
	/**
	 * 把Google and Wikipedia search结果存到数据库中
	 */
	public static void insertInto()
	{
//		String googleFile = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/googleGazetter.txt";
//		IOTools.writeSearchResultFromFile(googleFile);
		String wikiFile = "E:/hqn/新项目初识/图片地理位置研究/地理位置数据/data/wikipediaGazetter.txt";
		IOTools.writeSearchResultFromFile(wikiFile);
		
	}
}
